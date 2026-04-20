package com.example.konvert.konvert.paymono.screens.custom_components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.konvert.konvert.paymono.R
import com.example.konvert.konvert.paymono.data.JarAccount
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.MyWhiteText
import com.example.konvert.konvert.paymono.ui.theme.fontLight
import kotlin.math.roundToInt

@Composable
fun ServerBankaDesign(accountOrJar: JarAccount, loader: MutableState<Boolean>, delay: MutableState<Long>) {
    OutlinedButton(onClick = {  }, border = BorderStroke(1.dp, MyMilkText), shape = RoundedCornerShape(10.dp), modifier = Modifier.padding(0.dp, 4.dp).fillMaxWidth(), contentPadding = PaddingValues(0.dp)) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(16.dp, 2.dp, 16.dp, 2.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(0.dp),
                    painter = painterResource(R.drawable.banka),
                    contentDescription = "icon"
                )
                Column {
                    Text(
                        text = accountOrJar.card,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(10.dp, 0.dp, 0.dp ,0.dp), color = MyMilkText,
                        fontFamily = fontLight,
                        lineHeight = 18.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "${accountOrJar.title}",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(10.dp, 0.dp, 0.dp ,0.dp), color = MyWhiteText,
                        fontFamily = fontLight,
                        maxLines = 1
                    )
                }

            }
                Text(
                    text = (accountOrJar.balance / 100.0 + 0.00).toString(),
                    modifier = Modifier.padding(5.dp),
                    fontSize = 14.sp,
                    color = MyMilkText,
                    maxLines = 1,
                    lineHeight = 18.sp,
                    fontFamily = fontLight
                )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun ServerBankaPreview() {
    Column(modifier = Modifier.background(MyBackground)) {
        val jar = JarAccount("5375411422309984", "5375411422309984", 123123,  23432423, "asdasdas", 3434, "asdasd", maskedPan = null, "asdasd", 234234, "asdasd", "asdasd", "5375411422309984")
        ServerBankaDesign(jar, mutableStateOf(true), mutableStateOf(32343))
    }
}