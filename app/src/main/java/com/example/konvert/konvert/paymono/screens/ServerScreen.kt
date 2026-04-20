package com.example.konvert.konvert.paymono.screens

import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.konvert.konvert.paymono.DeliveryStatus
import com.example.konvert.konvert.paymono.MonobankService
import com.example.konvert.konvert.paymono.R
import com.example.konvert.konvert.paymono.UtilsObject
import com.example.konvert.konvert.paymono.data.JarAccount
import com.example.konvert.konvert.paymono.data.UpTrans
import com.example.konvert.konvert.paymono.database.LogStorage
import com.example.konvert.konvert.paymono.getCurrentDate
import com.example.konvert.konvert.paymono.noRippleClickable
import com.example.konvert.konvert.paymono.screens.custom_components.LogsDialog
import com.example.konvert.konvert.paymono.screens.custom_components.ServerBankaDesign
import com.example.konvert.konvert.paymono.screens.custom_components.ServerBankaDesignERROR
import com.example.konvert.konvert.paymono.screens.custom_components.ServerCardDesign
import com.example.konvert.konvert.paymono.screens.custom_components.ServerCardDesignERROR
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun ServerScreen(viewModel: StateViewModel = viewModel(), goNext: () -> Unit, monobankService: MonobankService, goChangeData: () -> Unit, goToJournal: () -> Unit) {

    val context = LocalContext.current
    //val data = UtilsObject.account_numbers

    //Log.e("MESSI UI LIST: ", "123")
    //Log.e("MESSI UI LIST: ", monobankService.jarsAndAccountsForShow.toList().toString())

    var lastClickTime: Long = 0
    var clickCount = 0
    val sevenClickThreshold = 1000

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val listOfLogs = remember {
        mutableStateOf<List<String>>(emptyList())
    }
    val logsDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            launch {
                while(true) {
                    delay(1.seconds)
                    //monobankService.jarsAndAccountsForShow.
                }
            }
        }

    }

    if (logsDialogState.value) {
        LogsDialog(logs = listOfLogs, dialogState = logsDialogState)
    }

    BackHandler {

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MyBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {


        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(30.dp, 50.dp, 30.dp, 0.dp)) {

            IconButton(onClick = { goNext() }) {
                Icon(
                    painter = painterResource(id = R.drawable.pngegg),
                    contentDescription = "back button",
                    modifier = Modifier.size(35.dp),
                    tint = Color.White)
            }

            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "logo",
                modifier = Modifier
                    .size(45.dp)
                    .noRippleClickable {
                        val currentTime = SystemClock.elapsedRealtime()

                        if (currentTime - lastClickTime < sevenClickThreshold) {
                            clickCount++
                        } else {
                            clickCount = 1 // Якщо пауза між кліками занадто довга, скидаємо лічильник
                        }

                        lastClickTime = currentTime

                        if (clickCount == 7) {
                            if (logsDialogState.value == false) {

                                val logs = LogStorage.getLogs(context)
                                listOfLogs.value = logs
                                logsDialogState.value = true
                            }
                            clickCount = 0 // Скидаємо лічильник після потрійного кліку
                        }
                    }

            )

        }

        when (monobankService.currentState.value) {
            is DeliveryStatus.Success -> {

                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier
                    .weight(
                        if (monobankService.jarsAndAccountsForShow.size < 4) {
                            3f
                        } else if (monobankService.jarsAndAccountsForShow.size == 4) {
                            5f
                        } else 6f
                    )
                    .padding(20.dp, 0.dp)) {


                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Подключённые реквизиты", color = MyMilkText, fontSize = 14.sp, modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold)
                        if (monobankService.cardLoaderStatus.value ) {
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(30.dp),
                                    color = MyMilkText
                                )
                                Text(
                                    text = if (viewModel.tick in 1..59) "${viewModel.tick}" else "",
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    fontFamily = fontLight,
                                    modifier = Modifier.padding(0.dp),
                                    color = MyMilkText,
                                )

                            }
                        }
                    }

                    LazyColumn(modifier = Modifier
                        .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                        .fillMaxSize()) {
                        items(monobankService.jarsAndAccountsForShow.sortedBy { it.type }
                        ) { accountOrJar ->
                            //Log.e("MESSI LAZY COLUMN: ", viewModel.tick.toString())
                           if (accountOrJar.sendId == "errorCard") {
                                ServerCardDesignERROR(accountOrJar = accountOrJar)
                            } else if (accountOrJar.sendId == "errorBank") {
                                ServerBankaDesignERROR(accountOrJar = accountOrJar)
                            } else if (accountOrJar.currencyCode != 980 && accountOrJar.goal == null) {
                                ServerCardDesignERROR(accountOrJar = accountOrJar)
                            } else if (accountOrJar.title == null) {
                                ServerCardDesign(accountOrJar = accountOrJar, loader = monobankService.cardLoaderStatus, delay = monobankService.delayState)
                            } else {
                                ServerBankaDesign(accountOrJar = accountOrJar, loader = monobankService.cardLoaderStatus, delay = monobankService.delayState)
                            }
                        }
                    }

                }
            }

            is DeliveryStatus.WrongXtoken -> {
                Text(text = "Wrong X-Token", color = Color.White, fontSize = 18.sp)
            }

            is DeliveryStatus.TooMany -> {
                Text(text = "Too Many request. Wait 1 minute", color = Color.White, fontSize = 18.sp)
            }

            is DeliveryStatus.Error -> {
                Text(text = "ERROR: \n ${(monobankService.currentState.value as DeliveryStatus.Error).error}", color = Color.White, fontSize = 18.sp)
            }

            else -> {
                //LogStorage.addLog(context, "${getCurrentDate()}| SERVER Something Wrong ${(monobankService.currentState.value as DeliveryStatus.Success).statusCode}")

                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = MyMilkText
                    )
                    Text(
                        text = if (viewModel.tick in 1..59) "${viewModel.tick}" else "",
                        fontSize = 10.sp,
                        maxLines = 1, fontFamily = fontLight,
                        modifier = Modifier.padding(0.dp),
                        color = MyMilkText
                    )
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .weight(2f)
            .padding(20.dp, 40.dp)
            .clickable {
                goChangeData()

                viewModel.checkAllData(
                    _token = viewModel.tokenValue,
                    _black_card = viewModel.blackCardValue,
                    _white_card = viewModel.whiteCardValue,
                    _jar_1 = viewModel.jarCardLink_1,
                    _jar_2 = viewModel.jarCardLink_2,
                    _jar_3 = viewModel.jarCardLink_3,
                    _jar_card_1 = viewModel.jarCardNumber_1,
                    _jar_card_2 = viewModel.jarCardNumber_2,
                    _jar_card_3 = viewModel.jarCardNumber_3,
                    _fop_card = viewModel.FOPCardValue,
                    _black_card_IBAN = viewModel.blackCardValueIBAN,
                    _white_card_IBAN = viewModel.whiteCardValueIBAN,
                    _fop_card_IBAN = viewModel.fopCardValueIBAN,
                    _black_card_link = viewModel.blackCardValueLink,
                    _white_card_link = viewModel.whiteCardValueLink,
                )
                Log.e(
                    "MESSI goChangeData ",
                    (UtilsObject.all_cards.size + UtilsObject.all_jars_cards.size).toString()
                )

            },
            verticalArrangement = Arrangement.Center) {

            Column(modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                .padding(0.dp)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {

                Image(painter = painterResource(id = R.drawable.user), contentDescription = "user icon",
                    modifier = Modifier.size(50.dp))
                if (monobankService.jarsAndAccountsForShow.size < 5) {
                    Text(text = "Изменить данные аккаунта", color = MyMilkText, fontSize = 14.sp, modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp), fontWeight = FontWeight.Bold)
                }
            }

        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .weight(2f)
            .clickable {
                goToJournal()
            }
            .padding(20.dp, 0.dp, 20.dp, 80.dp),
            verticalArrangement = Arrangement.Center) {

            Column(modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                .padding(0.dp)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {

                Image(painter = painterResource(id = R.drawable.journal), contentDescription = "journal transaction",
                    modifier = Modifier.size(50.dp))
                if (monobankService.jarsAndAccountsForShow.size < 5) {
                    Text(text = "Системный журнал", color = MyMilkText, fontSize = 14.sp, modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp), fontWeight = FontWeight.Bold)
                }

            }

        }


    }
}


@Preview
@Composable
fun ServerPreview() {

    val service = MonobankService()

    service.jarsAndAccountsForShow.add(JarAccount("5375411422309984", "errorBank", 123123,  23432423, "zforsort", 3434, "asdasd", maskedPan = null, "asdasd", 234234, "asdasd", "asdasd", "5375411422309984"))
    service.jarsAndAccountsForShow.add(JarAccount(id = "234234234", balance = 0, sendId = "errorCard", currencyCode = 0, card = "333344444555556666", type = "yforsort"))
    service.jarsAndAccountsForShow.add(JarAccount("asdasd", "", 5555,  23432423, "black", 980, "asdasd", maskedPan = null, "asdasd", null, "asdasd", title = null, "5375411422309984"))
    service.jarsAndAccountsForShow.add(JarAccount("asdasd", "", 4444,  23432423, "white", 980, "asdasd", maskedPan = null, "asdasd", null, "asdasd", title = null, "5375411422309984"))
    service.jarsAndAccountsForShow.add(JarAccount("asdasd", "asdasd", 123123,  23432423, "xforsort", 980, "asdasd", maskedPan = null, "asdasd", 234234, "asdasd", "На API", "5375411422309984"))



    service.currentState.value = DeliveryStatus.Success("sadsad")
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "1554466347", "16.09.2024","asd", 123, 123, "123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "1554466347","16.09.2024", "2asd", 123, 123, "-123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "1554466347", "16.09.2024","asd", 123, 123, "123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "1554466347","16.09.2024", "asd", 123, 123, "123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "1554466347","16.09.2024", "asd", 123, 123, "123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "1554466347","16.09.2024", "asd", 123, 123, "123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "1554466347","16.09.2024", "asd", 123, 123, "123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "1554466347", "16.09.2024","asd", 123, 123, "123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "1554466347","16.09.2024", "asd", 123, 123, "123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "1554466347","16.09.2024", "asd", 123, 123, "123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "1554466347","16.09.2024", "ashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgd", 123, 123, "123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))

    //service.currentState.value = DeliveryStatus.Loading()
    service.cardLoaderStatus.value = true

    ServerScreen( viewModel(), goNext = {}, service, {}, { })
}