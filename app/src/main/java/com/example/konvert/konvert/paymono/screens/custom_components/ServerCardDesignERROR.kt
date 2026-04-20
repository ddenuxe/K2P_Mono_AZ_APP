package com.example.konvert.konvert.paymono.screens.custom_components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.konvert.konvert.paymono.R
import com.example.konvert.konvert.paymono.data.JarAccount
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight
import com.example.konvert.konvert.paymono.ui.theme.gradientRED

@Composable
fun ServerCardDesignERROR(accountOrJar: JarAccount) {

    GradientButton(
        elevation = 1.dp,
        gradient = gradientRED,
        contentColor = Color(0xFFC2C2C2),
        onClick = { },
        modifier = Modifier
            .padding(0.dp, 4.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(0.dp),
                    painter = painterResource(R.drawable.card),
                    contentDescription = "icon"
                )
                Text(
                    text = accountOrJar.card,
                    fontSize = 12.sp,
                    maxLines = 1, fontFamily = fontLight,
                    modifier = Modifier.padding(5.dp), color = MyMilkText
                )
            }

                Text(
                    text = "ERROR",
                    modifier = Modifier.padding(5.dp),
                    fontSize = 14.sp,
                    maxLines = 1,
                    color = Color(0xFFE70202),
                    fontFamily = fontLight,
                    fontWeight = FontWeight.Bold,
                )
            }

    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun ServerDesignPreviewERROR() {
    val account = JarAccount("5375411422309984", "5375411422309984", 123123,  23432423, "asdasdas", 3434, "asdasd", maskedPan = null, "asdasd", null, "asdasd", "asdasd", "5375411422309984")

    ServerCardDesignERROR(account)
}