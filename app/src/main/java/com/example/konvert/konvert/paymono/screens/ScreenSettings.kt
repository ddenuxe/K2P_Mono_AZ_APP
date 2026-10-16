package com.example.konvert.konvert.paymono.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.konvert.konvert.paymono.BuildConfig
import com.example.konvert.konvert.paymono.MonobankService
import com.example.konvert.konvert.paymono.R
import com.example.konvert.konvert.paymono.data.UpTrans
import com.example.konvert.konvert.paymono.database.LogStorage
import com.example.konvert.konvert.paymono.getCorrectDay
import com.example.konvert.konvert.paymono.getCorrectTime
import com.example.konvert.konvert.paymono.screens.custom_components.BlackCardTransaction
import com.example.konvert.konvert.paymono.screens.custom_components.LogsDialog
import com.example.konvert.konvert.paymono.screens.custom_components.SimpleTransaction
import com.example.konvert.konvert.paymono.screens.custom_components.TextFieldValidationCard
import com.example.konvert.konvert.paymono.screens.custom_components.WhiteCardTransaction
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyLightGrey
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight
import com.example.konvert.konvert.paymono.ui.theme.newBlue
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.time.Duration.Companion.seconds


@Composable
fun ScreenSettings(deleteAll: () -> Unit,  goBack: () -> Unit, viewModel: StateViewModel = viewModel() , goToStart: () -> Unit, monobankService: MonobankService) {

    val localContext = LocalContext.current
    val shared = localContext.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

    val buttonState = remember { mutableStateOf(false) }

    val showLoader = remember { mutableStateOf(false) }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val context = LocalContext.current
    val listOfLogs = remember {
        mutableStateOf<List<String>>(emptyList())
    }
    val logsDialogState = remember { mutableStateOf(false) }

    if (logsDialogState.value) {
        LogsDialog(logs = listOfLogs, dialogState = logsDialogState)
    }

    if (buttonState.value) {
        AlertDialog(modifier = Modifier, containerColor = MyBackground, onDismissRequest = {
            viewModel.setJarCardNumerValue_1("")
            viewModel.setJarCardLinkValue_1("")
            buttonState.value = false }, text = {
            Column(modifier = Modifier.fillMaxWidth()) {

//                Text("mono-банка №1", color = MyMilkText, style = MaterialTheme.typography.bodyMedium, fontSize = 22.sp, modifier = Modifier
//                    .padding(0.dp, 0.dp, 0.dp, 10.dp)
//                    .fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = fontLight)

                    Text("Вы действительно хотите сбросить настройки приложения?", color = MyMilkText, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = fontLight)


 } },
            confirmButton = {
                Button(colors = ButtonColors(containerColor = MyMilkText, contentColor = MyBackground, disabledContentColor = Color.DarkGray, disabledContainerColor = newBlue),
                    onClick = {
                        viewModel.setTokenName("")
                        viewModel.setUserId("")
                        viewModel.setDeviceId("")
                        viewModel.setWhiteCards("")
                        viewModel.setBlackCards("")
                        viewModel.setJarCardNumerValue_1("")
                        viewModel.setJarCardNumerValue_2("")
                        viewModel.setJarCardNumerValue_3("")
                        viewModel.setJarCardLinkValue_1("")
                        viewModel.setJarCardLinkValue_2("")
                        viewModel.setJarCardLinkValue_3("")
                        shared.edit().putString("token", "").apply()
                        shared.edit().putString("device_id", "").apply()
                        shared.edit().putString("user_id", "").apply()
                        shared.edit().putString("black_card", "").apply()
                        shared.edit().putString("white_card", "").apply()
                        shared.edit().putString("server", "").apply()
                        //shared.edit().putString("server_ping", "http://185.80.234.53/AZ/Ping.ashx").apply()
                        //////////////////////////////////////////////
                        shared.edit().putString("jar_link_1", "").apply()
                        shared.edit().putString("jar_link_2", "").apply()
                        shared.edit().putString("jar_link_3", "").apply()
                        ///////////////////////////////////////
                        shared.edit().putString("jar_card_1", "").apply()
                        shared.edit().putString("jar_card_2", "").apply()
                        shared.edit().putString("jar_card_3", "").apply()
                        /////////////////////////////////////////////////
                        //shared.edit().putBoolean("stopped", true).apply()
                        deleteAll()

                        Thread {
                            val temp = try {
                                val info = AdvertisingIdClient.getAdvertisingIdInfo(localContext)
                                info.id.toString()
                            } catch (e: Exception) {
                                ""
                            }
                            viewModel.setDeviceId(temp)

                        }.start()
                        goToStart()
//                        Intent(localContext, MonobankService::class.java).also {
//                            it.action = MonobankService.Actions.STOP.toString()
//                            //localContext.startService(it)
//                            localContext.stopService(it)
//                        }
//                        monobankService.unbindService()
//                        monobankService.onDestroy()

//                        CoroutineScope(Dispatchers.Main).launch {
//                            showLoader.value = true
//                            delay(5000)
//                            launch(Dispatchers.Main) {
//                                goToStart()
//                            }
//                        }


                        buttonState.value = false
                    } )
                {
                    Text("Сбросить", fontFamily = fontLight)
                }
            },
            dismissButton = {
                Button(colors = ButtonColors(containerColor = MyMilkText, contentColor = MyBackground, disabledContentColor = Color.Magenta, disabledContainerColor = Color.DarkGray),
                    onClick = {
                        viewModel.setJarCardNumerValue_1("")
                        viewModel.setJarCardLinkValue_1("")
                        buttonState.value = false
                    })
                {
                    Text("Отмена", fontFamily = fontLight)
                }
            }
        )
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(MyBackground)
        .padding(10.dp, 10.dp, 10.dp, 50.dp).imePadding(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 20.dp)) {

            IconButton(onClick = { goBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.circle_left),
                    contentDescription = "back button",
                    modifier = Modifier.size(35.dp),
                    tint = MyMilkText
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "О приложении",
                    textAlign = TextAlign.Center,
                    color = MyMilkText,
                    modifier = Modifier,
                    fontFamily = fontLight,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Версия: ${BuildConfig.VERSION_NAME}",
                    textAlign = TextAlign.Center,
                    color = MyMilkText.copy(alpha = 0.5f),
                    modifier = Modifier,
                    fontFamily = fontLight,
                    fontSize = 12.sp
                )
            }


            Image(
                painter = painterResource(id = R.drawable.logo_upd),
                contentDescription = "logo",
                modifier = Modifier.size(45.dp)
            )

        }

        if (showLoader.value) {
            CircularProgressIndicator(
                modifier = Modifier.size(140.dp),
                color = MyMilkText,
                strokeWidth = 10.dp
            )
        } else {

            Text(
                text = "Согласно ограничениям Monobank, приложение может отправлять запросы только раз в минуту. Поэтому, если баланс на вашей карте не обновился моментально, просто подождите.\n" +
                        "\n" +
                        "Все подключённые реквизиты обрабатываются в порядке очереди. Чем меньше карт и банок подключено, тем чаще приложение может с ними взаимодействовать.\n" +
                        "\n" +
                        "Таким образом, при максимальной загрузке приложения (2 карты и 3 банки) транзакции будут отображаться в журнале каждые 5 минут (за 1 минуту — 1 реквизит).",
                textAlign = TextAlign.Center,
                color = MyMilkText,
                modifier = Modifier,
                fontFamily = fontLight,
                fontSize = 16.sp
            )
        }


        Column() {

//            OutlinedButton(onClick = {
//
//
//                if (logsDialogState.value == false) {
//
//                    val logs = LogStorage.getLogs(context)
//                    listOfLogs.value = logs
//                    logsDialogState.value = true
//                }
//
//            }, border = BorderStroke(1.dp, MyMilkText), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth().padding(10.dp, 0.dp)) {
//
//                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
//                    Text(
//                        text = "Экспорт",
//                        fontSize = 16.sp,
//                        modifier = Modifier
//                            .padding(horizontal = 0.dp).fillMaxWidth(),
//                        textAlign = TextAlign.Center,
//                        color = MyMilkText,
//                        maxLines = 1
//                    )
//
//                }
//            }

            OutlinedButton(onClick = {


                buttonState.value = true

                //Toast.makeText(localContext, "Success!", Toast.LENGTH_LONG).show()


            }, border = BorderStroke(1.dp, MyMilkText), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth().padding(10.dp, 10.dp)) {

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Сбросить настройки",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(horizontal = 0.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MyMilkText,
                        maxLines = 1
                    )

                }
            }
        }


    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun ScreenSettingsPreview() {
    val service = MonobankService()

    ScreenSettings( deleteAll = { }, goBack = {} , goToStart =  {}, monobankService = service)
}