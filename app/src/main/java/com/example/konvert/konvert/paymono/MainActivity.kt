package com.example.konvert.konvert.paymono

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.konvert.konvert.paymono.data.Card
import com.example.konvert.konvert.paymono.database.LogStorage
import com.example.konvert.konvert.paymono.database.TransactionDatabase
import com.example.konvert.konvert.paymono.screens.Screen1
import com.example.konvert.konvert.paymono.screens.ScreenJournal
import com.example.konvert.konvert.paymono.screens.ScreenSettings
import com.example.konvert.konvert.paymono.screens.ScreenSummary
import com.example.konvert.konvert.paymono.screens.ServerScreen
import com.example.konvert.konvert.paymono.screens.StateViewModel
import com.example.konvert.konvert.paymono.screens.screen_banks.ScreenBanks
import com.example.konvert.konvert.paymono.screens.screen_cards.ScreenCards
import com.example.konvert.konvert.paymono.screens.screen_device_id.ScreenDeviceId
import com.example.konvert.konvert.paymono.screens.screen_token.ScreenToken
import com.example.konvert.konvert.paymono.screens.screen_user_id.ScreenUserId
import com.example.konvert.konvert.paymono.ui.theme.Konvert2payMonoTheme
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.launch
import org.slf4j.helpers.Util

class MainActivity : ComponentActivity() {

//    lateinit var database: TransactionDatabase
//    var listOfTransaction = mutableStateListOf<UpTrans>()
//        private set
    private lateinit var database: TransactionDatabase

    lateinit var shared: SharedPreferences

    private var isBound by mutableStateOf(false)
    private var isServerRun by mutableStateOf(false)
    private lateinit var monobankService: MonobankService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder = service as MonobankService.MonobankServiceBinder
            monobankService = binder.getService()
            isBound = true
            //Log.e("MESSI start: ", isServerRun.toString())
            isServerRun = monobankService.serverIsRunning.value
            //Log.e("MESSI after: ", isServerRun.toString())
            //LogStorage.addLog(this@MainActivity, "${getCurrentDate()}| monobankService=${monobankService}")

            //LogStorage.addLog(this@MainActivity, "${getCurrentDate()}| isBound=$isBound")
            //LogStorage.addLog(this@MainActivity, "${getCurrentDate()}| isServerRun=$isServerRun")
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MonobankService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
        isServerRun = false
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = TransactionDatabase.getInstance(this)
        shared = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        LogStorage.addLog(this, "${getCurrentDate()}| СТАРТ")



        val myViewModel by viewModels<StateViewModel>()

        val token_monobank = shared.getString("token", "").toString()
        val server_name = shared.getString("server", "").toString()
        //val server_ping = shared.getString("server_ping", "http://185.80.234.53/AZ/Ping.ashx").toString()

        val timeStamp = shared.getLong("time", 0L)
        val current = System.currentTimeMillis()
        if (timeStamp == 0L) {
            shared.edit().putLong("time", current).apply()
            UtilsObject.startedTimestamp = current
        } else {
            UtilsObject.startedTimestamp = timeStamp
        }

        //val stopped = shared.getBoolean("stopped", false)

        val device_id = shared.getString("device_id", "").toString()
        val user_id = shared.getString("user_id", "").toString()


        /////////////////////////////////////////////////////////


        val black_card = shared.getString("black_card", "").toString()
        val white_card = shared.getString("white_card", "").toString()
        val fop_card = shared.getString("fop_card", "").toString()

        val black_card_iban = shared.getString("black_card_iban", "").toString()
        val white_card_iban = shared.getString("white_card_iban", "").toString()
        val fop_card_iban = shared.getString("fop_card_iban", "").toString()

        val black_card_link = shared.getString("black_card_link", "").toString()
        val white_card_link = shared.getString("white_card_link", "").toString()


        ///////////////////////////////////////
        val jar_card_1 = shared.getString("jar_card_1", "").toString()
        val jar_card_2 = shared.getString("jar_card_2", "").toString()
        val jar_card_3 = shared.getString("jar_card_3", "").toString()


        val jar_link_1 = shared.getString("jar_link_1", "").toString()
        val jar_link_2 = shared.getString("jar_link_2", "").toString()
        val jar_link_3 = shared.getString("jar_link_3", "").toString()


        //////////////////////////////////////////////////////
        UtilsObject.suka = shared.getBoolean("suka", false)



        //Log.e("MESSI UtilsObject.suka: ", UtilsObject.suka.toString())
        myViewModel.init(token_monobank, server_name, black_card, white_card, jar_link_1, jar_link_2, jar_link_3, device_id, user_id, jar_card_1, jar_card_2, jar_card_3 ,fop_card,
            black_card_iban, white_card_iban, fop_card_iban, black_card_link, white_card_link
        )
        //enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }


//        if (device_id == "") {
//            Thread {
//                val temp = try {
//                    val info = AdvertisingIdClient.getAdvertisingIdInfo(this)
//                    info.id.toString()
//                } catch (e: Exception) {
//                    ""
//                }
//                myViewModel.setDeviceId(temp)
//
//            }.start()
//        }



        fun deleteAllTransactions() {
            lifecycleScope.launch {
                database.dao.deleteAll()
            }
            monobankService.listOfTransaction.clear()
            monobankService.listOfTransaction.forEachIndexed { index, item ->
                monobankService.listOfTransaction[index] = monobankService.listOfTransaction[index].copy()
            }
            unbindService(connection)
            //monobankService.onDestroy()
        }


        setContent {
            Konvert2payMonoTheme {

                val navController = rememberNavController()

                if (token_monobank != "" && (black_card != "" || white_card != "" || fop_card != "" || jar_card_1 != "" || jar_card_2 != "" || jar_card_3 != "")) {
                    UtilsObject.server_name = server_name
                    //UtilsObject.server_ping = server_ping
                    UtilsObject.token_monobank = token_monobank
                    UtilsObject.device_id = myViewModel.deviceValue
                    UtilsObject.user_id = user_id

                    if (jar_link_1 != "") {

                        UtilsObject.all_jars_cards.put(
                            jar_link_1.substringAfter("ua/")
                                .filterNot { it.isWhitespace() },
                                    jar_card_1.filterNot { it.isWhitespace() })
                    }
                    if (jar_link_2 != "") {

                        UtilsObject.all_jars_cards.put(
                            jar_link_2.substringAfter("ua/")
                                .filterNot { it.isWhitespace() },
                                    jar_card_2.filterNot { it.isWhitespace() })
                    }
                    if (jar_link_3 != "") {

                        UtilsObject.all_jars_cards.put(
                            jar_link_3.substringAfter("ua/")
                                .filterNot { it.isWhitespace() },
                            jar_card_3.filterNot { it.isWhitespace() })
                    }

                    if (white_card_iban != "" || white_card != "") UtilsObject.all_cards.add(Card("white", white_card, white_card_iban, white_card_link))
                    if (black_card_iban != "" || black_card != "") UtilsObject.all_cards.add(Card("black", black_card, black_card_iban, black_card_link))
                    if (fop_card_iban != "" || fop_card != "") UtilsObject.all_cards.add(Card(UtilsObject.fop_carta, fop_card, fop_card_iban))

//                    if (white_card_iban != "") UtilsObject.all_cards.put("white", white_card_iban.filterNot { it.isWhitespace() })
//                    if (black_card_iban != "") UtilsObject.all_cards.put("black", black_card_iban.filterNot { it.isWhitespace() })
//                    if (fop_card_iban != "") UtilsObject.all_cards.put(UtilsObject.fop_carta, fop_card_iban.filterNot { it.isWhitespace() })
//                    //if (fop_card != "") UtilsObject.fopCardNumber = fop_card.filterNot { it.isWhitespace() }
//
//
//                    if (white_card != "") UtilsObject.whiteCardNumber = white_card.filterNot { it.isWhitespace() }
//                    if (black_card != "") UtilsObject.blackCardNumber = black_card.filterNot { it.isWhitespace() }
//                    if (fop_card != "") UtilsObject.fopCardNumber = fop_card.filterNot { it.isWhitespace() }

//                    if (black_card_link != "") UtilsObject.blackCardLink = black_card_link.filterNot { it.isWhitespace() }
//                    if (white_card_link != "") UtilsObject.whiteCardLink = white_card_link.filterNot { it.isWhitespace() }

                    Intent(this, MonobankService::class.java).also {
                            it.action = MonobankService.Actions.START.toString()
                            this.startService(it)
                        }
                    }


                val time = shared.getLong("delay_time", 0L)
                val delay = 60000 - (System.currentTimeMillis() - time)
                myViewModel.startTimer(delay)


                Surface(modifier = Modifier.fillMaxSize()) {
                    //Log.e("MESSI NavHost: ", isServerRun.toString())
                    NavHost(
                        navController = navController,
                        startDestination = if (!isServerRun) "initial" else "server",
                        //startDestination = "initial",
                        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(100)) },
                        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(100)) },
                        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(100)) },
                        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(100)) },
                    ) {
                        composable(route = "initial") {
                            Screen1 { navController.navigate("token") }
                        }
                        composable(route = "token") {
                            ScreenToken ( { navController.navigate("user") }, viewModel = myViewModel)
                        }
                        composable(route = "user") {
                            ScreenUserId ( goNext =  { navController.navigate("device") }, goBack = { navController.popBackStack()}, viewModel = myViewModel)
                        }
                        composable(route = "device") {
                            ScreenDeviceId ( goNext =  { navController.navigate("cards") }, goBack = { navController.popBackStack()}, viewModel = myViewModel)
                        }
                        composable(route = "cards") {
                            ScreenCards ( goNext =  { navController.navigate("banks") }, goBack = { navController.popBackStack()}, viewModel = myViewModel)
                        }
                        composable(route = "banks") {
                            ScreenBanks ( goNext =  { navController.navigate("summary/Подтверждение данных") }, goBack = { navController.popBackStack()}, viewModel = myViewModel)
                        }
                        composable(route = "summary/{header}") { backStackEntry ->
                            val header: String? = backStackEntry.arguments?.getString("header")
                            ScreenSummary (header = header,  goNext =  { navController.navigate("server") }, goBack = { navController.popBackStack()}, viewModel = myViewModel)
                        }

                        composable(route = "server") {
                            ServerScreen(viewModel = myViewModel, goNext =  { navController.navigate("settings") }, monobankService = monobankService,
                                goChangeData = {
                                    navController.navigate("summary/Изменение данных")
                                               },
                                goToJournal = {
                                    navController.navigate("journal") })
                        }
                        composable(route = "journal") {
                            ScreenJournal( goBack = { navController.popBackStack()}, monobankService = monobankService)
                        }
                        composable(route = "settings") {
                            ScreenSettings (::deleteAllTransactions, goBack = { navController.popBackStack()}, viewModel = myViewModel, goToStart =  { navController.navigate("initial") } ,
                                monobankService = monobankService)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        shared.edit().putBoolean("suka", true).apply()
        LogStorage.addLog(this, "${getCurrentDate()}|MainActivity onDestroy()")

        Intent(this, MonobankService::class.java).also {
            it.action = MonobankService.Actions.STOP.toString()
            this.startService(it)
        }
        Log.e("MESSI onDestroy: ", "timeStamp.value.toString()")
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Konvert2payMonoTheme {
        //Greeting("Android")
    }
}