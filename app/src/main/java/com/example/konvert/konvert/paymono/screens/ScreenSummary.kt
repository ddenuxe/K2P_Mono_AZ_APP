package com.example.konvert.konvert.paymono.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.konvert.konvert.paymono.MonobankService
import com.example.konvert.konvert.paymono.R
import com.example.konvert.konvert.paymono.UtilsObject
import com.example.konvert.konvert.paymono.noRippleClickable
import com.example.konvert.konvert.paymono.screens.custom_components.GradientButton
import com.example.konvert.konvert.paymono.screens.custom_components.TextFieldValidationCard
import com.example.konvert.konvert.paymono.screens.screen_cards.BlackCardAlarmDialog
import com.example.konvert.konvert.paymono.screens.screen_cards.FopCardAlarmDialog
import com.example.konvert.konvert.paymono.screens.screen_cards.WhiteCardAlarmDialog
import com.example.konvert.konvert.paymono.ui.theme.Konvert2payMonoTheme
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyLightGrey
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight
import com.example.konvert.konvert.paymono.ui.theme.gradient1
import com.example.konvert.konvert.paymono.ui.theme.gradient2
import com.example.konvert.konvert.paymono.ui.theme.gradientGreyCard
import com.example.konvert.konvert.paymono.ui.theme.newBlue
import com.example.konvert.konvert.paymono.ui.theme.textGrey
import kotlinx.coroutines.launch
import my.nanihadesuka.compose.ColumnScrollbar
import my.nanihadesuka.compose.ScrollbarLayoutSide
import my.nanihadesuka.compose.ScrollbarSelectionActionable
import my.nanihadesuka.compose.ScrollbarSelectionMode
import my.nanihadesuka.compose.ScrollbarSettings
import com.example.konvert.konvert.paymono.data.Card
import com.example.konvert.konvert.paymono.database.LogStorage
import com.example.konvert.konvert.paymono.getCurrentDate
import com.example.konvert.konvert.paymono.ui.theme.gradientFopCard


@Composable
fun ScreenSummary(
    goNext: () -> Unit,
    goBack: () -> Unit,
    viewModel: StateViewModel = viewModel(),
    header: String?
) {

    BackHandler {

    }

    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    val localContext = LocalContext.current
    val shared = localContext.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

    val jarState_1 = remember { mutableStateOf(false) }
    val jarState_2 = remember { mutableStateOf(false) }
    val jarState_3 = remember { mutableStateOf(false) }

    //val cardValue_1 = remember { mutableStateOf(viewModel.jar_card_1_temp) }
    //val cardValue_2 = remember { mutableStateOf(viewModel.jar_card_2_temp) }
    //val cardValue_3 = remember { mutableStateOf(viewModel.jar_card_3_temp) }

    //val linkValue_1 = remember { mutableStateOf(viewModel.jar_1_link_temp) }
    //val linkValue_2 = remember { mutableStateOf(viewModel.jar_2_link_temp) }
    //val linkValue_3 = remember { mutableStateOf(viewModel.jar_3_link_temp) }

    val showBlackCard = remember { mutableIntStateOf(if (viewModel.blackCardValueIBAN.isNotEmpty()) 2 else 0) }
    val showWhiteCard = remember { mutableIntStateOf(if (viewModel.whiteCardValueIBAN.isNotEmpty()) 2 else 0) }

    val showFOPCard = remember { mutableIntStateOf(if (viewModel.fopCardValueIBAN.isNotEmpty()) 2 else 0) }

    val cardState_White = remember { mutableStateOf(false) }
    val cardState_Black = remember { mutableStateOf(false) }
    val cardState_FOP = remember { mutableStateOf(false) }

    val listState = rememberScrollState()

    val token = remember { mutableStateOf(viewModel.tokenValue) }
    val whiteCard = remember { mutableStateOf(viewModel.whiteCardValue) }
    val blackCard = remember { mutableStateOf(viewModel.blackCardValue) }
    val jarLink1Viewmodel = remember { mutableStateOf(viewModel.jarCardLink_1) }
    val jarLink2Viewmodel = remember { mutableStateOf(viewModel.jarCardLink_2) }
    val jarLink3Viewmodel = remember { mutableStateOf(viewModel.jarCardLink_3) }
    val jarCard1Viewmodel = remember { mutableStateOf(viewModel.jarCardNumber_1) }
    val jarCard2Viewmodel = remember { mutableStateOf(viewModel.jarCardNumber_2) }
    val jarCard3Viewmodel = remember { mutableStateOf(viewModel.jarCardNumber_3) }
    val serverUrl = remember { mutableStateOf(viewModel.serverValue) }
    //val serverPing = remember { mutableStateOf(viewModel.serverPingValue) }
    val deviceId = remember { mutableStateOf(viewModel.deviceValue) }
    val userId = remember { mutableStateOf(viewModel.userValue) }




    //val numberOfCards = header?.substringAfter("&")?.toInt()

    if (jarState_1.value) {
        AlertDialog(modifier = Modifier, containerColor = MyBackground, onDismissRequest = {
            viewModel.setJarCardNumerValue_1(viewModel.jar_card_1_temp)
            viewModel.setJarCardLinkValue_1(viewModel.jar_1_link_temp)
            jarState_1.value = false }, text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                Text("mono-банка №1", color = MyMilkText, style = MaterialTheme.typography.bodyMedium, fontSize = 22.sp, modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    .fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = fontLight)

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MyLightGrey))
                Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                    Text("Укажите номер банки", color = MyMilkText, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = fontLight)
                    TextFieldValidationCard(
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = viewModel.jarCardNumber_1,
                        onChange = viewModel::setJarCardNumerValue_1,
                        placeholder = "", modifier = Modifier.fillMaxWidth(),
                        onClear = { viewModel.setJarCardNumerValue_1("") }
                    )
                }
                Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                    Text("Укажите ссылку на банку", color = MyMilkText, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = fontLight)
                    TextFieldValidationCard(value = viewModel.jarCardLink_1, onChange = viewModel::setJarCardLinkValue_1, placeholder = "", modifier = Modifier, onClear = { viewModel.setJarCardLinkValue_1("") })
                } } },
            confirmButton = {
                Button(colors = ButtonColors(containerColor = MyMilkText, contentColor = MyBackground, disabledContentColor = Color.DarkGray, disabledContainerColor = newBlue),
                    onClick = {
                        //viewModel.setJarCardNumerValue_1(cardValue_1.value)
                        //viewModel.setJarCardLinkValue_1(linkValue_1.value)
                        viewModel.jar_card_1_temp = viewModel.jarCardNumber_1
                        viewModel.jar_1_link_temp = viewModel.jarCardLink_1
                        jarState_1.value = false
                    }, enabled = if(viewModel.jarCardNumber_1 != "" && viewModel.jarCardLink_1 != "" && viewModel.jarCardNumber_1.length == 16) true else false)
                {
                    Text("Сохранить банку", fontFamily = fontLight)
                }
            },
            dismissButton = {
                Button(colors = ButtonColors(containerColor = MyMilkText, contentColor = MyBackground, disabledContentColor = Color.Magenta, disabledContainerColor = Color.DarkGray),
                    onClick = {
                        viewModel.setJarCardNumerValue_1(viewModel.jar_card_1_temp)
                        viewModel.setJarCardLinkValue_1(viewModel.jar_1_link_temp)
                        //viewModel.setJarCardNumerValue_1("")
                        //viewModel.setJarCardLinkValue_1("")
                        //setCardValue_1("")
                        //setLinkValue_1("")
                        jarState_1.value = false
                    })
                {
                    Text("Отмена", fontFamily = fontLight)
                }
            }
        )
    }

    if (jarState_2.value) {
        AlertDialog(modifier = Modifier, containerColor = MyBackground, onDismissRequest = {
            viewModel.setJarCardNumerValue_2(viewModel.jar_card_2_temp)
            viewModel.setJarCardLinkValue_2(viewModel.jar_2_link_temp)
            jarState_2.value = false }, text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                Text("mono-банка №2", color = MyMilkText, style = MaterialTheme.typography.bodyMedium, fontSize = 22.sp, modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    .fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = fontLight)

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MyLightGrey))
                Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                    Text("Укажите номер банки", color = MyMilkText, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = fontLight)
                    TextFieldValidationCard(
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = viewModel.jarCardNumber_2,
                        onChange = viewModel::setJarCardNumerValue_2,
                        placeholder = "", modifier = Modifier.fillMaxWidth(),
                        onClear = { viewModel.setJarCardNumerValue_2("") }
                    )
                }
                Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                    Text("Укажите ссылку на банку", color = MyMilkText, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = fontLight)
                    TextFieldValidationCard(value = viewModel.jarCardLink_2, onChange = viewModel::setJarCardLinkValue_2, placeholder = "", modifier = Modifier, onClear = { viewModel.setJarCardLinkValue_2("") })
                } } },
            confirmButton = {
                Button(colors = ButtonColors(containerColor = MyMilkText, contentColor = MyBackground, disabledContentColor = Color.DarkGray, disabledContainerColor = newBlue),
                    onClick = {
                        //viewModel.setJarCardNumerValue_1(cardValue_1.value)
                        //viewModel.setJarCardLinkValue_1(linkValue_1.value)
                        viewModel.jar_card_2_temp = viewModel.jarCardNumber_2
                        viewModel.jar_2_link_temp = viewModel.jarCardLink_2
                        jarState_2.value = false
                    }, enabled = if(viewModel.jarCardNumber_2 != "" && viewModel.jarCardLink_2 != "" && viewModel.jarCardNumber_2.length == 16) true else false)
                {
                    Text("Сохранить банку", fontFamily = fontLight)
                }
            },
            dismissButton = {
                Button(colors = ButtonColors(containerColor = MyMilkText, contentColor = MyBackground, disabledContentColor = Color.Magenta, disabledContainerColor = Color.DarkGray),
                    onClick = {
                        viewModel.setJarCardNumerValue_2(viewModel.jar_card_2_temp)
                        viewModel.setJarCardLinkValue_2(viewModel.jar_2_link_temp)
                        //viewModel.setJarCardNumerValue_1("")
                        //viewModel.setJarCardLinkValue_1("")
                        //setCardValue_1("")
                        //setLinkValue_1("")
                        jarState_2.value = false
                    })
                {
                    Text("Отмена", fontFamily = fontLight)
                }
            }
        )
    }

    if (jarState_3.value) {
        AlertDialog(modifier = Modifier, containerColor = MyBackground, onDismissRequest = {
            viewModel.setJarCardNumerValue_3(viewModel.jar_card_3_temp)
            viewModel.setJarCardLinkValue_3(viewModel.jar_3_link_temp)
            jarState_3.value = false }, text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                Text("mono-банка №3", color = MyMilkText, style = MaterialTheme.typography.bodyMedium, fontSize = 22.sp, modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    .fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = fontLight)

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MyLightGrey))
                Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                    Text("Укажите номер банки", color = MyMilkText, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = fontLight)
                    TextFieldValidationCard(
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = viewModel.jarCardNumber_3,
                        onChange = viewModel::setJarCardNumerValue_3,
                        placeholder = "", modifier = Modifier.fillMaxWidth(),
                        onClear = { viewModel.setJarCardNumerValue_3("") }
                    )
                }
                Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                    Text("Укажите ссылку на банку", color = MyMilkText, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = fontLight)
                    TextFieldValidationCard(value = viewModel.jarCardLink_3, onChange = viewModel::setJarCardLinkValue_3, placeholder = "", modifier = Modifier, onClear = { viewModel.setJarCardLinkValue_3("") })
                } } },
            confirmButton = {
                Button(colors = ButtonColors(containerColor = MyMilkText, contentColor = MyBackground, disabledContentColor = Color.DarkGray, disabledContainerColor = newBlue),
                    onClick = {
                        //viewModel.setJarCardNumerValue_1(cardValue_1.value)
                        //viewModel.setJarCardLinkValue_1(linkValue_1.value)
                        viewModel.jar_card_3_temp = viewModel.jarCardNumber_3
                        viewModel.jar_3_link_temp = viewModel.jarCardLink_3
                        jarState_3.value = false
                    }, enabled = if(viewModel.jarCardNumber_3 != "" && viewModel.jarCardLink_3 != "" && viewModel.jarCardNumber_3.length == 16) true else false)
                {
                    Text("Сохранить банку", fontFamily = fontLight)
                }
            },
            dismissButton = {
                Button(colors = ButtonColors(containerColor = MyMilkText, contentColor = MyBackground, disabledContentColor = Color.Magenta, disabledContainerColor = Color.DarkGray),
                    onClick = {
                        viewModel.setJarCardNumerValue_3(viewModel.jar_card_3_temp)
                        viewModel.setJarCardLinkValue_3(viewModel.jar_3_link_temp)
                        //viewModel.setJarCardNumerValue_1("")
                        //viewModel.setJarCardLinkValue_1("")
                        //setCardValue_1("")
                        //setLinkValue_1("")
                        jarState_3.value = false
                    })
                {
                    Text("Отмена", fontFamily = fontLight)
                }
            }
        )
    }

    if (cardState_Black.value) {
        BlackCardAlarmDialog(viewModel = viewModel, cardState = cardState_Black)
    }

    if (cardState_White.value) {
        WhiteCardAlarmDialog(viewModel = viewModel, cardState = cardState_White)
    }

    if (cardState_FOP.value) {
        FopCardAlarmDialog(viewModel = viewModel, cardState = cardState_FOP)
    }

    ColumnScrollbar(
        state = listState,
        //indicatorContent = indicatorContent,
        settings = ScrollbarSettings(
            selectionMode = ScrollbarSelectionMode.Thumb,
            selectionActionable = ScrollbarSelectionActionable.WhenVisible,
            alwaysShowScrollbar = true,
            side = ScrollbarLayoutSide.End,
            thumbUnselectedColor = MyMilkText
        ),
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .verticalScroll(
//                state = rememberScrollState(),
//                enabled = true
//            ),
            .verticalScroll(listState)
            .background(MyBackground)
            .imePadding()
            .noRippleClickable {
                if (viewModel.blackCardValueIBAN.isEmpty()) {
                    showBlackCard.intValue = 0
                } else {
                    showBlackCard.intValue = 2
                }
                if (viewModel.whiteCardValueIBAN.isEmpty()) {
                    showWhiteCard.intValue = 0
                } else {
                    showWhiteCard.intValue = 2
                }
                if (viewModel.fopCardValueIBAN.isEmpty()) {
                    showFOPCard.intValue = 0
                } else {
                    showFOPCard.intValue = 2
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp, 50.dp, 30.dp, 0.dp)) {


            IconButton(onClick = {
                goBack()
                viewModel.setWhiteCards(whiteCard.value)
                viewModel.setBlackCards(blackCard.value)
//                viewModel.setJarCardLinkValue_1(jarLink1Viewmodel.value)
//                viewModel.setJarCardLinkValue_2(jarLink2Viewmodel.value)
//                viewModel.setJarCardLinkValue_3(jarLink3Viewmodel.value)
//                viewModel.setJarCardNumerValue_1(jarCard1Viewmodel.value)
//                viewModel.setJarCardNumerValue_2(jarCard2Viewmodel.value)
//                viewModel.setJarCardNumerValue_3(jarCard3Viewmodel.value)
                viewModel.setTokenName(token.value)
                viewModel.setAddress(serverUrl.value)

                viewModel.setDeviceId(deviceId.value)
                viewModel.setUserId(userId.value)

            }) {
                Icon(
                    painter = painterResource(id = R.drawable.circle_left),
                    contentDescription = "back button",
                    modifier = Modifier.size(35.dp),
                    tint = MyMilkText)
            }
            Text(text = header?.substringBefore("&") ?: "Подтверждение данных", color = MyMilkText, fontFamily = fontLight, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "logo",
                modifier = Modifier.size(45.dp)
            )

        }

            Column(
                verticalArrangement = Arrangement.Center
            ) {


                Column(modifier = Modifier.padding(30.dp, 10.dp)) {

                    Text(
                        text = "Monobank Token",
                        color = MyMilkText,
                        fontFamily = fontLight,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(7.dp)
                    )

                    Column(modifier = Modifier.shadow(1.dp, RoundedCornerShape(10.dp), true)) {
                        OutlinedTextField(value = viewModel.tokenValue,
                            onValueChange = { if (!it.contains("\n")) viewModel.setTokenName(it) },
                            singleLine = true,
                            textStyle = TextStyle(color = MyMilkText),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MyBackground),
                            colors = OutlinedTextFieldDefaults.colors(
                                cursorColor = MyMilkText,
                                focusedBorderColor = MyMilkText,
                                disabledContainerColor = MyMilkText,
                                unfocusedBorderColor = MyMilkText
                            ),
                            shape = RoundedCornerShape(10.dp),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    tint = MyMilkText,
                                    contentDescription = "Back button",
                                    modifier = Modifier.clickable {
                                        viewModel.setTokenName("")
                                    }
                                )
                            })
                    }

                }

                Column(modifier = Modifier.padding(30.dp, 10.dp)) {

                    Text(
                        text = "Test Request URL",
                        color = MyMilkText,
                        fontFamily = fontLight,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(7.dp)
                    )

                    Column(modifier = Modifier.shadow(1.dp, RoundedCornerShape(10.dp), true)) {
                        OutlinedTextField(value = viewModel.serverValue,
                            onValueChange = { if (!it.contains("\n")) viewModel.setAddress(it) },
                            singleLine = true,
                            textStyle = TextStyle(color = MyMilkText),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MyBackground),
                            colors = OutlinedTextFieldDefaults.colors(
                                cursorColor = MyMilkText,
                                focusedBorderColor = MyMilkText,
                                disabledContainerColor = MyMilkText,
                                unfocusedBorderColor = MyMilkText
                            ),
                            shape = RoundedCornerShape(10.dp),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    tint = MyMilkText,
                                    contentDescription = "Back button",
                                    modifier = Modifier.clickable {
                                        viewModel.setAddress("")
                                    }
                                )
                            })
                    }

                }


                Column(modifier = Modifier.padding(30.dp, 10.dp)) {

                    Text(
                        text = "Device ID",
                        color = MyMilkText,
                        fontFamily = fontLight,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(7.dp)
                    )

                    Column(modifier = Modifier.shadow(1.dp, RoundedCornerShape(10.dp), true)) {
                        OutlinedTextField(value = viewModel.deviceValue,
                            onValueChange = { if (!it.contains("\n")) viewModel.setDeviceId(it) },
                            singleLine = true,
                            textStyle = TextStyle(color = MyMilkText),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MyBackground),
                            colors = OutlinedTextFieldDefaults.colors(
                                cursorColor = MyMilkText,
                                focusedBorderColor = MyMilkText,
                                disabledContainerColor = MyMilkText,
                                unfocusedBorderColor = MyMilkText
                            ),
                            shape = RoundedCornerShape(10.dp),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    tint = MyMilkText,
                                    contentDescription = "Back button",
                                    modifier = Modifier.clickable {
                                        viewModel.setDeviceId("")
                                    }
                                )
                            })
                    }

                }

                Column(modifier = Modifier.padding(30.dp, 10.dp)) {

                    Text(
                        text = "User ID",
                        color = MyMilkText,
                        fontFamily = fontLight,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(7.dp)
                    )

                    Column(modifier = Modifier.shadow(1.dp, RoundedCornerShape(10.dp), true)) {
                        OutlinedTextField(value = viewModel.userValue,
                            onValueChange = { if (!it.contains("\n")) viewModel.setUserId(it) },
                            singleLine = true,
                            textStyle = TextStyle(color = MyMilkText),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MyBackground),
                            colors = OutlinedTextFieldDefaults.colors(
                                cursorColor = MyMilkText,
                                focusedBorderColor = MyMilkText,
                                disabledContainerColor = MyMilkText,
                                unfocusedBorderColor = MyMilkText
                            ),
                            shape = RoundedCornerShape(10.dp),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    tint = MyMilkText,
                                    contentDescription = "Back button",
                                    modifier = Modifier.clickable {
                                        viewModel.setUserId("")
                                    }
                                )
                            })
                    }

                }

                Column(modifier = Modifier.padding(30.dp, 10.dp)) {

                    Column {
                        Text(
                            text = "Черная карта monobank",
                            color = MyMilkText,
                            fontFamily = fontLight,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(7.dp)
                        )
                        if (viewModel.blackCardValueIBAN.isEmpty() && viewModel.blackCardValue.isEmpty()) {

                            GradientButton(elevation = 1.dp,
                                gradient = gradient1,
                                onClick = {
                                    cardState_Black.value = true
                                },
                                modifier = Modifier
                                    .padding(0.dp, 5.dp)
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                                    .onFocusChanged {
                                        if (!it.hasFocus) {
                                            coroutineScope.launch { focusRequester.requestFocus() }
                                        }
                                    }
                            ) {
                                Text(
                                    "Добавить черную карту",
                                    fontSize = 16.sp,
                                    color = MyMilkText,
                                    fontFamily = fontLight
                                )
                            }
                        } else {

                            OutlinedButton(onClick = { cardState_Black.value = true }, border = BorderStroke(1.dp, MyMilkText), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth(),
                                colors = ButtonColors(
                                    contentColor = Color(0XFF4E4E4D),
                                    containerColor = Color(0XFF323232),
                                    disabledContentColor = Color.DarkGray,
                                    disabledContainerColor = Color.LightGray
                                ),

                                ) {

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Изменить черную карту",
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 0.dp),
                                        textAlign = TextAlign.Center,
                                        color = MyMilkText,
                                        maxLines = 1,
                                        fontFamily = fontLight
                                    )

                                    IconButton(onClick = {
                                        viewModel.setBlackCards("")
                                        viewModel.setBlackCardsIBAN("")
                                        viewModel.setBlackCardsLink("")
                                        viewModel.black_card_temp = ""
                                        viewModel.black_card_IBAN_temp = ""
                                        viewModel.black_card_link_temp = ""

                                    }, modifier = Modifier.size(25.dp)) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "back button",
                                            modifier = Modifier.size(25.dp),
                                            tint = MyMilkText
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Column {
                        Text(
                            text = "Белая карта monobank",
                            color = MyMilkText,
                            fontFamily = fontLight,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(7.dp)
                        )

                        if (viewModel.whiteCardValueIBAN.isEmpty() && viewModel.whiteCardValue.isEmpty()) {

                            GradientButton(elevation = 1.dp,
                                gradient = gradient2,
                                onClick = {
                                    cardState_White.value = true
                                },
                                modifier = Modifier
                                    .padding(0.dp, 5.dp)
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                                    .onFocusChanged {
                                        if (!it.hasFocus) {
                                            coroutineScope.launch { focusRequester.requestFocus() }
                                        }
                                    }
                            ) {
                                Text(
                                    "Добавить белую карту",
                                    fontSize = 16.sp,
                                    color = MyBackground,
                                    fontFamily = fontLight
                                )
                            }
                        } else {

                            OutlinedButton(
                                onClick = { cardState_White.value = true },
                                border = BorderStroke(1.dp, MyMilkText),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonColors(
                                    contentColor = MyMilkText,
                                    containerColor = MyMilkText,
                                    disabledContentColor = Color.DarkGray,
                                    disabledContainerColor = Color.LightGray
                                ),
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Изменить белую карту",
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 0.dp),
                                        textAlign = TextAlign.Center,
                                        color = MyBackground,
                                        maxLines = 1,
                                        fontFamily = fontLight
                                    )

                                    IconButton(onClick = {
                                        viewModel.setWhiteCards("")
                                        viewModel.setWhiteCardsIBAN("")
                                        viewModel.setWhiteCardsLink("")
                                        viewModel.white_card_temp = ""
                                        viewModel.white_card_IBAN_temp = ""
                                        viewModel.white_card_link_temp = ""
                                    }, modifier = Modifier.size(25.dp)) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "back button",
                                            modifier = Modifier.size(25.dp),
                                            tint = MyBackground
                                        )
                                    }
                                }
                            }
                        }

                    }

                    Column {
                        Text(
                            text = "ФОП карта monobank",
                            color = MyMilkText,
                            fontFamily = fontLight,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(7.dp)
                        )
                        if (viewModel.fopCardValueIBAN.isEmpty() || viewModel.FOPCardValue.isEmpty()) {

                            GradientButton(elevation = 1.dp,
                                gradient = gradientFopCard,
                                onClick = {
                                    cardState_FOP.value = true
                                },
                                modifier = Modifier
                                    .padding(0.dp, 5.dp)
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                                    .onFocusChanged {
                                        if (!it.hasFocus) {
                                            coroutineScope.launch { focusRequester.requestFocus() }
                                        }
                                    }
                            ) {
                                Text(
                                    "Добавить карту ФОП",
                                    fontSize = 16.sp,
                                    color = MyMilkText,
                                    fontFamily = fontLight
                                )
                            }
                        } else {

                            OutlinedButton(
                                onClick = { cardState_FOP.value = true },
                                border = BorderStroke(1.dp, MyMilkText),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonColors(
                                    contentColor = textGrey,
                                    containerColor = Color(0xFF5fc7cf),
                                    disabledContentColor = Color.DarkGray,
                                    disabledContainerColor = Color.LightGray
                                )
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Изменить ФОП карту",
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 0.dp),
                                        textAlign = TextAlign.Center,
                                        color = MyMilkText,
                                        maxLines = 1,
                                        fontFamily = fontLight
                                    )

                                    IconButton(onClick = {
                                        viewModel.setFOPCards("")
                                        viewModel.setFOPCardsIBAN("")
                                        viewModel.fop_card_temp = ""
                                        viewModel.fop_card_IBAN_temp = ""
                                    }, modifier = Modifier.size(25.dp)) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "back button",
                                            modifier = Modifier.size(25.dp),
                                            tint = MyMilkText
                                        )
                                    }
                                }
                            }
                        }
                    }

                                }


                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(30.dp, 20.dp)
                ) {
                    if (viewModel.jarCardLink_1.isEmpty()) {

                        GradientButton(elevation = 1.dp,
                            gradient = gradient2,
                            onClick = {
                                jarState_1.value = true
                            },
                            modifier = Modifier
                                .padding(0.dp, 5.dp)
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onFocusChanged {
                                    if (!it.hasFocus) {
                                        coroutineScope.launch { focusRequester.requestFocus() }
                                    }
                                }
                        ) {
                            Text(
                                "Добавить mono-банку №1",
                                fontSize = 16.sp,
                                color = MyBackground,
                                fontFamily = fontLight
                            )
                        }
                    } else {

                        OutlinedButton(
                            onClick = { jarState_1.value = true },
                            border = BorderStroke(1.dp, MyMilkText),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Изменить mono-банку №1",
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 0.dp),
                                    textAlign = TextAlign.Center,
                                    color = MyMilkText,
                                    maxLines = 1, fontFamily = fontLight
                                )

                                IconButton(onClick = {
                                    viewModel.setJarCardNumerValue_1("")
                                    viewModel.setJarCardLinkValue_1("")
                                    viewModel.jar_card_1_temp = ""
                                    viewModel.jar_1_link_temp = ""
                                }, modifier = Modifier.size(25.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "back button",
                                        modifier = Modifier.size(25.dp),
                                        tint = MyMilkText
                                    )
                                }
                            }
                        }
                    }


                    if (viewModel.jarCardLink_2.isEmpty()) {

                        GradientButton(elevation = 1.dp,
                            gradient = gradient2,
                            onClick = {
                                jarState_2.value = true
                            },
                            modifier = Modifier
                                .padding(0.dp, 5.dp)
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onFocusChanged {
                                    if (!it.hasFocus) {
                                        coroutineScope.launch { focusRequester.requestFocus() }
                                    }
                                }
                        ) {
                            Text(
                                "Добавить mono-банку №2",
                                fontSize = 16.sp,
                                color = MyBackground,
                                fontFamily = fontLight
                            )
                        }
                    } else {

                        OutlinedButton(
                            onClick = { jarState_2.value = true },
                            border = BorderStroke(1.dp, MyMilkText),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Изменить mono-банку №2",
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 0.dp),
                                    textAlign = TextAlign.Center,
                                    color = MyMilkText,
                                    maxLines = 1, fontFamily = fontLight
                                )

                                IconButton(onClick = {
                                    viewModel.setJarCardNumerValue_2("")
                                    viewModel.setJarCardLinkValue_2("")
                                    viewModel.jar_card_2_temp = ""
                                    viewModel.jar_2_link_temp = ""
                                }, modifier = Modifier.size(25.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "back button",
                                        modifier = Modifier.size(25.dp),
                                        tint = MyMilkText
                                    )
                                }
                            }
                        }
                    }

                    if (viewModel.jarCardLink_3.isEmpty()) {

                        GradientButton(elevation = 1.dp,
                            gradient = gradient2,
                            onClick = {
                                jarState_3.value = true
                            },
                            modifier = Modifier
                                .padding(0.dp, 5.dp)
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onFocusChanged {
                                    if (!it.hasFocus) {
                                        coroutineScope.launch { focusRequester.requestFocus() }
                                    }
                                }
                        ) {
                            Text(
                                "Добавить mono-банку №3",
                                fontSize = 16.sp,
                                color = MyBackground,
                                fontFamily = fontLight
                            )
                        }
                    } else {

                        OutlinedButton(
                            onClick = { jarState_3.value = true },
                            border = BorderStroke(1.dp, MyMilkText),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Изменить mono-банку №3",
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 0.dp),
                                    textAlign = TextAlign.Center,
                                    color = MyMilkText,
                                    maxLines = 1, fontFamily = fontLight
                                )

                                IconButton(onClick = {
                                    viewModel.setJarCardNumerValue_3("")
                                    viewModel.setJarCardLinkValue_3("")
                                    viewModel.jar_card_3_temp = ""
                                    viewModel.jar_3_link_temp = ""
                                }, modifier = Modifier.size(25.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "back button",
                                        modifier = Modifier.size(25.dp),
                                        tint = MyMilkText
                                    )
                                }
                            }
                        }
                    }


                }
            }


            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MyMilkText,
                    disabledContainerColor = MyLightGrey
                ),
                onClick = {
                    UtilsObject.all_cards = mutableSetOf()
                    UtilsObject.all_jars_cards = mutableMapOf()

                    if (viewModel.jarCardLink_1 != "") {
                        UtilsObject.all_jars_cards.put(
                            viewModel.jarCardLink_1.substringAfter("ua/")
                                .filterNot { it.isWhitespace() },
                                    viewModel.jarCardNumber_1.filterNot { it.isWhitespace() })
                    }
                    if (viewModel.jarCardLink_2 != "") {
                        UtilsObject.all_jars_cards.put(
                            viewModel.jarCardLink_2.substringAfter("ua/")
                                .filterNot { it.isWhitespace() },
                                    viewModel.jarCardNumber_2.filterNot { it.isWhitespace() })
                    }
                    if (viewModel.jarCardLink_3 != "") {
                        UtilsObject.all_jars_cards.put(
                            viewModel.jarCardLink_3.substringAfter("ua/")
                                .filterNot { it.isWhitespace() },
                            viewModel.jarCardNumber_3.filterNot { it.isWhitespace() })
                    }

                    if (viewModel.whiteCardValueIBAN != "" || viewModel.whiteCardValue != "") UtilsObject.all_cards.add(Card("white", viewModel.whiteCardValue, viewModel.whiteCardValueIBAN, viewModel.whiteCardValueLink))
                    if (viewModel.blackCardValueIBAN != "" || viewModel.blackCardValue != "") UtilsObject.all_cards.add(Card("black", viewModel.blackCardValue, viewModel.blackCardValueIBAN, viewModel.blackCardValueLink))
                    if (viewModel.fopCardValueIBAN != "" || viewModel.FOPCardValue != "") UtilsObject.all_cards.add(Card(UtilsObject.fop_carta, viewModel.FOPCardValue, viewModel.fopCardValueIBAN))

//                    if (viewModel.whiteCardValueIBAN != "") UtilsObject.all_cards.put( "white",viewModel.whiteCardValueIBAN.filterNot { it.isWhitespace() })
//                    if (viewModel.blackCardValueIBAN != "") UtilsObject.all_cards.put("black",viewModel.blackCardValueIBAN.filterNot { it.isWhitespace() })
//                    if (viewModel.fopCardValueIBAN != "") UtilsObject.all_cards.put(UtilsObject.fop_carta, viewModel.fopCardValueIBAN.filterNot { it.isWhitespace() })
//                    //if (viewModel.FOPCardValue != "") UtilsObject.fopCardNumber = viewModel.FOPCardValue.filterNot { it.isWhitespace() }
//
//                    if (viewModel.whiteCardValue != "") UtilsObject.whiteCardNumber = viewModel.whiteCardValue.filterNot { it.isWhitespace() }
//                    if (viewModel.blackCardValue != "") UtilsObject.blackCardNumber = viewModel.blackCardValue.filterNot { it.isWhitespace() }
//                    if (viewModel.FOPCardValue != "") UtilsObject.fopCardNumber = viewModel.FOPCardValue.filterNot { it.isWhitespace() }
//
//                    if (viewModel.whiteCardValueLink != "") UtilsObject.whiteCardLink = viewModel.whiteCardValueLink.filterNot { it.isWhitespace() }
//                    if (viewModel.blackCardValueLink != "") UtilsObject.blackCardLink = viewModel.blackCardValueLink.filterNot { it.isWhitespace() }

                    if (viewModel.myToken == "") {
                        Intent(localContext, MonobankService::class.java).also {
                            it.action = MonobankService.Actions.START.toString()
                            localContext.startService(it)
                        }
                    } else if (!viewModel.isTheSame()) {
                        Log.e("MESSI INTENT: ", "RESTART token ")
                        LogStorage.addLog(localContext, "${getCurrentDate()}|content not the same, restart() triggered")

                        Intent(localContext, MonobankService::class.java).also {
                            it.action = MonobankService.Actions.RESTART.toString()
                            localContext.startService(it)
                        }
                    }


                    LogStorage.addLog(localContext, "${getCurrentDate()}|cards | black(${viewModel.blackCardValue}:${viewModel.blackCardValueIBAN}) white(${viewModel.whiteCardValue}:${viewModel.whiteCardValueIBAN}) fop(${viewModel.FOPCardValue}:${viewModel.fopCardValueIBAN})")

                    shared.edit().putString("token", viewModel.tokenValue).apply()
                    shared.edit().putString("server", viewModel.serverValue).apply()
                    //shared.edit().putString("server_ping", viewModel.serverPingValue).apply()
                    shared.edit().putString("black_card", viewModel.blackCardValue).apply()
                    shared.edit().putString("white_card", viewModel.whiteCardValue).apply()
                    shared.edit().putString("fop_card", viewModel.FOPCardValue).apply()

                    shared.edit().putString("black_card_iban", viewModel.blackCardValueIBAN).apply()
                    shared.edit().putString("white_card_iban", viewModel.whiteCardValueIBAN).apply()
                    shared.edit().putString("fop_card_iban", viewModel.fopCardValueIBAN).apply()

                    shared.edit().putString("black_card_link", viewModel.blackCardValueLink).apply()
                    shared.edit().putString("white_card_link", viewModel.whiteCardValueLink).apply()

                    shared.edit().putString("jar_link_1", viewModel.jarCardLink_1).apply()
                    shared.edit().putString("jar_link_2", viewModel.jarCardLink_2).apply()
                    shared.edit().putString("jar_link_3", viewModel.jarCardLink_3).apply()
                    shared.edit().putString("jar_card_1", viewModel.jarCardNumber_1).apply()
                    shared.edit().putString("jar_card_2", viewModel.jarCardNumber_2).apply()
                    shared.edit().putString("jar_card_3", viewModel.jarCardNumber_3).apply()
                    UtilsObject.server_name = viewModel.serverValue
                    //UtilsObject.server_ping = viewModel.serverPingValue
                    UtilsObject.token_monobank = viewModel.tokenValue
                    UtilsObject.device_id = viewModel.deviceValue
                    UtilsObject.user_id = viewModel.userValue
                    shared.edit().putString("device_id", viewModel.deviceValue).apply()
                    shared.edit().putString("user_id", viewModel.userValue).apply()
                    val timeStamp = shared.getLong("delay_time", 0L)
                    val delay = 60000 - (System.currentTimeMillis() - timeStamp)
                    viewModel.startTimer(delay)


//                    Log.e("MESSI ALL_CARDS: ", UtilsObject.all_cards.toString())
//                    Log.e("MESSI FOP_CARD: ", UtilsObject.fopCardNumber.toString())
//                    Log.e("MESSI black_iban: ", UtilsObject.blackCardIBAN.toString())
//                    Log.e("MESSI fop_iban: ", UtilsObject.fopCardIBAN.toString())


                    goNext()
                },
                enabled = viewModel.tokenValue.isNotEmpty()
                        //&& viewModel.serverValue.isNotEmpty()
                        && (viewModel.blackCardValueIBAN.isNotEmpty()
                        || viewModel.whiteCardValueIBAN.isNotEmpty()
                        || (viewModel.fopCardValueIBAN.isNotEmpty() && viewModel.FOPCardValue.isNotEmpty())
                        || viewModel.blackCardValue.isNotEmpty()
                        || viewModel.whiteCardValue.isNotEmpty()
                        || viewModel.jarCardLink_1.isNotEmpty()
                        || viewModel.jarCardLink_2.isNotEmpty()
                        || viewModel.jarCardLink_3.isNotEmpty()),
                modifier = Modifier
                    .padding(30.dp, 15.dp, 30.dp, 100.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(15)
            ) {
                Text(
                    text = "Подтвердить",
                    color = MyBackground,
                    fontFamily = fontLight,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Preview
@Composable
fun ScreenSummaryPreview() {
    Konvert2payMonoTheme {
        ScreenSummary ({ }, { }, header = "header&0")
    }
}