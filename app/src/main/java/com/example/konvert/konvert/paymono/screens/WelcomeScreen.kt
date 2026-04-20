//package com.example.konvert.konvert.paymono.screens
//
//import android.content.Context
//import android.content.Intent
//import android.util.Log
//import androidx.activity.compose.BackHandler
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Clear
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonColors
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.konvert.konvert.paymono.MonobankService
//import com.example.konvert.konvert.paymono.UtilsObject
//import com.example.konvert.konvert.paymono.ui.theme.MyBackground
//import com.example.konvert.konvert.paymono.ui.theme.MyDarkGrey
//import com.example.konvert.konvert.paymono.ui.theme.MyLightGrey
//import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
//
//@Composable
//fun WelcomeScreen(goNext: () -> Unit, viewModel: StateViewModel = viewModel()) {
//
//    val context = LocalContext.current
//
//    val shared = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
//    val token_monobank = shared.getString("token", "").toString()
//    val server_name = shared.getString("server", "").toString()
//
//    val device_id = shared.getString("device_id", "").toString()
//    val user_id = shared.getString("user_id", "").toString()
//
//    val black_card = shared.getString("black_card", "").toString()
//    val white_card = shared.getString("white_card", "").toString()
//    val jar_link_1 = shared.getString("jar_link_1", "").toString()
//    val jar_link_2 = shared.getString("jar_link_2", "").toString()
//    val jar_link_3 = shared.getString("jar_link_3", "").toString()
//        ///////////////////////////////////////
//    val jar_card_1 = shared.getString("jar_card_1", "").toString()
//    val jar_card_2 = shared.getString("jar_card_2", "").toString()
//    val jar_card_3 = shared.getString("jar_card_3", "").toString()
//
//
//
//
//    val jarState_1 = remember { mutableStateOf(false) }
//
//    val jarState_2 = remember { mutableStateOf(false) }
//
//    val jarState_3 = remember { mutableStateOf(false) }
//
//
//    //viewModel.init(token_monobank, server_name, black_card, white_card, jar_link_1, jar_link_2, jar_link_3, device_id, user_id, jar_card_1, jar_card_2, jar_card_3)
//
////
////    if (jarState_1.value) {
////        AlertDialog(containerColor = MyLightGrey, onDismissRequest = { jarState_1.value = false }, text = {
////                Column {
//////                    Column(modifier = Modifier.padding(0.dp, 10.dp)) {
//////                        Text("Введите имя банки №1", color = MyDarkGrey, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier.padding(10.dp, 0.dp))
//////                        TextFieldValidationCard(value = viewModel.jarName_1, onChange = viewModel::setJarNameValue_1, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
//////                    }
////                    Column(modifier = Modifier.padding(0.dp, 10.dp)) {
////                        Text("Введите cсылку на банку №1", color = MyDarkGrey, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier.padding(10.dp, 0.dp))
////                        TextFieldValidationCard(value = viewModel.jarCardLink_1, onChange = viewModel::setJarCardLinkValue_1, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
////                    }
////                    Column(modifier = Modifier.padding(0.dp, 10.dp)) {
////                        Text("Введите номер карты банки №1", color = MyDarkGrey, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier.padding(10.dp, 0.dp))
////                        TextFieldValidationCard(value = viewModel.jarCardNumber_1, onChange = viewModel::setJarCardNumerValue_1, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
////                    } } }, confirmButton = { Button(colors = ButtonColors(containerColor = MyDarkGrey, contentColor = Color.White, disabledContentColor = Color.Magenta, disabledContainerColor = Color.DarkGray), onClick = { jarState_1.value = false }) { Text("OK") } },)
////    }
////
////    if (jarState_2.value) {
////        AlertDialog(containerColor = MyLightGrey, onDismissRequest = { jarState_2.value = false }, text = {
////            Column {
//////                Column(modifier = Modifier.padding(0.dp, 10.dp)) {
//////                    Text("Введите имя банки №2", color = MyDarkGrey, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier.padding(10.dp, 0.dp))
//////                    TextFieldValidationCard(value = viewModel.jarName_2, onChange = viewModel::setJarNameValue_2, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
//////                }
////                Column(modifier = Modifier.padding(0.dp, 10.dp)) {
////                    Text("Введите cсылку на банку №2", color = MyDarkGrey, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier.padding(10.dp, 0.dp))
////                    TextFieldValidationCard(value = viewModel.jarCardLink_2, onChange = viewModel::setJarCardLinkValue_2, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
////                }
////                Column(modifier = Modifier.padding(0.dp, 10.dp)) {
////                    Text("Введите номер карты банки №2", color = MyDarkGrey, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier.padding(10.dp, 0.dp))
////                    TextFieldValidationCard(value = viewModel.jarCardNumber_2, onChange = viewModel::setJarCardNumerValue_2, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
////                } } }, confirmButton = { Button(colors = ButtonColors(containerColor = Color.Black, contentColor = Color.White, disabledContentColor = Color.Magenta, disabledContainerColor = Color.DarkGray), onClick = { jarState_2.value = false }) { Text("OK") } },)
////    }
////
////    if (jarState_3.value) {
////        AlertDialog(containerColor = MyLightGrey, onDismissRequest = { jarState_3.value = false }, text = {
////            Column {
//////                Column(modifier = Modifier.padding(0.dp, 10.dp)) {
//////                    Text("Введите имя банки №3", color = MyDarkGrey, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier.padding(10.dp, 0.dp))
//////                    TextFieldValidationCard(value = viewModel.jarName_3, onChange = viewModel::setJarNameValue_3, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
//////                }
////                Column(modifier = Modifier.padding(0.dp, 10.dp)) {
////                    Text("Введите cсылку на банку №3", color = MyDarkGrey, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier.padding(10.dp, 0.dp))
////                    TextFieldValidationCard(value = viewModel.jarCardLink_3, onChange = viewModel::setJarCardLinkValue_3, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
////                }
////                Column(modifier = Modifier.padding(0.dp, 10.dp)) {
////                    Text("Введите номер карты банки №3", color = MyDarkGrey, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier.padding(10.dp, 0.dp))
////                    TextFieldValidationCard(value = viewModel.jarCardNumber_3, onChange = viewModel::setJarCardNumerValue_3, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
////                } } }, confirmButton = { Button(colors = ButtonColors(containerColor = Color.Black, contentColor = Color.White, disabledContentColor = Color.Magenta, disabledContainerColor = Color.DarkGray), onClick = { jarState_3.value = false }) { Text("OK") } },)
////    }
////
////    Column(
////        modifier = Modifier
////            .fillMaxSize()
////            .verticalScroll(rememberScrollState())
////            .background(MyBackground),
////        horizontalAlignment = Alignment.CenterHorizontally,
////        verticalArrangement = Arrangement.Center
////    ) {
////
////
////        Column(Modifier.padding(0.dp, 100.dp, 0.dp, 0.dp)) {
////            Text(text = "Monobank user TOKEN", fontSize = 18.sp, modifier = Modifier
////                .fillMaxWidth()
////                .padding(horizontal = 65.dp), textAlign = TextAlign.Start, color = MyMilkText)
////            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
////                TextFieldValidationCard(value = viewModel.tokenValue, onChange = viewModel::setTokenName, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
////            }
////        }
////
////
////        Column(Modifier.padding(0.dp, 10.dp)) {
////            Text(text = "Server Address", fontSize = 18.sp, modifier = Modifier
////                .fillMaxWidth()
////                .padding(horizontal = 65.dp), textAlign = TextAlign.Start, color = MyMilkText)
////            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
////                TextFieldValidationCard(value = viewModel.serverValue, onChange = viewModel::setAddress, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
////            }
////        }
////
////        Column(Modifier.padding(0.dp, 10.dp)) {
////            Text(text = "Введите User ID (user_id)", fontSize = 18.sp, modifier = Modifier
////                .fillMaxWidth()
////                .padding(horizontal = 65.dp), textAlign = TextAlign.Start, color = MyMilkText)
////            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
////                TextFieldValidationCard(value = viewModel.userIdValue, onChange = viewModel::setUserId, placeholder = "",  modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
////            }
////        }
////
////        Column(Modifier.padding(0.dp, 10.dp)) {
////            Text(text = "Введите Device ID (id)", fontSize = 18.sp, modifier = Modifier
////                .fillMaxWidth()
////                .padding(horizontal = 65.dp), textAlign = TextAlign.Start, color = MyMilkText)
////            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
////                TextFieldValidationCard(value = viewModel.deviceIdValue, onChange = viewModel::setDeviceId, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
////            }
////        }
////
////        Column(Modifier.padding(0.dp, 10.dp)) {
////            Text(text = "Введите номер черной карты", fontSize = 18.sp, modifier = Modifier
////                .fillMaxWidth()
////                .padding(horizontal = 65.dp), textAlign = TextAlign.Start, color = MyMilkText)
////            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
////                TextFieldValidationCard(value = viewModel.blackCardValue, onChange = viewModel::setBackValue, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
////            }
////        }
////
////        Column(Modifier.padding(0.dp, 10.dp)) {
////            Text(text = "Введите номер белой карты", fontSize = 18.sp, modifier = Modifier
////                .fillMaxWidth()
////                .padding(horizontal = 65.dp), textAlign = TextAlign.Start, color = MyMilkText)
////            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
////                TextFieldValidationCard(value = viewModel.whiteCardValue, onChange = viewModel::setWhiteValue, placeholder = "", modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp))
////            }
////        }
////
////        if (viewModel.jarCardLink_1 == "") {
////            Button(
////                onClick = { jarState_1.value = true },
////                colors = ButtonDefaults.buttonColors(containerColor = MyDarkGrey)
////            ) { Text(text = "Добавить банку №1", color = MyMilkText) }
////        } else {
////            Card(
////                modifier = Modifier.padding(20.dp, 5.dp),
////                elevation = CardDefaults.elevatedCardElevation(5.dp)
////            ) {
////                Column(
////                    Modifier
////                        .padding(0.dp, 10.dp)
////                        .fillMaxWidth()
////                ) {
////                    Text(
////                        text = "Банка #1",
////                        fontSize = 18.sp,
////                        modifier = Modifier
////                            .fillMaxWidth()
////                            .padding(horizontal = 65.dp),
////                        textAlign = TextAlign.Start,
////                        color = Color.DarkGray
////                    )
////                    Row(
////                        modifier = Modifier.fillMaxWidth(),
////                        verticalAlignment = Alignment.CenterVertically,
////                        horizontalArrangement = Arrangement.SpaceBetween
////                    ) {
////                        Text(
////                            text = viewModel.jarCardLink_1,
////                            fontSize = 18.sp,
////                            modifier = Modifier
////                                .padding(horizontal = 65.dp)
////                                .weight(8f),
////                            textAlign = TextAlign.Start,
////                            color = Color.DarkGray
////                        )
////                        IconButton(onClick = {
////                            viewModel.setJarCardLinkValue_1("")
////                            viewModel.setJarCardNumerValue_1("")
////                            shared.edit().putString("jar_card_1", viewModel.jarCardNumber_1).apply()
////                            shared.edit().putString("jar_link_1", viewModel.jarCardLink_1).apply()
////                        },
////                            modifier = Modifier.weight(2f)) { Icon(Icons.Filled.Clear, contentDescription = "Clear") }
////                    }
////                }
////            }
////        }
////
////        if (viewModel.jarCardLink_2 == "") {
////            Button(
////                onClick = { jarState_2.value = true },
////                colors = ButtonDefaults.buttonColors(containerColor = MyDarkGrey)
////            ) { Text(text = "Добавить банку №2", color = MyMilkText) }
////        } else {
////            Card(
////                modifier = Modifier.padding(20.dp, 5.dp),
////                elevation = CardDefaults.elevatedCardElevation(5.dp)
////            ) {
////                Column(
////                    Modifier
////                        .padding(0.dp, 10.dp)
////                        .fillMaxWidth()
////                ) {
////                    Text(
////                        text = "Банка #2",
////                        fontSize = 18.sp,
////                        modifier = Modifier
////                            .fillMaxWidth()
////                            .padding(horizontal = 65.dp),
////                        textAlign = TextAlign.Start,
////                        color = Color.DarkGray
////                    )
////                    Row(
////                        modifier = Modifier.fillMaxWidth(),
////                        verticalAlignment = Alignment.CenterVertically,
////                        horizontalArrangement = Arrangement.SpaceBetween
////                    ) {
////                        Text(
////                            text = viewModel.jarCardLink_2,
////                            fontSize = 18.sp,
////                            modifier = Modifier
////                                .padding(horizontal = 65.dp)
////                                .weight(8f),
////                            textAlign = TextAlign.Start,
////                            color = Color.DarkGray
////                        )
////                        IconButton(onClick = {
////                            viewModel.setJarCardLinkValue_2("")
////                            viewModel.setJarCardNumerValue_2("")
////                            shared.edit().putString("jar_card_2", viewModel.jarCardNumber_2).apply()
////                            shared.edit().putString("jar_link_2", viewModel.jarCardLink_2).apply()
////                        },
////                            modifier = Modifier.weight(2f)) { Icon(Icons.Filled.Clear, contentDescription = "Clear") }
////                    }
////                }
////            }
////        }
////
////        if (viewModel.jarCardLink_3 == "") {
////            Button(
////                onClick = { jarState_3.value = true },
////                colors = ButtonDefaults.buttonColors(containerColor = MyDarkGrey)
////            ) { Text(text = "Добавить банку №3", color = MyMilkText) }
////        } else {
////            Card(
////                modifier = Modifier.padding(20.dp, 5.dp),
////                elevation = CardDefaults.elevatedCardElevation(5.dp)
////            ) {
////                Column(
////                    Modifier
////                        .padding(0.dp, 10.dp)
////                        .fillMaxWidth()
////                ) {
////                    Text(
////                        text = "Банка #3",
////                        fontSize = 18.sp,
////                        modifier = Modifier
////                            .fillMaxWidth()
////                            .padding(horizontal = 65.dp),
////                        textAlign = TextAlign.Start,
////                        color = Color.DarkGray
////                    )
////                    Row(
////                        modifier = Modifier.fillMaxWidth(),
////                        verticalAlignment = Alignment.CenterVertically,
////                        horizontalArrangement = Arrangement.SpaceBetween
////                    ) {
////                        Text(
////                            text = viewModel.jarCardLink_3,
////                            fontSize = 18.sp,
////                            modifier = Modifier
////                                .padding(horizontal = 65.dp)
////                                .weight(8f),
////                            textAlign = TextAlign.Start,
////                            color = Color.DarkGray
////                        )
////                        IconButton(onClick = {
////                            viewModel.setJarCardLinkValue_3("")
////                            viewModel.setJarCardNumerValue_3("")
////                            shared.edit().putString("jar_card_3", viewModel.jarCardNumber_3).apply()
////                            shared.edit().putString("jar_link_3", viewModel.jarCardLink_3).apply()
////                        },
////                            modifier = Modifier.weight(2f)) { Icon(Icons.Filled.Clear, contentDescription = "Clear") }
////                    }
////                }
////            }
////        }
//
//        //Log.e("MESSI: ", viewModel.tokenValue)
//
//        Button(colors = ButtonDefaults.buttonColors(containerColor = MyDarkGrey),
//            onClick = {
//                goNext()
//                shared.edit().putString("token", viewModel.tokenValue).apply()
//                shared.edit().putString("server", viewModel.serverValue).apply()
//                shared.edit().putString("black_card", viewModel.blackCardValue).apply()
//                shared.edit().putString("white_card", viewModel.whiteCardValue).apply()
//                shared.edit().putString("jar_link_1", viewModel.jarCardLink_1).apply()
//                shared.edit().putString("jar_link_2", viewModel.jarCardLink_2).apply()
//                shared.edit().putString("jar_link_3", viewModel.jarCardLink_3).apply()
//                shared.edit().putString("jar_card_1", viewModel.jarCardNumber_1).apply()
//                shared.edit().putString("jar_card_2", viewModel.jarCardNumber_2).apply()
//                shared.edit().putString("jar_card_3", viewModel.jarCardNumber_3).apply()
//                UtilsObject.server_name = viewModel.serverValue
//                UtilsObject.token_monobank = viewModel.tokenValue
//                UtilsObject.device_id = viewModel.deviceValue
//                UtilsObject.user_id = viewModel.userValue
//                shared.edit().putString("device_id", viewModel.deviceValue).apply()
//                shared.edit().putString("user_id", viewModel.userValue).apply()
////                try {
////                    UtilsObject.pre_account_numbers.add(
////                        viewModel.blackCardValue.substring(
////                            viewModel.blackCardValue.length - 4,
////                            viewModel.blackCardValue.length
////                        )
////                    )
////                } catch (e: Exception) {
////                    //Log.e("MESSI viewmodel substring: ", e.toString())
////                }
////
////                try {
////                    UtilsObject.pre_account_numbers.add(
////                        viewModel.whiteCardValue.substring(
////                            viewModel.whiteCardValue.length - 4,
////                            viewModel.whiteCardValue.length
////                        )
////                    )
////                } catch (e: Exception) {
////                    //Log.e("MESSI substring 2: ", e.toString())
////                }
////
////                if (viewModel.jarCardLink_1 != "") {
////                    UtilsObject.pre_account_numbers.add(viewModel.jarCardLink_1.substringAfter("ua/").filterNot { it.isWhitespace() })
////                    UtilsObject.all_jars_cards.put(viewModel.jarCardLink_1.substringAfter("ua/").filterNot { it.isWhitespace() }, viewModel.jarCardNumber_1.filterNot { it.isWhitespace() })
////                }
////                if (viewModel.jarCardLink_2 != "") {
////                    UtilsObject.pre_account_numbers.add(viewModel.jarCardLink_2.substringAfter("ua/").filterNot { it.isWhitespace() })
////                    UtilsObject.all_jars_cards.put(viewModel.jarCardLink_2.substringAfter("ua/").filterNot { it.isWhitespace() }, viewModel.jarCardNumber_2.filterNot { it.isWhitespace() })
////                }
////                if (viewModel.jarCardLink_3 != "") {
////                    UtilsObject.pre_account_numbers.add(viewModel.jarCardLink_3.substringAfter("ua/").filterNot { it.isWhitespace() })
////                    UtilsObject.all_jars_cards.put(viewModel.jarCardLink_3.substringAfter("ua/").filterNot { it.isWhitespace() }, viewModel.jarCardNumber_3.filterNot { it.isWhitespace() })
////                }
//
////                if (viewModel.whiteCardValue != "") UtilsObject.all_cards.put("white", viewModel.whiteCardValue.filterNot { it.isWhitespace() })
////                if (viewModel.blackCardValue != "") UtilsObject.all_cards.put("black", viewModel.blackCardValue.filterNot { it.isWhitespace() })
////                if (viewModel.jarCardNumber_1 != "") UtilsObject.all_cards.add(viewModel.jarCardNumber_1)
////                if (viewModel.jarCardNumber_2 != "") UtilsObject.all_cards.add(viewModel.jarCardNumber_2)
////                if (viewModel.jarCardNumber_3 != "") UtilsObject.all_cards.add(viewModel.jarCardNumber_3)
////                UtilsObject.all_cards.add(viewModel.whiteCardValue)
////                UtilsObject.all_cards.add(viewModel.blackCardValue)
////                UtilsObject.all_cards.add(viewModel.jarCardNumber_1)
////                UtilsObject.all_cards.add(viewModel.jarCardNumber_2)
////                UtilsObject.all_cards.add(viewModel.jarCardNumber_3)
//
//
//                Intent(context, MonobankService::class.java).also {
//                    it.action = MonobankService.Actions.START.toString()
//                    context.startService(it)
//                }
//        },
//            enabled = viewModel.tokenValue.isNotEmpty() && viewModel.serverValue.isNotEmpty() && (viewModel.blackCardValue.isNotEmpty()
//                    || viewModel.whiteCardValue.isNotEmpty()
//                    || viewModel.jarCardLink_1.isNotEmpty()
//                    || viewModel.jarCardLink_2.isNotEmpty()
//                    || viewModel.jarCardLink_3.isNotEmpty()),
//            modifier = Modifier.padding(0.dp, 15.dp, 0.dp, 100.dp)
//        ) { Text(text = "Next", color = MyMilkText) }
//    }
////}
//
//@Preview
//@Composable
//fun WelcomeScreenPreview() {
//    WelcomeScreen ({ })
//}