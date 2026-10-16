package com.example.konvert.konvert.paymono

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.example.konvert.konvert.paymono.data.Account
import com.example.konvert.konvert.paymono.data.ClientInfo
import com.example.konvert.konvert.paymono.data.JarAccount
import com.example.konvert.konvert.paymono.data.PostData
import com.example.konvert.konvert.paymono.data.MyTransaction
import com.example.konvert.konvert.paymono.data.PingData
import com.example.konvert.konvert.paymono.data.UpTrans
import com.example.konvert.konvert.paymono.database.LogStorage
import com.example.konvert.konvert.paymono.database.TransactionDatabase
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.slf4j.helpers.Util
import java.lang.IndexOutOfBoundsException
import java.util.Calendar


class MonobankService : Service() {

    private var repeatJob: Job? = null
    private var repeatJob2: Job? = null
    private val binder = MonobankServiceBinder()
    private lateinit var database: TransactionDatabase
    lateinit var shared: SharedPreferences

    var timeStamp = mutableStateOf(0L)

    var listOfTransaction = mutableStateListOf<UpTrans>()
        private set
    var transactionStatus = mutableStateOf<String>("")
        private set

    val jarsAndAccounts = mutableStateListOf<JarAccount>()
    val jarsAndAccountsForShow = mutableStateListOf<JarAccount>()

    lateinit var whiteCard: String
    lateinit var blackCard: String
    lateinit var fopCard: String

    var currentState = mutableStateOf<DeliveryStatus>(DeliveryStatus.Loading())
        private set

    var serverIsRunning = mutableStateOf(false)
        private set

    lateinit var notification: Notification

    var cardLoaderStatus = mutableStateOf(false)
        private set

    val delayState = mutableLongStateOf(0)
    var firstStart = mutableStateOf(true)


    private var pingJob: Job? = null


    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        loadPreferences()
    }

    private fun loadPreferences() {
        shared = this@MonobankService.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        whiteCard = shared.getString("white_card", "").toString()
        blackCard = shared.getString("black_card", "").toString()
        fopCard = shared.getString("fop_card", "").toString()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.RESTART.toString() -> restart()
            Actions.STOP.toString() -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("ForegroundServiceType")
    private fun start() {
        serverIsRunning.value = true
        firstStart.value = true
        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|start()")

        loadPreferences()
        startInitDataAndLoop()

        pingJob?.cancel()
        pingJob = sendPing()
    }

    @SuppressLint("ForegroundServiceType")
    private fun restart() {
        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|restart()")

        // 1. Зупиняємо старі задачі, якщо вони крутилися
        repeatJob?.cancel()
        repeatJob2?.cancel()

        pingJob?.cancel()
        pingJob = sendPing()

        // 2. Оновлюємо змінні з SharedPreferences (нові картки/токен)
        loadPreferences()
        timeStamp.value = shared.getLong("delay_time", 0L)
        firstStart.value = true // Робимо оновлення початкових даних для нових налаштувань

        // 3. Запускаємо ініціалізацію та цикл заново з новими даними
        startInitDataAndLoop()
        serverIsRunning.value = true
    }

    @SuppressLint("ForegroundServiceType")
    private fun stop() {
        repeatJob?.cancel()
        repeatJob2?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        serverIsRunning.value = false
        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|stop()")

        pingJob?.cancel()
    }

    /**
     * Головний метод старту: спочатку ініціалізація (getInit),
     * а після її завершення — запуск циклу моніторингу виписок.
     */
    private fun startInitDataAndLoop() {
        timeStamp.value = shared.getLong("delay_time", 0L)

        // Запускаємо фоновий процес ініціалізації та подальшого циклу
        repeatJob2 = CoroutineScope(Dispatchers.IO).launch {
            var delayTime = 0L
            if (System.currentTimeMillis() - timeStamp.value < 60000) {
                cardLoaderStatus.value = true
                delayTime = 60000 - (System.currentTimeMillis() - timeStamp.value)
                delayState.longValue = delayTime
            }
            delay(delayTime)
            cardLoaderStatus.value = false

            try {
                database = TransactionDatabase.getInstance(this@MonobankService)
                listOfTransaction.clear()
                listOfTransaction.addAll(database.dao.getAll())
                LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|TransactionDatabase ініціалізовано (${listOfTransaction.size} транзакцій)")
            } catch (e: Exception) {
                currentState.value = DeliveryStatus.Error("DATABASE: $e")
                Log.e("MESSI BAD: ", e.toString())
                LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|DATABASE ERROR=$e FAIL")
            }

            // Викликаємо GET_INIT один раз на старті або рестарті
            getInit()

            // Налаштовуємо та запускаємо Foreground Notification
            withContext(Dispatchers.Main) {
                startTransactionListener()
            }

            // Запускаємо безперервний цикл перевірки транзакцій по вже заповнених картках
            repeatJob = startAllAccounts()
        }
    }

    private suspend fun getInit() {
        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|Get_Init() запуск")

        val response = Network.getInitData()
        val initData = response.body<ClientInfo>()
        val current = System.currentTimeMillis()
        shared.edit().putLong("delay_time", current).apply()

        when (response.status.value) {
            429 -> currentState.value = DeliveryStatus.TooMany()
            in 400..499 -> currentState.value = DeliveryStatus.WrongXtoken()
            else -> currentState.value = DeliveryStatus.Success(response.status.value.toString())
        }

        if (initData.clientId == null) return

        if (firstStart.value) {
            jarsAndAccounts.clear()
        }
        jarsAndAccountsForShow.clear()
        delay(100)

        if (initData.accounts != null) {
            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КАРТ на акаунті=${initData.accounts.size}")
            val preAccountsIbans = mutableListOf<String?>()
            val preAccountsCards = mutableListOf<String>()

            try {
                initData.accounts.forEach { it ->
                    preAccountsIbans.add(it.iban)
                    it.maskedPan?.forEach { tit -> preAccountsCards.add(tit) }
                }

                UtilsObject.all_cards.onEachIndexed { _d, (cardColor, cardNumber, ibanNumber, cardLink) ->
                    if (ibanNumber in preAccountsIbans || ("${cardNumber.take(6)}******${cardNumber.takeLast(4)}" in preAccountsCards)) {
                        val account: Account? = when {
                            cardColor == UtilsObject.fop_carta -> initData.accounts.find { it.type == UtilsObject.fop_carta }
                            ibanNumber in preAccountsIbans -> initData.accounts.find { it.iban == ibanNumber }
                            else -> initData.accounts.find {
                                it.maskedPan?.contains("${cardNumber.take(6)}******${cardNumber.takeLast(4)}") == true
                            }
                        }

                        // Перевіряємо умови відповідності (IBAN, маска карти, посилання тощо)
                        val isFopValid = (account?.type == UtilsObject.fop_carta && account.iban == ibanNumber)
                        val isRegularValid = (ibanNumber == account?.iban || ibanNumber == "") &&
                                (account?.maskedPan?.contains("${cardNumber.take(6)}******${cardNumber.takeLast(4)}") == true) &&
                                (account?.sendId == cardLink?.substringAfter("ua/") || cardLink == "" || cardLink == null)

                        if (account != null && (isFopValid || isRegularValid)) {
                            // УСПІХ: Картка повністю збіглася
                            val item = JarAccount(
                                account.id ?: "", account.sendId ?: "", account.balance ?: 0,
                                account.creditLimit, account.type, account.currencyCode ?: 0,
                                account.cashbackType, account.maskedPan, account.iban, card = cardNumber
                            )
                            if (firstStart.value) {
                                jarsAndAccounts.add(item)
                            }
                            jarsAndAccountsForShow.add(item)
                        } else {
                            // ПОМИЛКА ДАНИХ: Картка є в акаунті, але реквізити (IBAN/силка) не збіглися
                            val item = JarAccount(
                                id = cardNumber, sendId = "errorCard", balance = 0,
                                type = "yforsort", currencyCode = 0, card = cardNumber
                            )
                            jarsAndAccountsForShow.add(item)
                        }

                    } else {
                        // КАРТИ ВЗАГАЛІ НЕМАЄ В АКАУНТІ ТОКЕНА
                        val item = JarAccount(
                            id = cardNumber, balance = 0, sendId = "errorCard",
                            type = "yforsort", currencyCode = 0, card = cardNumber
                        )
                        jarsAndAccountsForShow.add(item)
                    }
                }
            } catch (e: Exception) {
                LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|Помилка при розподілі карток е=$e| FAIL")
                currentState.value = DeliveryStatus.Error("CARDS: $e")
            }
        }

        delay(100)
        if (initData.jars != null) {
            try {
                val preAccountsJars = initData.jars.map { it.sendId ?: "" }

                UtilsObject.all_jars_cards.onEachIndexed { _, (sendId, cardNumber) ->
                    if (sendId in preAccountsJars) {
                        val jar = initData.jars.find { it.sendId == sendId }
                        val item = JarAccount(
                            id = jar?.id ?: "", sendId = jar?.sendId ?: "", balance = jar?.balance ?: 0,
                            title = jar?.title, currencyCode = jar?.currencyCode ?: 0,
                            description = jar?.description, goal = jar?.goal, type = "xforsort", card = cardNumber
                        )
                        if (firstStart.value) {
                            jarsAndAccounts.add(item)
                        }
                        jarsAndAccountsForShow.add(item)
                    } else {
                        // БАНКА НЕ ЗНАЙДЕНА АБО ПОМИЛКА — додаємо з errorBank, щоб вона не зникала з UI
                        val item = JarAccount(
                            id = cardNumber, balance = 0, sendId = "errorBank",
                            type = "zforsort", currencyCode = 0, card = cardNumber
                        )
                        jarsAndAccountsForShow.add(item)
                    }
                }
            } catch (e: Exception) {
                LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|Банка помилка е=$e| FAIL")
            }
        }

        firstStart.value = false
    }

    private fun startTransactionListener() {
        val stopIntent = Intent(this, MonobankService::class.java).also {
            it.action = Actions.STOP.toString()
        }
        val pendingIntent = PendingIntent.getService(this, 102, stopIntent, PendingIntent.FLAG_IMMUTABLE) as PendingIntent

        notification = NotificationCompat.Builder(this, "monobank_server")
            .setSmallIcon(R.drawable.logo_upd)
            .setContentTitle("Додаток активний")
            .setContentText("Очікування нових транзакцій...")
            .setOngoing(false)
            .setContentIntent(pendingIntent)
            .build()

        this@MonobankService.startForeground(1, notification)
    }

    /**
     * Чистий цикл опитування виписок. Жодних викликів getInit() всередині!
     */
    private fun startAllAccounts(): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            Log.e("MESSI_LOOP", "=== СТАРТ ЦИКЛУ ОПИТУВАННЯ ===")
            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|=== СТАРТ ЦИКЛУ ОПИТУВАННЯ === Кількість карток: ${jarsAndAccounts.size}")

            while (isActive) {
                if (jarsAndAccounts.isNotEmpty()) {
                    val currentAccounts = jarsAndAccounts.distinctBy { it.id }
                    Log.e("MESSI_LOOP", "Початок кола. Унікальних карток: ${currentAccounts.size}")

                    for ((index, account) in currentAccounts.withIndex()) {
                        if (!isActive) break

                        Log.e("MESSI_LOOP", "Опитування картки #${index + 1} з ID: ${account.id}")
                        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|Опитування картки ${account.id}")

                        try {
                            getAccountsData(account.id, jarsAndAccounts)
                            Log.e("MESSI_LOOP", "Картка ${account.id} успішно опитана")
                        } catch (e: Exception) {
                            Log.e("MESSI_LOOP", "ПОМИЛКА картки ${account.id}: ${e.message}")
                            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|ПОМИЛКА картки ${account.id}: $e| FAIL")
                        }

                        delay(5000L) // Пауза між картками
                    }
                } else {
                    Log.e("MESSI_LOOP", "УВАГА: jarsAndAccounts порожній!")
                }

                Log.e("MESSI_LOOP", "Кінець кола. Чекаємо 60 секунд до наступного...")
                LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|Кінець кола. Очікування 60 секунд...")

                delay(60000L)
            }
        }
    }

    private suspend fun getAccountsData(accountId: String, listOfJarsAndAccounts: List<JarAccount>) {
        val timelapse = System.currentTimeMillis() - UtilsObject.startedTimestamp

        val fromTime = if (timelapse < 86400000) {
            (System.currentTimeMillis() - timelapse) / 1000
        } else {
            System.currentTimeMillis() / 1000 - 86400
        }

        try {
            val myResponse = Network.getTransactions(accountId, fromTime)
            val lastTransaction = myResponse.body<List<MyTransaction>>()
            transactionStatus.value = "Status: ${myResponse.status}"

            // Оновлюємо баланс картки з останньої транзакції, якщо вона є
            if (lastTransaction.isNotEmpty()) {
                val latestBalance = lastTransaction.first().balance ?: 0

                // Шукаємо і оновлюємо в загальному списку
                jarsAndAccounts.find { it.id == accountId }?.let { target ->
                    val index = jarsAndAccounts.indexOf(target)
                    if (index != -1) {
                        jarsAndAccounts[index] = target.copy(balance = latestBalance)
                    }
                }

                // Шукаємо і оновлюємо в списку для показу в UI
                jarsAndAccountsForShow.find { it.id == accountId }?.let { target ->
                    val index = jarsAndAccountsForShow.indexOf(target)
                    if (index != -1) {
                        jarsAndAccountsForShow[index] = target.copy(balance = latestBalance)
                    }
                }
            }

            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|АКАУНТ дані status=${myResponse.status.value}|транзакцій=${lastTransaction.size}")

            val jarAccount: JarAccount? = listOfJarsAndAccounts.find { it.id == accountId }

            lastTransaction.forEach { it ->
                val exists = database.dao.getTransactionById(it.id)
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it.time?.times(1000) ?: 0

                if (!exists) {
                    val postData = PostData(
                        card = getCardNumber(myTransaction = it, jarAccount = jarAccount),
                        iban = getIban(myTransaction = it, jarAccount = jarAccount),
                        type = if (jarAccount?.type == "xforsort") "jar" else jarAccount?.type ?: "",
                        link = getCardLink(myTransaction = it, jarAccount = jarAccount),
                        bank = if (jarAccount?.goal != null) "https://send.monobank.ua/${jarAccount.sendId}" else "",
                        transaction_id = it.id,
                        time = getCorrectTime(calendar),
                        date = getCorrectDay(calendar),
                        description = it.description ?: "",
                        mcc = it.mcc ?: 0,
                        originalMcc = it.originalMcc ?: 0,
                        amount = it.amount ?: 0,
                        operationAmount = it.operationAmount ?: 0,
                        currencyCode = it.currencyCode ?: 0,
                        commissionRate = it.commissionRate ?: 0,
                        cashbackAmount = it.cashbackAmount ?: 0,
                        balance = it.balance ?: 0,
                        hold = it.hold ?: false,
                        app = "Monobank",
                        id = UtilsObject.device_id,
                        user_id = UtilsObject.user_id
                    )

                    val responseTrans = Network.sendServer(postData)
                    LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|ТРАНЗАКЦІЯ id=${it.amount} на сервер|status=${responseTrans.status.value}| SUCCESS")

                    delay(500)
                    try {
                        Network.sendTestData(postData)
                        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|і на тестовий сервер| SUCCESS")
                    } catch (e: Exception) {
                        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|ERROR з тестовим сервером e=$e| FAIL")
                    }

                    val uptrans = UpTrans(
                        id = it.id,
                        time = getCorrectTime(calendar),
                        date = getCorrectDay(calendar),
                        description = it.description ?: "",
                        mcc = it.mcc,
                        amount = (it.amount?.div(100.0)).toString(),
                        originalMcc = it.originalMcc,
                        operationAmount = it.operationAmount,
                        currencyCode = it.currencyCode,
                        commissionRate = it.commissionRate,
                        cashbackAmount = it.cashbackAmount,
                        balance = it.balance,
                        hold = it.hold,
                        comment = it.comment,
                        receiptId = it.receiptId,
                        invoiceId = it.invoiceId,
                        counterEdrpou = it.counterEdrpou,
                        counterIban = it.counterIban,
                        counterName = it.counterName,
                        type = if (jarAccount != null) "mono " + jarAccount.type else "",
                        bankaTitle = jarAccount?.title,
                        card = jarAccount?.card,
                        iban = jarAccount?.iban,
                        status = responseTrans.status.value == 200
                    )

                    delay(2000)
                    if (responseTrans.status.value == 200) {
                        database.dao.upsertTransaction(uptrans)
                        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|ТРАНЗАКЦІЯ додана в DB| SUCCESS")
                    }
                    listOfTransaction.add(uptrans)
                }
            }
        } catch (e: Exception) {
            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|Помилка з транзакціями е=$e| FAIL")
            transactionStatus.value = "Status ERROR: $e"
            Log.e("MESSI ERROR network: ", e.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        repeatJob?.cancel()
        repeatJob2?.cancel()
        pingJob?.cancel()
        serverIsRunning.value = false
        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|onDestroy()")
        stopSelf()
    }

    enum class Actions {
        START, STOP, RESTART
    }

    inner class MonobankServiceBinder : Binder() {
        fun getService() = this@MonobankService
    }
}

//class MonobankService : Service() {
//
//
//    private var repeatJob: Job = GlobalScope.launch { }
//    private var repeatJob2: Job = GlobalScope.launch { }
//    private val binder = MonobankServiceBinder()
//    private lateinit var database: TransactionDatabase
//    //var clientInfo = mutableStateOf<ClientInfo?>(null)
//    lateinit var shared: SharedPreferences
//
//    var timeStamp = mutableStateOf(0L)
//
//    var listOfTransaction = mutableStateListOf<UpTrans>()
//        private set
//    var transactionStatus = mutableStateOf<String>("")
//        private set
//
//    val jarsAndAccounts = mutableStateListOf<JarAccount>()
//
//    val jarsAndAccountsForShow = mutableStateListOf<JarAccount>()
//
//    //val uberTest = mutableSetOf<JarAccount>()
//    lateinit var whiteCard: String
//    lateinit var blackCard: String
//    lateinit var fopCard: String
//
//
//    var currentState = mutableStateOf<DeliveryStatus>(DeliveryStatus.Loading())
//        private set
//
//    var serverIsRunning = mutableStateOf(false)
//        private set
//
//    lateinit var notification: Notification
//
//    var cardLoaderStatus = mutableStateOf(false)
//        private set
//
//    val delayState = mutableLongStateOf(0)
//
//    var firstStart = mutableStateOf(false)
//
//    var helper = mutableStateOf(false)
//
//
//    override fun onBind(p0: Intent?): IBinder {
//        return binder
//
//    }
//
//
//    override fun onCreate() {
//        super.onCreate()
//        shared = this@MonobankService.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
//        whiteCard = shared.getString("white_card", "").toString()
//        blackCard = shared.getString("black_card", "").toString()
//        fopCard = shared.getString("fop_card", "").toString()
//        //Log.e("MESSI onCreate: ", UtilsObject.suka.toString())
//    }
//
//    override fun onStart(intent: Intent?, startId: Int) {
//        super.onStart(intent, startId)
//        timeStamp.value = shared.getLong("delay_time", 0L)
//        //Log.e("MESSI onStart: ", timeStamp.value.toString())
//    }
//
//
//
////    var timer = object : TimerTask() {
////        override fun run() {
////            cardLoaderStatus.value = false
////            Log.e("MESSI TIMER OFF", "jhg" )
////        }
////    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        when (intent?.action) {
//            Actions.START.toString() -> start()
//            Actions.RESTART.toString() -> restart()
//            Actions.STOP.toString() -> stop()
//        }
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    @SuppressLint("ForegroundServiceType")
//    private fun start() {
//        serverIsRunning.value = true
//        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|start()")
//
//        //Log.e("MESSI start(): ", UtilsObject.suka.toString())
//        //if (!UtilsObject.suka) {
//            startInitData()
//        //}
//    }
//
//    @SuppressLint("ForegroundServiceType")
//    private fun stop() {
//        repeatJob.cancel()
//        repeatJob2.cancel()
//        //stopForeground(STOP_FOREGROUND_REMOVE)
//        stopSelf()
//        serverIsRunning.value = false
//        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|stop()")
//        onDestroy()
//        //Log.e("MESSI timer START: ", timer.scheduledExecutionTime().toString())
//        //cardLoaderStatus.value = true
//    }
//
//    @SuppressLint("ForegroundServiceType")
//    private fun restart() {
//        repeatJob.cancel()
//        repeatJob2.cancel()
//        //cardLoaderStatus.value = true
//        timeStamp.value = shared.getLong("delay_time", 0L)
//        startInitData()
//        serverIsRunning.value = true
//        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|restart()")
//
//    }
//
//
//
//    @SuppressLint("SuspiciousIndentation")
//    private fun startInitData(): Job {
//
//        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|StartInitData()")
//
//
//        firstStart.value = true
//        //helper.value = false
//
//        repeatJob2 = sendPing()
//        repeatJob2.start()
//
//        return CoroutineScope(Dispatchers.IO).launch {
//            var delay = 0L
//            if (System.currentTimeMillis() - timeStamp.value < 60000) {
//                cardLoaderStatus.value = true
//                delay = 60000 - (System.currentTimeMillis() - timeStamp.value)
//                delayState.longValue = delay
//                //Log.e("MESSI 2 timeStamp: ", delay.toString())
//            }
//            //Log.e("MESSI 1: ", "test")
//            //Log.e("MESSI delay is: ", delay.toString())
//            delay(delay)
//            cardLoaderStatus.value = false
//            //Log.e("MESSI 3: ", "after delay 1")
//
//                try {
//
//                    database = TransactionDatabase.getInstance(this@MonobankService)
//                    listOfTransaction = mutableStateListOf()
//                    listOfTransaction.addAll(database.dao.getAll())
//
//                    LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|TransactionDatabase initialization (${listOfTransaction.size} транзакцій)")
//
//                } catch (e: Exception) {
//                    currentState.value = DeliveryStatus.Error("DATABASE: $e")
//                    Log.e("MESSI BAD: ", e.toString())
//                    LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|DATABASE ERROR=$e FAIL")
//
//                }
////
////                    val listOfAccs = listOf(Account("123", "123", 123, 123, "black", 123, "asdas", null, "123123123123123"))
////
////                    val preAccounts = listOfAccs.map { it.maskedPan?.get(0)?.takeLast(4) }
////
////                    Log.e("MESSI TEST: ", preAccounts.toString())
//
//                    if (firstStart.value) {
//                        getInit()
//                        delay(500)
//                        startTransactionListener()
//                    } else {
//                        startTransactionListener()
//                    }
//
//                    //Log.e("MESSI startInitData: ", jarsAndAccountsForShow.toString())
//
//        }
//    }
//
//    private suspend fun getInit() {
//
//        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|Get_Init() first start=${firstStart.value}")
//
//
//        val response = Network.getInitData()
//        val initData = response.body<ClientInfo>()
//        val current = System.currentTimeMillis()
//        shared.edit().putLong("delay_time", current).apply()
//
//        when (response.status.value) {
//            429 -> currentState.value = DeliveryStatus.TooMany()
//            in 400..499 -> currentState.value = DeliveryStatus.WrongXtoken()
//            200 -> currentState.value = DeliveryStatus.Success(response.status.value.toString())
//            else -> currentState.value = DeliveryStatus.Success(response.status.value.toString())
//        }
//
//        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|FIRST INIT status=${response.status.value}")
//
//
//        if (initData.clientId == null) {
//            return
//        }
//        if (firstStart.value) {
//            jarsAndAccounts.clear()
//        }
//        jarsAndAccountsForShow.clear()
//        delay(100)
//        Log.e("MESSI getInit_getInit: ", UtilsObject.all_cards.toString())
//
//        if (initData.accounts != null) {
//            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КАРТ на акаунті=${initData.accounts.size}")
//
//            val preAccountsIbans = mutableListOf<String?>()
//            val preAccountsCards = mutableListOf<String>()
//
//            try {
//                initData.accounts.forEach { it ->
//
//                    preAccountsIbans.add(it.iban)
//                    it.maskedPan?.forEach { tit ->
//                        preAccountsCards.add(tit)
//                    }
//                }
//                //preAccounts.add(UtilsObject.all_cards.get(UtilsObject.fop_carta)?.takeLast(4))
//
//                //Log.e("MESSI FOP_CARD: ", UtilsObject.fopCardNumber.toString())
//                Log.e("MESSI FOP_FALSE: ", helper.value.toString())
//
//
//                Log.e("MESSI ACCOUNTS: ", initData.accounts.toString())
//                Log.e("MESSI PREAC: ", preAccountsIbans.toString())
//
//
//                UtilsObject.all_cards.onEachIndexed { _d, (cardColor, cardNumber, ibanNumber, cardLink) ->
//
//
//                    if (ibanNumber in preAccountsIbans || ("${cardNumber.take(6)}******${cardNumber.takeLast(4)}" in preAccountsCards)) {
//                        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КАРТА #$_d($cardColor) ВЕРИФІКОВАНА")
//
//
//                        val account: Account? //= initData.accounts.find { it.iban == ibanNumber }
//
//                        if (cardColor == UtilsObject.fop_carta) {
//                            account = initData.accounts.find {
//                                it.type == UtilsObject.fop_carta
//                            }
//                        } else if (ibanNumber in preAccountsIbans) {
//                           account = initData.accounts.find { it.iban == ibanNumber }
//                        } else {
//                            account = initData.accounts.find {
//                                it.maskedPan?.find { jit ->
//                                    jit == "${cardNumber.take(6)}******${cardNumber.takeLast(4)}"
//                                } == "${cardNumber.take(6)}******${cardNumber.takeLast(4)}"
//                            }
//                        }
//
//
//                        var item = JarAccount(account?.id ?: "", account?.sendId ?: "", account?.balance ?: 0, account?.creditLimit, account?.type, account?.currencyCode ?: 0, account?.cashbackType, account?.maskedPan, account?.iban, card = cardNumber)
//
//                        Log.e("MESSI getInit forEach: ", "$cardNumber: $cardColor")
//
//                        if (account?.type == UtilsObject.fop_carta
//                            && account.iban == ibanNumber
//                            ) {
//                            Log.e("MESSI getInit FOP works: ", account.type.toString())
//                            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КАРТА #$_d($cardColor)|type+|iban+|card+| SUCCESS")
//
//                            if (firstStart.value) {
//                                jarsAndAccounts.add(item)
//                            }
//                            jarsAndAccountsForShow.add(item)
//
//                        } else if (account?.type == UtilsObject.fop_carta) {
//                            Log.e("MESSI getInit FOP false: ", account.type.toString())
//                            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КАРТА #$_d($cardColor)|type+|iban OR card -| FAIL")
//
//                            item = JarAccount(account?.id ?: "", "errorCard", account?.balance ?: 0, account?.creditLimit, type = "yforsort", account?.currencyCode ?: 0, account?.cashbackType, account?.maskedPan, account?.iban, card = cardNumber)
//                            jarsAndAccountsForShow.add(item)
//
//                        }
//                        else if (
//                            ibanNumber == account?.iban
//                            && (account.maskedPan?.find { card ->
//                                card == "${cardNumber.take(6)}******${cardNumber.takeLast(4)}"
//                            } != null)
//                            && (account.sendId == cardLink?.substringAfter("ua/") || cardLink == "")
//                            ) {
//                            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КАРТА #$_d($cardColor)|iban+|link+| SUCCESS")
//
//                            Log.e("MESSI getInit blackOrWhite: ", cardColor)
//                            if (firstStart.value) {
//                                jarsAndAccounts.add(item)
//                            }
//                            jarsAndAccountsForShow.add(item)
//                        } else if (
//                            (account?.maskedPan?.find { card ->
//                                card == "${cardNumber.take(6)}******${cardNumber.takeLast(4)}"
//                            } != null) && ((ibanNumber == "" || ibanNumber == account.iban) && (account.sendId == cardLink?.substringAfter("ua/") || cardLink == ""))
//                        ) {
//                            Log.e("MESSI only card: ", cardColor)
//                            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КАРТА #$_d($cardColor)|card+ |iban+ |link+| SUCCESS")
//
//                            if (firstStart.value) {
//                                jarsAndAccounts.add(item)
//                            }
//                            jarsAndAccountsForShow.add(item)
//                        } else if (
//                            ibanNumber == account?.iban &&
//                            ((cardNumber == "" || (account.maskedPan?.find { card ->
//                                card == "${cardNumber.take(6)}******${cardNumber.takeLast(4)}"
//                            } != null)) && (account.sendId == cardLink?.substringAfter("ua/") || cardLink == ""))
//                        ) {
//                            Log.e("MESSI only IBAN: ", cardColor)
//                            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КАРТА #$_d($cardColor)|iban+|card+|link+| SUCCESS")
//
//                            if (firstStart.value) {
//                                jarsAndAccounts.add(item)
//                            }
//                            jarsAndAccountsForShow.add(item)
//                        } else if (
//
//                            (ibanNumber != "" && ibanNumber != account?.iban)
//                            ||
//                            (cardNumber != "" && account?.maskedPan?.find { card -> card == "${cardNumber.take(6)}******${cardNumber.takeLast(4)}" } == null)
//                            ||
//                            (cardLink?.substringAfter("ua/") != "" && account?.sendId != cardLink?.substringAfter("ua/"))
//
//                            ) {
//                            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КАРТА #$_d($cardColor)|iban=${ibanNumber == account?.iban}|card=${account?.maskedPan?.find { card -> card == "${cardNumber.take(6)}******${cardNumber.takeLast(4)}" }}|link=${account?.sendId != cardLink?.substringAfter("ua/")}| FAIL")
//
////                            Log.e("MESSI BLACK Iban or Link ERROR: ", UtilsObject.all_cards.toString())
////                            Log.e("MESSI BLACK 1: ", (account?.maskedPan?.find { card ->
////                                card.takeLast(4) == cardNumber.takeLast(4)
////                            } != null).toString())
////                            Log.e("MESSI BLACK 2: ", account?.maskedPan?.find { card ->
////                                card.takeLast(4) == cardNumber.takeLast(4)
////                            }.toString())
////                            Log.e("MESSI BLACK 3: ", account?.maskedPan.toString())
//
//                            item = JarAccount(account?.id ?: "", "errorCard", account?.balance ?: 0, account?.creditLimit, type = "yforsort", account?.currencyCode ?: 0, account?.cashbackType, account?.maskedPan, account?.iban, card = cardNumber)
//                            jarsAndAccountsForShow.add(item)
//                        }
//
//                        else {
//                            if (blackCard == cardNumber && account?.type == "white") {
//                                shared.edit().putString("white_card", cardNumber).apply()
//                            }
//                            if (whiteCard == cardNumber && account?.type == "black") {
//                                shared.edit().putString("black_card", cardNumber).apply()
//                            }
//                            //val list = jarsAndAccounts.map { it.card }
//
//                            if (UtilsObject.all_cards.filter { it.cardNumber == cardNumber }.size > 1) {
//                                Log.e("MESSI getInit in Jars: ", UtilsObject.all_cards.toString())
//                                LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КАРТА #$_d($cardColor) вже існує| FAIL")
//
//                                item = JarAccount(account?.id ?: "", "errorCard", account?.balance ?: 0, account?.creditLimit, type = "yforsort", account?.currencyCode ?: 0, account?.cashbackType, account?.maskedPan, account?.iban, card = cardNumber)
//                                jarsAndAccountsForShow.add(item)
//                            } else {
//                                Log.e("MESSI NOT getInit in Jars: ", cardNumber.toString())
//                                LogStorage.addLog(this@MonobankService, "${getCurrentDate()}| КАРТА #$_d($cardColor)| SUCCESS")
//
//                                if (firstStart.value) {
//                                    jarsAndAccounts.add(item)
//                                }
//                                jarsAndAccountsForShow.add(item)
//                            }
//
//                        }
//
//                    } else {
//                        Log.e("MESSI NOT in preAccounts: ", cardNumber.toString())
//                        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КАРТА #$_d($cardColor) НЕ збігаються ДАНІ в акаунті preIbans=(${preAccountsIbans}) preCards=(${preAccountsCards})| FAIL")
//
//
//                        val item = JarAccount(id = cardNumber, balance = 0, sendId = "errorCard", type = "yforsort", currencyCode = 0, card = cardNumber)
//                        jarsAndAccountsForShow.add(item)
//                    }
//                }
//
//            } catch (e: Exception) {
//                LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|ЯКАСЬ ПОМИЛКА при розподілі карток е=$e preIbans=(${preAccountsIbans}) preCards=(${preAccountsCards})| FAIL")
//
//                currentState.value = DeliveryStatus.Error("CARDS: pre=$preAccountsIbans ac=${jarsAndAccounts.size} all=${initData.accounts} $e")
//            }
//
//
//        }
//
//        delay(100)
//        if (initData.jars != null) {
//
//            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|БАНОК на акаунті=${initData.jars.size}")
//
//            var preAccountsJars: List<String?>
//
//            try {
//
//                preAccountsJars = initData.jars.map { it.sendId ?: "" }
//
//                UtilsObject.all_jars_cards.onEachIndexed { _, (sendId, cardNumber) ->
//                    if (sendId in preAccountsJars) {
//                        val jar = initData.jars.find { it.sendId == sendId }
//                        val item = JarAccount(id = jar?.id ?: "", sendId= jar?.sendId ?: "", balance = jar?.balance ?: 0, title = jar?.title , currencyCode = jar?.currencyCode ?: 0, description = jar?.description, goal = jar?.goal, type = "xforsort", card = cardNumber)
//
//                        //Log.e("MESSI getInit jars forEach: ", cardNumber.toString())
//
////                        if (sendId in jarsAndAccounts.map { it.sendId }) {
////                            //Log.e("MESSI NOT in preAccounts: ", cardNumber.toString())
////                            item = JarAccount(id = cardNumber, balance = 0, sendId = "errorBank", currencyCode = 0, card = cardNumber)
////                            jarsAndAccountsForShow.add(item)
////                        } else {
//                        if (firstStart.value) {
//                            jarsAndAccounts.add(item)
//                        }
//                            jarsAndAccountsForShow.add(item)
//                     //   }
//
//                    } else {
//                        //Log.e("MESSI NOT getInit j in preAccounts: ", cardNumber.toString())
//                        val item = JarAccount(id = cardNumber, balance = 0, sendId = "errorBank", type = "zforsort", currencyCode = 0, card = cardNumber)
//
//                        jarsAndAccountsForShow.add(item)
//                    }
//                }
//
//            } catch (e: Exception) {
//                preAccountsJars = listOf(null)
//                LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|БАНКА ПОМИЛКА е=$e| FAIL")
//
//                currentState.value = DeliveryStatus.Error("JARS: pre=$preAccountsJars ac=${jarsAndAccounts.size} $e")
//            }
//
//        }
//
//
//
//        delay(100)
//
//        try {
//
//            jarsAndAccountsForShow.forEachIndexed { index, item ->
//                jarsAndAccountsForShow[index] = item.copy()
//            }
//        } catch (e: Exception) {
//            currentState.value = DeliveryStatus.Error("SHUFFLING: $e")
//
//            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|SHUFFLING ПОМИЛКА е=$e| FAIL")
//
//        }
//
//
//    }
//
//    private fun startTransactionListener() {
////
////        val stopIntent = Intent(this, MonobankService::class.java).apply {
////            action = "ACTION_SERVICE_STOP"
////            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
////        }
////        //val stopPendingIntent: PendingIntent = PendingIntent.getBroadcast(this, STOP_FOREGROUND_REMOVE, stopIntent, PendingIntent.FLAG_IMMUTABLE)
////
////        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)
//
//        val stopIntent = Intent(this, MonobankService::class.java).also {
//            it.action = Actions.STOP.toString()
//        }
//        val pendingIntent = PendingIntent.getService(this, 102, stopIntent, PendingIntent.FLAG_IMMUTABLE) as PendingIntent
//
//        notification = NotificationCompat.Builder(this, "monobank_server")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle("Приложение активно.")
//            .setContentText("Ожидание новых уведомлений.")
//            .setOngoing(false)
//            .setContentIntent(pendingIntent)
////            .addAction(
////                0, "STOP SERVICE", pendingIntent
////            )
//            .build()
//
//
//        //ContextCompat.startForegroundService(this, serviceIntent);
//        this@MonobankService.startForeground(1, notification)
//
//        try {
//        repeatJob = startAllAccounts()
//        repeatJob.start()
//
//        } catch (e: Exception) {
//            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|ЯКАСЬ ПОМИЛКА при startAllAccounts е=$e| FAIL")
//
//            currentState.value = DeliveryStatus.Error("ITERATE: ac=${jarsAndAccounts} $e")
//        }
//    }
//
//    private fun startAllAccounts(): Job {
//        return CoroutineScope(Dispatchers.Default).launch {
//            Log.e("MESSI jarsAndAccounts size: ", jarsAndAccounts.size.toString())
//            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КІЛЬКІСТЬ КАРТОК=${jarsAndAccounts.size}")
//
//            while (isActive) {
//                if (jarsAndAccounts.isNotEmpty()) {
//                    // Створюємо копію списку на момент початку ітерації, щоб уникнути конфліктів модифікації
//                    val currentAccounts = jarsAndAccounts.toList()
//
//                    for (account in currentAccounts) {
//                        if (!isActive) break
//
//                        try {
//                            // Викликаємо запит і захищаємо кожну окрему картку
//                            getAccountsData(account.id, currentAccounts)
//                        } catch (e: Exception) {
//                            Log.e("MESSI CARD ERROR", "Card ${account.id}: ${e.message}")
//                            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|ПОМИЛКА картки ${account.id}: $e| FAIL")
//                        }
//
//                        delay(5000L) // Пауза між картками
//                    }
//                } else {
//                    Log.e("MESSI", "jarsAndAccounts is empty!")
//                }
//
//                // Загальна пауза перед наступним колом
//                delay(60000L)
//            }
//        }
//    }
//
////    private fun startAllAccounts(): Job {
////
////        return CoroutineScope(Dispatchers.Default).launch {
////            var i = 0
////            Log.e("MESSI jarsAndAccounts size: ", jarsAndAccounts.size.toString())
////            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|КІЛЬКІСТЬ КАРТОК=${jarsAndAccounts.size}")
////
////                while (i < jarsAndAccounts.size) {
////
////                    getAccountsData(jarsAndAccounts[i].id, jarsAndAccounts)
////                    delay(60000L)
////                    //Log.e("MESSI 4: ", "after delay 2")
////                    i++
////                    if (i == jarsAndAccounts.size) {
////                        i = 0
////                    }
////                }
////
////
////        }
////
////    }
//
//
//    private suspend fun getAccountsData(accountId: String, listOfJarsAndAccounts: List<JarAccount>) {
//        //Log.e("MESSI startedTimestamp: ", UtilsObject.startedTimestamp.toString())
//        //Log.e("MESSI current: ", System.currentTimeMillis().toString())
//        val timelapse = System.currentTimeMillis() - UtilsObject.startedTimestamp
//        //Log.e("MESSI timelapse: ", timelapse.toString())
//
//        var fromTime = 0L
//        if (timelapse < 86400000) {
//            fromTime = (System.currentTimeMillis() - timelapse) / 1000
//            //Log.e("MESSI currentTimeMillis 1: ", time.toString())
//
//        } else {
//            fromTime = System.currentTimeMillis() / 1000 - 86400
//            //Log.e("MESSI currentTimeMillis 2: ", time.toString())
//        }
//
//        try {
//            val myResponse = Network.getTransactions(accountId, fromTime)
//            val lastTransaction = myResponse.body<List<MyTransaction>>()
//            Log.e("MESSI myResponse: $myResponse", myResponse.status.value.toString())
//            transactionStatus.value = "Status: ${myResponse.status}"
//            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|АКАУНТ дані status=${myResponse.status.value}|кількість транзакцій=${lastTransaction.size}")
//
//            val jarAccount: JarAccount? = listOfJarsAndAccounts.find { it.id == accountId }
//
//            lastTransaction.forEach { it ->
//                val exists = database.dao.getTransactionById(it.id)
//                //Log.e("MESSI EXIST DB: ", exists.toString())
//                LogStorage.addLog(this@MonobankService, "${getCurrentDate()}| чи ТРАНЗАКЦІЯ існує в DB $exists")
//
//                val calendar = Calendar.getInstance()
//                calendar.timeInMillis = it.time?.times(1000) ?: 0
//
//                Log.e("MESSI lastTransaction IT: ", it.toString())
//
//                if (!exists) {
//                    val postData = PostData(
//                        card = getCardNumber(myTransaction = it, jarAccount = jarAccount),
//                        iban = getIban(myTransaction = it, jarAccount = jarAccount),
//                        type = if (jarAccount?.type == "xforsort") "jar" else jarAccount?.type ?: "",
//                        link = getCardLink(myTransaction = it, jarAccount = jarAccount),
//                        bank = if (jarAccount?.goal != null) "https://send.monobank.ua/${jarAccount.sendId}" else "" ,
//                        transaction_id = it.id,
//                        time = getCorrectTime(calendar),
//                        date = getCorrectDay(calendar),
//                        description = it.description
//                            ?: "",//URLDecoder.decode(it.description, "UTF-8"),
//                        mcc = it.mcc ?: 0,
//                        originalMcc = it.originalMcc ?: 0,
//                        amount = it.amount ?: 0,
//                        operationAmount = it.operationAmount ?: 0,
//                        currencyCode = it.currencyCode ?: 0,
//                        commissionRate = it.commissionRate ?: 0,
//                        cashbackAmount = it.cashbackAmount ?: 0,
//                        balance = it.balance ?: 0,
//                        hold = it.hold ?: false,
//                        app = "Monobank",
//                        id = UtilsObject.device_id,
//                        user_id = UtilsObject.user_id
//                    )
//
//                    val responseTrans = Network.sendServer(postData)
//                    Log.e("MESSI postData responseTrans: ", responseTrans.bodyAsText())
//                    LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|ТРАНЗАКЦІЯ id=${it.amount} пішла на сервер|status=${responseTrans.status.value}| SUCCESS")
//
//                    delay(500)
//                    try {
//                        val responseTest = Network.sendTestData(postData)
//                        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|і на тестовий  сервер|status=${responseTest.status.value}| SUCCESS")
//
//                    } catch (e: Exception) {
//                        Log.e("MESSI ERROR WITH SERVER: ", "RESPONSE TEST")
//                        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|ERROR з тестовим сервером e=$e| FAIL")
//
//                    }
//
//                    Log.e("MESSI postData responseTest: ", it.toString())
//
//                    val uptrans = UpTrans(
//                        id = it.id,
//                        time = getCorrectTime(calendar),
//                        date = getCorrectDay(calendar),
//                        description = it.description ?: "",
//                        mcc = it.mcc,
//                        amount = (it.amount?.div(100.0)).toString(),
//                        originalMcc = it.originalMcc,
//                        operationAmount = it.operationAmount,
//                        currencyCode = it.currencyCode,
//                        commissionRate = it.commissionRate,
//                        cashbackAmount = it.cashbackAmount,
//                        balance = it.balance,
//                        hold = it.hold,
//                        comment = it.comment,
//                        receiptId = it.receiptId,
//                        invoiceId = it.invoiceId,
//                        counterEdrpou = it.counterEdrpou,
//                        counterIban = it.counterIban,
//                        counterName = it.counterName,
//                        type = if (jarAccount != null) "mono " + jarAccount.type else "",
//                        bankaTitle = jarAccount?.title,
//                        card = jarAccount?.card,
//                        iban = jarAccount?.iban,
//                        status = if (responseTrans.status.value == 200) true else false
//                        )
//
//
//                    delay(2000)
//                    if (responseTrans.status.value == 200) {
//                        database.dao.upsertTransaction(uptrans)
//                        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|ТРАНЗАКЦІЯ id=${it.amount} додалась в DB|status=${responseTrans.status.value}| SUCCESS")
//
//                    }
//                    listOfTransaction.add(uptrans)
//                }
//
//            }
//
//
//            delay(100)
//            if (!firstStart.value) {
//                getInit()
//            }
//            firstStart.value = false
//
//
//
//        } catch (e: Exception) {
//            LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|Хуйня з ТРАНЗАКЦІЄЮ е=$e| FAIL")
//
//            transactionStatus.value = "Status ERROR: $e"
//            Log.e("MESSI ERROR network: ", e.toString())
//        }
//    }
//
//
//    override fun onUnbind(intent: Intent?): Boolean {
//        return super.onUnbind(intent)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        repeatJob.cancel()
//        repeatJob2.cancel()
//        serverIsRunning.value = false
//        val current = System.currentTimeMillis()
//        //shared.edit().putLong("time", current).apply()
//        //shared.edit().putBoolean("suka", false).apply()
//        Log.e("MESSI onDestroy service: ", current.toString())
//        //stopForeground(STOP_FOREGROUND_REMOVE)
//        LogStorage.addLog(this@MonobankService, "${getCurrentDate()}|onDestroy()")
//
//        stopSelf()
//
//    }
//
//    enum class Actions {
//        START, STOP, RESTART
//    }
//
//    inner class MonobankServiceBinder : Binder() {
//        fun getService() = this@MonobankService
//    }
//
//}

@SuppressLint("SuspiciousIndentation")
private fun sendPing(): Job {
    return CoroutineScope(Dispatchers.IO).launch {
        while (isActive) {
            val json = PingData(UtilsObject.device_id, UtilsObject.user_id)
            try {
                val responsePing = Network.sendPingData(json)
                Log.e("MESSI response Ping: ", responsePing.status.value.toString())
            } catch (e: Exception) {
                Log.e("MESSI PING: ", e.toString())
            }
            // 60 секунд (або 10000L, якщо планували кожні 10 секунд)
            delay(10000L)
        }
    }
}

sealed class DeliveryStatus {
    class Loading : DeliveryStatus()
    class Success(val statusCode: String) : DeliveryStatus()
    class WrongXtoken : DeliveryStatus()
    class TooMany : DeliveryStatus()
    class Error(val error: String) : DeliveryStatus()
}















