package com.example.konvert.konvert.paymono.screens.screen_cards

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.konvert.konvert.paymono.UtilsObject
import com.example.konvert.konvert.paymono.data.Card
import com.example.konvert.konvert.paymono.screens.StateViewModel
import com.example.konvert.konvert.paymono.screens.custom_components.TextFieldValidationCard
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyLightGrey
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.MyRed
import com.example.konvert.konvert.paymono.ui.theme.fontLight
import com.example.konvert.konvert.paymono.ui.theme.newBlue




@Composable
fun BlackCardAlarmDialog(viewModel: StateViewModel, cardState: MutableState<Boolean>) {
    AlertDialog(modifier = Modifier, containerColor = MyBackground, onDismissRequest = {
        viewModel.setBlackCards(viewModel.black_card_temp)
        viewModel.setBlackCardsIBAN(viewModel.black_card_IBAN_temp)
        viewModel.setBlackCardsLink(viewModel.black_card_link_temp)

        cardState.value = false
    }, text = {
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                "Черная карта",
                color = MyMilkText,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontFamily = fontLight
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MyLightGrey)
            )
            Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                Text(text =
                    buildAnnotatedString {
                        append("Укажите номер карты")
                        withStyle(style = SpanStyle(color = MyRed, fontSize = 22.sp, fontWeight = FontWeight.Bold)) {
                            append(" *")
                        }
                    },
                    color = MyMilkText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = fontLight
                )
                TextFieldValidationCard(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = viewModel.blackCardValue,
                    onChange = viewModel::setBlackCards,
                    placeholder = "", modifier = Modifier.fillMaxWidth(),
                    onClear = {
                        viewModel.setBlackCards("")
                    }
                )
            }
            Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                Text(
                    "Укажите IBAN карты",
                    color = MyMilkText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = fontLight
                )
                TextFieldValidationCard(
                    value = viewModel.blackCardValueIBAN,
                    onChange = viewModel::setBlackCardsIBAN,
                    placeholder = "",
                    modifier = Modifier,
                    onClear = {
                        viewModel.setBlackCardsIBAN("")
                        //UtilsObject.blackCardIBAN = ""
                    })
            }

            Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                Text(
                    "Укажите ссылку пополнения карты",
                    color = MyMilkText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = fontLight
                )
                TextFieldValidationCard(
                    value = viewModel.blackCardValueLink,
                    onChange = viewModel::setBlackCardsLink,
                    placeholder = "", modifier = Modifier.fillMaxWidth(),
                    onClear = {
                        viewModel.setBlackCardsLink("")
                        //UtilsObject.blackCardLink = ""
                    }
                )
            }
        }
    },
        confirmButton = {
            Button(colors = ButtonColors(
                containerColor = MyMilkText,
                contentColor = MyBackground,
                disabledContentColor = Color.DarkGray,
                disabledContainerColor = newBlue
            ),
                onClick = {

                    viewModel.black_card_temp = viewModel.blackCardValue
                    viewModel.black_card_IBAN_temp = viewModel.blackCardValueIBAN
                    viewModel.black_card_link_temp = viewModel.blackCardValueLink

                    UtilsObject.all_cards.clear()
                    if (viewModel.whiteCardValueIBAN != "" || viewModel.whiteCardValue != "") UtilsObject.all_cards.add(Card("white", viewModel.whiteCardValue, viewModel.whiteCardValueIBAN, viewModel.whiteCardValueLink))
                    if (viewModel.blackCardValueIBAN != "" || viewModel.blackCardValue != "") UtilsObject.all_cards.add(Card("black", viewModel.blackCardValue, viewModel.blackCardValueIBAN, viewModel.blackCardValueLink))
                    if (viewModel.fopCardValueIBAN != "" || viewModel.FOPCardValue != "") UtilsObject.all_cards.add(Card(UtilsObject.fop_carta, viewModel.FOPCardValue, viewModel.fopCardValueIBAN))
                    cardState.value = false
                },
                enabled = if ( (viewModel.blackCardValue != "" && viewModel.blackCardValue.length == 16) || (viewModel.blackCardValueIBAN != "" && viewModel.blackCardValueIBAN.length == 29)) true else false
            )
            {
                Log.e("MESSI blackCardValue: ", viewModel.blackCardValue)
                Log.e("MESSI blackCardValue: ", viewModel.blackCardValueIBAN)
                Log.e("MESSI blackCardValue: ", viewModel.blackCardValueLink)
                Text("Сохранить карту", fontFamily = fontLight)
            }
        },
        dismissButton = {
            Button(colors = ButtonColors(
                containerColor = MyMilkText,
                contentColor = MyBackground,
                disabledContentColor = Color.Magenta,
                disabledContainerColor = Color.DarkGray
            ),
                onClick = {
                    viewModel.setBlackCards(viewModel.black_card_temp)
                    viewModel.setBlackCardsIBAN(viewModel.black_card_IBAN_temp)
                    viewModel.setBlackCardsLink(viewModel.black_card_link_temp)

                    cardState.value = false
                })
            {
                Text("Отмена", fontFamily = fontLight)
            }
        }
    )
}

@Composable
fun WhiteCardAlarmDialog(viewModel: StateViewModel, cardState: MutableState<Boolean>) {
    AlertDialog(modifier = Modifier, containerColor = MyBackground, onDismissRequest = {
        viewModel.setWhiteCards(viewModel.white_card_temp)
        viewModel.setWhiteCardsIBAN(viewModel.white_card_IBAN_temp)
        viewModel.setWhiteCardsLink(viewModel.white_card_link_temp)

        cardState.value = false
    }, text = {
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                "Белая карта",
                color = MyMilkText,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontFamily = fontLight
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MyLightGrey)
            )
            Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                Text(
                    text =
                    buildAnnotatedString {
                        append("Укажите номер карты")
                        withStyle(style = SpanStyle(color = MyRed, fontSize = 22.sp, fontWeight = FontWeight.Bold)) {
                            append(" *")
                        }
                    },
                    color = MyMilkText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = fontLight
                )
                TextFieldValidationCard(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = viewModel.whiteCardValue,
                    onChange = viewModel::setWhiteCards,
                    placeholder = "", modifier = Modifier.fillMaxWidth(),
                    onClear = {
                        viewModel.setWhiteCards("")
                    }
                )
            }
            Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                Text(
                    "Укажите IBAN карты",
                    color = MyMilkText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = fontLight
                )
                TextFieldValidationCard(
                    value = viewModel.whiteCardValueIBAN,
                    onChange = viewModel::setWhiteCardsIBAN,
                    placeholder = "",
                    modifier = Modifier,
                    onClear = {
                        viewModel.setWhiteCardsIBAN("")
                        //UtilsObject.whiteCardIBAN = ""
                    })
            }
            Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                Text(
                    "Укажите ссылку пополнения карты",
                    color = MyMilkText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = fontLight
                )
                TextFieldValidationCard(
                    value = viewModel.whiteCardValueLink,
                    onChange = viewModel::setWhiteCardsLink,
                    placeholder = "",
                    modifier = Modifier,
                    onClear = {
                        viewModel.setWhiteCardsLink("")
                        //UtilsObject.whiteCardLink = ""
                    })
            }
        }
    },
        confirmButton = {
            Button(colors = ButtonColors(
                containerColor = MyMilkText,
                contentColor = MyBackground,
                disabledContentColor = Color.DarkGray,
                disabledContainerColor = newBlue
            ),
                onClick = {

                    viewModel.white_card_temp = viewModel.whiteCardValue
                    viewModel.white_card_IBAN_temp = viewModel.whiteCardValueIBAN
                    viewModel.white_card_link_temp = viewModel.whiteCardValueLink


                    UtilsObject.all_cards.clear()
                    if (viewModel.whiteCardValueIBAN != "" || viewModel.whiteCardValue != "") UtilsObject.all_cards.add(Card("white", viewModel.whiteCardValue, viewModel.whiteCardValueIBAN, viewModel.whiteCardValueLink))
                    if (viewModel.blackCardValueIBAN != "" || viewModel.blackCardValue != "") UtilsObject.all_cards.add(Card("black", viewModel.blackCardValue, viewModel.blackCardValueIBAN, viewModel.blackCardValueLink))
                    if (viewModel.fopCardValueIBAN != "" || viewModel.FOPCardValue != "") UtilsObject.all_cards.add(Card(UtilsObject.fop_carta, viewModel.FOPCardValue, viewModel.fopCardValueIBAN))


                    cardState.value = false
                },
                enabled = if ( (viewModel.whiteCardValue != "" && viewModel.whiteCardValue.length == 16) || (viewModel.whiteCardValueIBAN != "" && viewModel.whiteCardValueIBAN.length == 29)) true else false            )
            {
                Text("Сохранить карту", fontFamily = fontLight)
            }
        },
        dismissButton = {
            Button(colors = ButtonColors(
                containerColor = MyMilkText,
                contentColor = MyBackground,
                disabledContentColor = Color.Magenta,
                disabledContainerColor = Color.DarkGray
            ),
                onClick = {
                    viewModel.setWhiteCards(viewModel.white_card_temp)
                    viewModel.setWhiteCardsIBAN(viewModel.white_card_IBAN_temp)
                    viewModel.setWhiteCardsLink(viewModel.white_card_link_temp)

                    cardState.value = false
                })
            {
                Text("Отмена", fontFamily = fontLight)
            }
        }
    )
}

@Composable
fun FopCardAlarmDialog(viewModel: StateViewModel, cardState: MutableState<Boolean>) {
    AlertDialog(modifier = Modifier, containerColor = MyBackground, onDismissRequest = {
        viewModel.setFOPCards(viewModel.fop_card_temp)
        viewModel.setFOPCardsIBAN(viewModel.fop_card_IBAN_temp)

        cardState.value = false
    }, text = {
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                "ФОП карта",
                color = MyMilkText,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontFamily = fontLight
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MyLightGrey)
            )
            Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                Text(
                    text =
                    buildAnnotatedString {
                        append("Укажите номер карты")

                    },
                    color = MyMilkText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = fontLight
                )
                TextFieldValidationCard(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = viewModel.FOPCardValue,
                    onChange = viewModel::setFOPCards,
                    placeholder = "", modifier = Modifier.fillMaxWidth(),
                    onClear = {
                        viewModel.setFOPCards("")
                    }
                )
            }
            Column(modifier = Modifier.padding(0.dp, 0.dp)) {
                Text(
                    text =
                    buildAnnotatedString {
                        append("Укажите IBAN карты")
                        withStyle(style = SpanStyle(color = MyRed, fontSize = 22.sp, fontWeight = FontWeight.Bold)) {
                            append(" *")
                        }
                    },
                    color = MyMilkText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = fontLight
                )
                TextFieldValidationCard(
                    value = viewModel.fopCardValueIBAN,
                    onChange = viewModel::setFOPCardsIBAN,
                    placeholder = "",
                    modifier = Modifier,
                    onClear = {
                        viewModel.setFOPCardsIBAN("")
                        //UtilsObject.fopCardIBAN = ""
                    })
            }
        }
    },
        confirmButton = {
            Button(colors = ButtonColors(
                containerColor = MyMilkText,
                contentColor = MyBackground,
                disabledContentColor = Color.DarkGray,
                disabledContainerColor = newBlue
            ),
                onClick = {

                    viewModel.fop_card_temp = viewModel.FOPCardValue
                    viewModel.fop_card_IBAN_temp = viewModel.fopCardValueIBAN


                    UtilsObject.all_cards.clear()
                    if (viewModel.whiteCardValueIBAN != "" || viewModel.whiteCardValue != "") UtilsObject.all_cards.add(Card("white", viewModel.whiteCardValue, viewModel.whiteCardValueIBAN, viewModel.whiteCardValueLink))
                    if (viewModel.blackCardValueIBAN != "" || viewModel.blackCardValue != "") UtilsObject.all_cards.add(Card("black", viewModel.blackCardValue, viewModel.blackCardValueIBAN, viewModel.blackCardValueLink))
                    if (viewModel.fopCardValueIBAN != "" || viewModel.FOPCardValue != "") UtilsObject.all_cards.add(Card(UtilsObject.fop_carta, viewModel.FOPCardValue, viewModel.fopCardValueIBAN))

                    cardState.value = false
                },
                enabled = if ( (viewModel.FOPCardValue != "" && viewModel.FOPCardValue.length == 16) && (viewModel.fopCardValueIBAN != "" && viewModel.fopCardValueIBAN.length == 29)) true else false
            )
            {
                Text("Сохранить карту", fontFamily = fontLight)
            }
        },
        dismissButton = {
            Button(colors = ButtonColors(
                containerColor = MyMilkText,
                contentColor = MyBackground,
                disabledContentColor = Color.Magenta,
                disabledContainerColor = Color.DarkGray
            ),
                onClick = {
                    viewModel.setFOPCards(viewModel.fop_card_temp)
                    viewModel.setFOPCardsIBAN(viewModel.fop_card_IBAN_temp)

                    cardState.value = false
                })
            {
                Text("Отмена", fontFamily = fontLight)
            }
        }
    )
}

@Composable
@Preview
fun CardAlarmDialogPreview() {
    val cardState = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {

    }
    BlackCardAlarmDialog(viewModel= viewModel(), cardState = cardState)
}