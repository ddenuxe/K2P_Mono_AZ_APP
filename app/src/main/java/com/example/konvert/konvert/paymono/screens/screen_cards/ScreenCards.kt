package com.example.konvert.konvert.paymono.screens.screen_cards

import android.content.Context
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
import com.example.konvert.konvert.paymono.ui.theme.gradient1
import com.example.konvert.konvert.paymono.ui.theme.gradient2
import com.example.konvert.konvert.paymono.ui.theme.gradientFopCard
import com.example.konvert.konvert.paymono.ui.theme.gradientGreyCard
import com.example.konvert.konvert.paymono.ui.theme.gradientWhiteCard
import com.example.konvert.konvert.paymono.ui.theme.newBlue
import com.example.konvert.konvert.paymono.ui.theme.textGrey
import kotlinx.coroutines.launch

@Composable
fun ScreenCards(goNext: () -> Unit, goBack: () -> Unit, viewModel: StateViewModel = viewModel()) {

    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    val showBlackCard = remember { mutableIntStateOf(if (viewModel.blackCardValueIBAN.isNotEmpty()) 2 else 0) }
    val showWhiteCard = remember { mutableIntStateOf(if (viewModel.whiteCardValueIBAN.isNotEmpty()) 2 else 0) }
    val showFOPCard = remember { mutableIntStateOf(if (viewModel.fopCardValueIBAN.isNotEmpty()) 2 else 0) }

    val cardState_White = remember { mutableStateOf(false) }
    val cardState_Black = remember { mutableStateOf(false) }
    val cardState_FOP = remember { mutableStateOf(false) }



    val localContext = LocalContext.current
    val shared = localContext.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)


    if (cardState_Black.value) {
        BlackCardAlarmDialog(viewModel = viewModel, cardState = cardState_Black)
    }

    if (cardState_White.value) {
        WhiteCardAlarmDialog(viewModel = viewModel, cardState = cardState_White)
    }

    if (cardState_FOP.value) {
        FopCardAlarmDialog(viewModel = viewModel, cardState = cardState_FOP)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MyBackground)
            .imePadding()
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (!it.hasFocus) {
                    coroutineScope.launch { focusRequester.requestFocus() }
                }
            }
            .noRippleClickable {
                if (viewModel.blackCardValue.isEmpty()) {
                    showBlackCard.intValue = 0
                } else {
                    showBlackCard.intValue = 2
                }
                if (viewModel.whiteCardValue.isEmpty()) {
                    showWhiteCard.intValue = 0
                } else {
                    showWhiteCard.intValue = 2
                }
                if (viewModel.FOPCardValue.isEmpty()) {
                    showFOPCard.intValue = 0
                } else {
                    showFOPCard.intValue = 2
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
            .fillMaxWidth()
            .weight(2f)
            .padding(30.dp, 30.dp, 0.dp, 0.dp)) {
            IconButton(onClick = { goBack() }) { Icon(painter = painterResource(id = R.drawable.circle_left), contentDescription = "back button", modifier = Modifier.size(35.dp), tint = MyMilkText) }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.weight(4f)) {
            Text(text = "Cards",
                textAlign = TextAlign.Center,
                color = MyMilkText,
                modifier = Modifier.padding(0.dp),
                fontWeight = FontWeight.Bold)
            Image(painter = painterResource(id = R.drawable.logo_upd),
                contentDescription = "logo",
                modifier = Modifier.size(150.dp))

            Text(text = "На этом экране вы можете добавить\n" +
                    "необходимые реквизиты.",
                textAlign = TextAlign.Center,
                color = MyMilkText,
                modifier = Modifier)


        }


        Column(verticalArrangement = Arrangement.Top, modifier = Modifier
            .padding(30.dp, 0.dp)
            .weight(4f)) {

            if (viewModel.blackCardValue.isEmpty() && viewModel.blackCardValue.isEmpty()) {

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

        if (viewModel.whiteCardValue.isEmpty() && viewModel.whiteCardValue.isEmpty()) {

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

            OutlinedButton(onClick = { cardState_White.value = true }, border = BorderStroke(1.dp, MyMilkText), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    contentColor = MyMilkText,
                    containerColor = MyMilkText,
                    disabledContentColor = Color.DarkGray,
                    disabledContainerColor = Color.LightGray
                ),) {

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
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


        if (viewModel.FOPCardValue.isEmpty() || viewModel.fopCardValueIBAN.isEmpty()) {

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

            OutlinedButton(onClick = { cardState_FOP.value = true }, border = BorderStroke(1.dp, MyMilkText), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    contentColor = Color(0xFF519196),
                    containerColor = Color(0xFF5fc7cf),
                    disabledContentColor = Color.DarkGray,
                    disabledContainerColor = Color.LightGray
                )) {

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
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

        Column(modifier = Modifier
            .fillMaxWidth()
            .weight(2f)
            .padding(0.dp, 0.dp)) {

            Button(colors = ButtonDefaults.buttonColors(containerColor = MyMilkText), onClick = {
                shared.edit().putString("black_card", viewModel.blackCardValue).apply()
                shared.edit().putString("white_card", viewModel.whiteCardValue).apply()
                shared.edit().putString("fop_card", viewModel.FOPCardValue).apply()
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
fun ScreenCardsPreview() {
    Konvert2payMonoTheme {

        ScreenCards ({ }, { })
    }
}