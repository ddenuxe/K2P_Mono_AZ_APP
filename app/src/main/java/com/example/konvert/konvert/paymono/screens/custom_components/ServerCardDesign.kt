package com.example.konvert.konvert.paymono.screens.custom_components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.konvert.konvert.paymono.R
import com.example.konvert.konvert.paymono.UtilsObject
import com.example.konvert.konvert.paymono.data.JarAccount
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyBlue
import com.example.konvert.konvert.paymono.ui.theme.MyLightGrey
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.MyWhiteText
import com.example.konvert.konvert.paymono.ui.theme.fontLight
import com.example.konvert.konvert.paymono.ui.theme.gradient1
import com.example.konvert.konvert.paymono.ui.theme.gradient2
import com.example.konvert.konvert.paymono.ui.theme.gradientFopCard
import kotlin.math.roundToInt

@Composable
fun ServerCardDesign(
    accountOrJar: JarAccount,
    loader: MutableState<Boolean>,
    delay: MutableState<Long>,
    //cards: MutableList<String>
) {

   // Log.e("MESSI CARDS: ", cards.toString() )
   // Log.e("MESSI 1 CARD: ", accountOrJar.card.toString() )

//    val timer = object : TimerTask() {
//        override fun run() {
//            Log.e("MESSI TIMER OFF", "jhg" )
//        }
//    }
//
//    var myTimer = remember { timer }

//    val ticks by remember { mutableStateOf(delay) }
//
//    LaunchedEffect(Unit) {
//        while(true) {
//            delay(1.seconds)
//            ticks.value -= 1000
//        }
//    }

    GradientButtonWithoutPadding(
        onClick = { },
        modifier = Modifier
            .padding(0.dp, 4.dp)
            .fillMaxWidth(),
        elevation = 1.dp,
        gradient = if (accountOrJar.type == "black") gradient1 else if (accountOrJar.type == UtilsObject.fop_carta) gradientFopCard else gradient2
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(16.dp, 2.dp, 16.dp, 2.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(0.dp),
                    painter = if (accountOrJar.type == "black") painterResource(R.drawable.card) else painterResource(id = R.drawable.white),
                    contentDescription = "icon",
                )
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier) {
                    Text(
                        text = accountOrJar.card,
                        fontSize = 12.sp,
                        maxLines = 1, fontFamily = fontLight,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp), color = if (accountOrJar.type == "black") MyMilkText else if (accountOrJar.type == UtilsObject.fop_carta) MyMilkText else MyBackground
                    )
                    Text(
                        text = getCardName(accountOrJar.type),
                        fontSize = 12.sp,
                        maxLines = 1, fontFamily = fontLight,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp), color = if (accountOrJar.type == "black") MyLightGrey else if (accountOrJar.type == UtilsObject.fop_carta) MyWhiteText else MyBlue
                    )
                }

            }
            Text(
                    text = (accountOrJar.balance / 100.0 + 0.00).toString(),
                    modifier = Modifier.padding(5.dp),
                    fontSize = 14.sp,
                    maxLines = 1,
                    color = if (accountOrJar.type == "black") MyMilkText else if (accountOrJar.type == UtilsObject.fop_carta) MyMilkText else MyBackground,
                    fontFamily = fontLight
            )
        }
    }
}


fun getCardName(card: String?): String {

    return when (card) {
        "black" -> "Черная карта"
        "white" -> "Белая карта"
        "platinum" -> "Платиновая карта"
        "iron" -> "Железная карта"
        "fop" -> "ФОП карта"
        "yellow" -> "Желтая карта"
        "eAid" -> "eAid карта"
        "madeInUkraine" -> "ФОП карта"
        else -> "Карта"
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun ServerDesignPreview() {
    val account = JarAccount("5375411422309984", "5375411422309984", 123123,  23432423, UtilsObject.fop_carta, 3434, "asdasd", maskedPan = null, "asdasd", null, "asdasd", "asdasd", "5375411422309984")

    ServerCardDesign(
        account,
        mutableStateOf(true),
        mutableStateOf(32343),
        //UtilsObject.all_cards_ERRORS
    )
}