package com.example.konvert.konvert.paymono.screens.screen_banks

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.konvert.konvert.paymono.R
import com.example.konvert.konvert.paymono.noRippleClickable
import com.example.konvert.konvert.paymono.screens.custom_components.GradientButton
import com.example.konvert.konvert.paymono.screens.StateViewModel
import com.example.konvert.konvert.paymono.screens.custom_components.TextFieldValidationCard
import com.example.konvert.konvert.paymono.ui.theme.Konvert2payMonoTheme
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyLightGrey
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight
import com.example.konvert.konvert.paymono.ui.theme.gradient2
import com.example.konvert.konvert.paymono.ui.theme.newBlue
import kotlinx.coroutines.launch

@Composable
fun ScreenBanks(goNext: () -> Unit, goBack: () -> Unit, viewModel: StateViewModel = viewModel()) {

    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    val jarState_1 = remember { mutableStateOf(false) }
    val jarState_2 = remember { mutableStateOf(false) }
    val jarState_3 = remember { mutableStateOf(false) }


    //val cardValue_1 = remember { mutableStateOf(viewModel.jar_card_1_temp) }
    //val cardValue_2 = remember { mutableStateOf(viewModel.jar_card_2_temp) }
    //val cardValue_3 = remember { mutableStateOf(viewModel.jar_card_3_temp) }

    //val linkValue_1 = remember { mutableStateOf(viewModel.jar_1_link_temp) }
    //val linkValue_2 = remember { mutableStateOf(viewModel.jar_2_link_temp) }
    //val linkValue_3 = remember { mutableStateOf(viewModel.jar_3_link_temp) }

    val localContext = LocalContext.current
    val shared = localContext.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)



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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MyBackground).imePadding()
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (!it.hasFocus) {
                    coroutineScope.launch {
                        focusRequester.requestFocus()
                    }
                }
            }
            .noRippleClickable { //if (viewModel.blackCardValue.isEmpty()) showBlackCard.intValue = 0 else showBlackCard.intValue = 2
                //if (viewModel.whiteCardValue.isEmpty()) showWhiteCard.intValue = 0 else showWhiteCard.intValue = 2
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth().weight(1f)
            .padding(30.dp, 0.dp, 30.dp, 0.dp)) {

            IconButton(onClick = { goBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.circle_left),
                    contentDescription = "back button",
                    modifier = Modifier.size(35.dp),
                    tint = MyMilkText)
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.weight(5f)) {
            Text(text = "Jars",
                textAlign = TextAlign.Center,
                color = MyMilkText,
                modifier = Modifier.padding(20.dp),
                fontWeight = FontWeight.Bold)
            Image(painter = painterResource(id = R.drawable.img),
                contentDescription = "logo",
                modifier = Modifier.size(150.dp))

            Text(text = "На этом экране вы можете добавить\n" +
                    "необходимые mono-банки.",
                textAlign = TextAlign.Center,
                color = MyMilkText,
                modifier = Modifier)


        }

        Column(verticalArrangement = Arrangement.Top, modifier = Modifier.padding(30.dp, 0.dp).weight(4f)) {
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

                OutlinedButton(onClick = { jarState_1.value = true }, border = BorderStroke(1.dp, MyMilkText), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Изменить mono-банку №1",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(horizontal = 0.dp),
                            textAlign = TextAlign.Center,
                            color = MyMilkText,
                            maxLines = 1,
                            fontFamily = fontLight
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

                OutlinedButton(onClick = { jarState_2.value = true }, border = BorderStroke(1.dp, MyMilkText), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Изменить mono-банку №2",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(horizontal = 0.dp),
                            textAlign = TextAlign.Center,
                            color = MyMilkText,
                            maxLines = 1,
                            fontFamily = fontLight
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

                OutlinedButton(onClick = { jarState_3.value = true }, border = BorderStroke(1.dp, MyMilkText), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Изменить mono-банку №3",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(horizontal = 0.dp),
                            textAlign = TextAlign.Center,
                            color = MyMilkText,
                            maxLines = 1,
                            fontFamily = fontLight
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

        Column(modifier = Modifier.fillMaxWidth().weight(2f).padding(0.dp, 0.dp)) {

            Button(colors = ButtonDefaults.buttonColors(containerColor = MyMilkText), onClick = {
                shared.edit().putString("jar_link_1", viewModel.jarCardLink_1).apply()
                shared.edit().putString("jar_link_2", viewModel.jarCardLink_2).apply()
                shared.edit().putString("jar_link_3", viewModel.jarCardLink_3).apply()
                shared.edit().putString("jar_card_1", viewModel.jarCardNumber_1).apply()
                shared.edit().putString("jar_card_2", viewModel.jarCardNumber_2).apply()
                shared.edit().putString("jar_card_3", viewModel.jarCardNumber_3).apply()


                goNext()

            }, modifier = Modifier
                .padding(30.dp, 0.dp, 30.dp, 0.dp)
                .fillMaxWidth(),
                shape = RoundedCornerShape(15)
            ) { Text(text = "Подтвердить", color = MyBackground, fontFamily = fontLight, fontSize = 16.sp) }
        }


    }
}

@Preview
@Composable
fun ScreenBanksPreview() {
    Konvert2payMonoTheme {
        ScreenBanks ({ }, { })
    }
}