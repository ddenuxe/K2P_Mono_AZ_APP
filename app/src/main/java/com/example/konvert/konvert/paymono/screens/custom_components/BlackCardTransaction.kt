package com.example.konvert.konvert.paymono.screens.custom_components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.konvert.konvert.paymono.data.UpTrans
import com.example.konvert.konvert.paymono.getCorrectDay
import com.example.konvert.konvert.paymono.getCorrectTime
import com.example.konvert.konvert.paymono.isIbanTransaction
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight
import com.example.konvert.konvert.paymono.ui.theme.gradient1
import java.util.Calendar

@Composable
fun BlackCardTransaction(transaction: UpTrans, search: String) {

    var tempBalance = ""

    if (transaction.balance != null) {
        val tmp = transaction.balance / 100.0
        tempBalance = tmp.toString()
    }

    var resultTextTransaction_1 = ""
    var resultTextTransaction_2 = ""

    var resultTextDescription_1 = ""
    var resultTextDescription_2 = ""

    var resultTextTime_1 = ""
    var resultTextTime_2 = ""

    var resultTextDate_1 = ""
    var resultTextDate_2 = ""

    var resultTextBalance_1 = ""
    var resultTextBalance_2 = ""

    var resultTextCardName_1 = ""
    var resultTextCardName_2 = ""

    var resultTextCard_1 = ""
    var resultTextCard_2 = ""

    var resultTextIban_1 = ""
    var resultTextIban_2 = ""

    var searchedInTransaction = ""
    var searchedInDescription = ""
    var searchedInTime = ""
    var searchedInDate = ""
    var searchedInBalance = ""
    var searchedInCardName = ""
    var searchedInCard = ""
    var searchedInIban = ""


    if (transaction.description?.contains(search) == true) {
        searchedInDescription = search
    } else if (transaction.amount?.contains(search) == true) {
        searchedInTransaction = search
    } else if (transaction.time?.contains(search) == true) {
        searchedInTime = search
    } else if (transaction.date?.contains(search) == true) {
        searchedInDate = search
    } else if (tempBalance.contains(search) == true) {
        searchedInBalance = search
    } else if (transaction.type?.contains(search) == true) {
        searchedInCardName = search
    } else if (transaction.card?.contains(search) == true) {
        searchedInCard = search
    } else if (transaction.iban?.contains(search) == true) {
        searchedInIban = search
    }


    if (transaction.amount != null) {
        val temp = transaction.amount

        resultTextTransaction_1 = temp.substringBefore(searchedInTransaction)
        resultTextTransaction_2 = temp.substringAfter(searchedInTransaction)
    }

    if (transaction.description != null) {
        val temp = transaction.description

        resultTextDescription_1 = temp.substringBefore(searchedInDescription)
        resultTextDescription_2 = temp.substringAfter(searchedInDescription)
    }

    if (transaction.time != null) {
        val temp = transaction.time

        resultTextTime_1 = temp.substringBefore(searchedInTime)
        resultTextTime_2 = temp.substringAfter(searchedInTime)
    }

    if (transaction.date != null) {
        val temp = transaction.date

        resultTextDate_1 = temp.substringBefore(searchedInDate)
        resultTextDate_2 = temp.substringAfter(searchedInDate)
    }


    if (transaction.type != null) {
        val temp = transaction.type

        resultTextCardName_1 = temp.substringBefore(searchedInCardName)
        resultTextCardName_2 = temp.substringAfter(searchedInCardName)
    }



    if (transaction.card != null) {
        val temp = transaction.card

        resultTextCard_1 = temp.substringBefore(searchedInCard)
        resultTextCard_2 = temp.substringAfter(searchedInCard)
    }

    if (transaction.iban != null) {
        val temp = transaction.iban

        resultTextIban_1 = temp.substringBefore(searchedInIban)
        resultTextIban_2 = temp.substringAfter(searchedInIban)
    }

    resultTextBalance_1 = tempBalance.substringBefore(searchedInBalance)
    resultTextBalance_2 = tempBalance.substringAfter(searchedInBalance)

    Card(onClick = { }, modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        shape = RoundedCornerShape(7.dp),
        border = BorderStroke(2.dp, if (transaction.status) Color.Green else Color.Red)
//        colors = CardColors(
//            containerColor = newBlue,
//            contentColor = MyMilkText,
//            disabledContentColor = Color.Magenta,
//            disabledContainerColor = Color.Cyan
//        )
    ) {

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp).background(gradient1)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                Column(modifier = Modifier.weight(3f)) {
                    //Text(text = transaction.date ?: "", color = MyMilkText, fontFamily = fontLight, fontSize = 13.sp)
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MyMilkText)) {
                            if (transaction.date != null) {

                                append(resultTextDate_1)
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red)) {
                                    append(searchedInDate)
                                }
                                append(resultTextDate_2)

                            }
                        }

                    }, color = MyMilkText, fontFamily = fontLight, fontSize = 13.sp)
                    //Text(text = transaction.time ?: "00:00", fontWeight = FontWeight.Bold, color = MyMilkText, fontFamily = fontLight, fontSize = 13.sp)
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MyMilkText)) {
                            if (transaction.time != null) {

                                append(resultTextTime_1)
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red)) {
                                    append(searchedInTime)
                                }
                                append(resultTextTime_2)

                            }
                        }

                    }, color = MyMilkText, fontFamily = fontLight, fontSize = 13.sp)
                }

                Column(modifier = Modifier.weight(7f)) {

                        if (transaction.type != null) {


                                Text(text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = MyMilkText)) {
                                        append(resultTextCardName_1)
                                        withStyle(style = SpanStyle(color = Color.Red)) {
                                            append(searchedInCardName)
                                        }
                                        append(resultTextCardName_2)
                                    }

                                    if (isIbanTransaction(transaction)) {
                                        withStyle(style = SpanStyle(color = MyMilkText)) {

                                            append(" (" + resultTextIban_1)
                                            withStyle(style = SpanStyle(color = Color.Red)) {
                                                append(searchedInIban)
                                            }
                                            append(resultTextIban_2 + ") ")
                                        }
                                    } else {
                                        withStyle(style = SpanStyle(color = MyMilkText)) {

                                            append(" (" + resultTextCard_1)
                                            withStyle(style = SpanStyle(color = Color.Red)) {
                                                append(searchedInCard)
                                            }
                                            append(resultTextCard_2 + ") ")
                                        }
                                    }

                                }, color = MyMilkText, fontFamily = fontLight, fontSize = 13.sp)
                            }





                    Text(text = buildAnnotatedString {
                        append("Баланс: ")
//                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MyMilkText, fontFamily = fontLight)) {
//                            if (transaction.balance != null) {
//                                append((transaction.balance / 100.0).toString() + " грн")
//                            }
//                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MyMilkText)) {
                            append(resultTextBalance_1)
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red)) {
                                append(searchedInBalance)
                            }
                            append(resultTextBalance_2 + " грн")

                        }

                    }, color = MyMilkText, fontFamily = fontLight, fontSize = 13.sp)
                    Text(text = buildAnnotatedString {
                        append("Транзакция: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MyMilkText)) {
                            if (transaction.amount != null) {
                                if (transaction.amount.toDouble() > 0) {
                                    append("+")
                                }
                                append(resultTextTransaction_1)
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red)) {
                                    append(searchedInTransaction)
                                }
                                append(resultTextTransaction_2 + " грн")

                            }
                        }

                    }, color = MyMilkText, fontFamily = fontLight, fontSize = 13.sp)

                    if (transaction.description != null) {
                        Text(text = buildAnnotatedString {
                            append("Детали платежа: ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MyMilkText)) {
                                append(resultTextDescription_1)
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red)) {
                                    append(searchedInDescription)
                                }
                                append(resultTextDescription_2)
                            }

                        }, color = MyMilkText, fontFamily = fontLight, fontSize = 13.sp)
                    }

                }
            }
        }


    }
}


@Preview
@Composable
fun BlackCardPreview() {

    val transaction = UpTrans("asd", "1519211717","16.09.2024", "Від: Сирський Олександр Зарусню", 123, 123, "65.00", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", type = "mono black", card = "5375411422309984", iban = "UA5878768768767868")

    BlackCardTransaction(transaction,"")
}