package com.example.konvert.konvert.paymono.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.konvert.konvert.paymono.MonobankService
import com.example.konvert.konvert.paymono.R
import com.example.konvert.konvert.paymono.UtilsObject
import com.example.konvert.konvert.paymono.data.UpTrans
import com.example.konvert.konvert.paymono.getCorrectDay
import com.example.konvert.konvert.paymono.getCorrectTime
import com.example.konvert.konvert.paymono.screens.custom_components.BlackCardTransaction
import com.example.konvert.konvert.paymono.screens.custom_components.FopCardTransaction
import com.example.konvert.konvert.paymono.screens.custom_components.SimpleTransaction
import com.example.konvert.konvert.paymono.screens.custom_components.WhiteCardTransaction
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight
import com.example.konvert.konvert.paymono.ui.theme.newBlue
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSelectionMode
import my.nanihadesuka.compose.ScrollbarSettings
import java.text.SimpleDateFormat
import java.util.Calendar


@SuppressLint("SuspiciousIndentation")
@Composable
fun ScreenJournal(goBack: () -> Unit, monobankService: MonobankService) {

    val textState = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val listState = rememberLazyListState()


//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", type = "black", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211717, "asd", 123, 123, "65.00", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", type = "white", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "1.00", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "ashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgd", 123, 123, "999.99", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "2342.34", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "3.42", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "23.23", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "435.54", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "57.57", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "68.58", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "54.56", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "634.53", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "345.45", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "345.34", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    monobankService.listOfTransaction.addAll(mutableListOf(UpTrans("asd", 1519211809, "asd", 123, 123, "4.56", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//


    Column(modifier = Modifier
        .fillMaxSize()
        //.verticalScroll(rememberScrollState())
        .background(MyBackground)
        .padding(10.dp, 0.dp, 5.dp, 10.dp)
        .imePadding(),
        verticalArrangement = Arrangement.SpaceBetween) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .weight(2f)
            .padding(20.dp, 0.dp)) {

            IconButton(onClick = { goBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.circle_left),
                    contentDescription = "back button",
                    modifier = Modifier.size(35.dp),
                    tint = MyMilkText
                )
            }

            Image(
                painter = painterResource(id = R.drawable.logo_upd),
                contentDescription = "logo",
                modifier = Modifier.size(45.dp)
            )

        }

        val searchedText = textState.value.text

        //val date = SimpleDateFormat("dd.MM.yyyy").parse("16.09.2024")

                LazyColumn(
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 0.dp)
                        .weight(13f)
                        .simpleVerticalScrollbar(listState),
                    state = listState,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    items(monobankService.listOfTransaction.filter {

                        it.amount.toString().contains(searchedText) ||
                                it.description.toString().contains(searchedText) ||
                                it.time.toString().contains(searchedText) ||
                                it.date.toString().contains(searchedText) ||
                                it.balance.toString().contains(searchedText) ||
                                it.bankaTitle.toString().contains(searchedText) ||
                                it.card.toString().contains(searchedText) ||
                                it.type.toString().contains(searchedText)
                    }.sortedBy { it.date?.let { it1 ->

                        SimpleDateFormat("dd.MM.yyyy HH:mm").parse(
                            it1 + " " + it.time
                        )
                    } }) { transaction ->
                        //Log.e("MESSI TRANSASCTION: ", transaction.toString())
                        when (transaction.type) {
                            "mono black" -> BlackCardTransaction(
                                transaction = transaction,
                                search = searchedText
                            )

                            "mono white" -> WhiteCardTransaction(
                                transaction = transaction,
                                search = searchedText
                            )

                            "mono ${UtilsObject.fop_carta}" -> FopCardTransaction(
                                transaction = transaction,
                                search = searchedText
                            )

                            else -> SimpleTransaction(
                                transaction = transaction,
                                search = searchedText
                            )
                        }

                    }
                }


        Box(modifier = Modifier
            .weight(2f),
             contentAlignment = Alignment.Center
        ) {
            SearchView(textState = textState, modifier = Modifier
                .fillMaxWidth())
        }


    }
}

@Composable
fun SearchView(textState: MutableState<TextFieldValue>, modifier: Modifier) {

    Column(modifier) {
        OutlinedTextField(
            value = textState.value,
            onValueChange = { value ->
                textState.value = value
            },
            singleLine = true,
            placeholder = { Text(text = "Найти", modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 50.dp, 0.dp), textAlign = TextAlign.Center, fontFamily = fontLight
            ) },
            textStyle = TextStyle(color = MyMilkText, fontFamily = fontLight),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = MyMilkText,
                focusedBorderColor = MyMilkText,
                unfocusedBorderColor = MyMilkText,
                focusedTextColor = MyMilkText,
                unfocusedTextColor = MyMilkText,
                focusedPlaceholderColor = Color.Transparent,
                unfocusedPlaceholderColor = MyMilkText),
            shape = RoundedCornerShape(7.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = MyMilkText,
                    contentDescription = "Back button",
                    modifier = Modifier.clickable {
                    }
                ) })
    }

}



@Composable
fun Modifier.simpleVerticalScrollbar(
    state: LazyListState,
    width: Dp = 8.dp
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )

    return drawWithContent {
        drawContent()

        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

        // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
        if (needDrawScrollbar && firstVisibleElementIndex != null) {
            val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
            val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
            val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

            drawRect(
                color = Color.White,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha
            )
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun ScreenJournalPreview() {
    val service = MonobankService()

    //val list = mutableStateListOf<UpTrans>()
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "12:23", "16.09.2323","asd123", 123, 123, "123.32", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", type = "mono black", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "13:00","16.09.2323", "asd", 123, 123, "65.00", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", type = "mono white", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "14:12","6.5.2323", "asd", 123, 123, "1.00", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", type = "mono white", bankaTitle = "Na futbol", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "00:00", "01.12.2024","ashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgdashgfgfhfhgfhghjgjhgd", 123, 123, "999.99", 2323, 2323, 2323, 2323, 2323, false, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", "asdasd")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "02:00","6.5.2323", "asd", 123, 123, "2342.34", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", type = "mono white", bankaTitle = "Na futbol", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "05:56", "6.5.2323","asd", 123, 123, "3.42", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "22:22","6.5.2323", "asd", 123, 123, "23.23", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", type = "mono white", bankaTitle = "Na futbol", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "23:32", "6.5.2323","asd", 123, 123, "435.54", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "11:00","6.5.2323", "asd", 123, 123, "57.57", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", type = "mono white", bankaTitle = "Na futbol", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "00:00","6.5.2323", "asd", 123, 123, "68.58", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "00:00", "6.5.2323","asd", 123, 123, "54.56", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", type = "mono white", bankaTitle = "Na futbol", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "00:00", "6.5.2323","asd", 123, 123, "634.53", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "00:00", "6.5.2323","asd", 123, 123, "345.45", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", type = "mono black", bankaTitle = "Na futbol", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "00:00","6.5.2323", "asd", 123, 123, "345.34", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
//    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "00:00","6.5.2323", "asd", 123, 123, "4.56", 2323, 2323, 2323, 2323, 2323, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", bankaTitle = "Na futbol", card = "5375411422309984")))
    service.listOfTransaction.addAll(mutableListOf(UpTrans("asd", "23:59", "6.5.2323","asd", 123, 123, "0.01", 2723, 2323, 2323, 2323, 2723, false, null, "asdasd", "asdasd", "asdasd", "asdasd", "asdasd", type = "mono ${UtilsObject.fop_carta}", bankaTitle = "Na futbol", card = "5375411422309984")))


    ScreenJournal( goBack = {}, service)
}