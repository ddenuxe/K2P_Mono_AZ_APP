package com.example.konvert.konvert.paymono.screens.custom_components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight
import com.example.konvert.konvert.paymono.ui.theme.gradientRED

@Composable
fun ServerBankaDesignERROR(accountOrJar: JarAccount) {
    GradientButton(onClick = {  }, border = BorderStroke(1.dp, MyMilkText), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth().padding(0.dp, 4.dp), gradient = gradientRED, elevation = 1.dp ) {

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
                    painter = painterResource(R.drawable.banka),
                    contentDescription = "icon"
                )
                Text(
                    text = accountOrJar.card,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(5.dp), color = MyMilkText,
                    fontFamily = fontLight,
                    maxLines = 1
                )
            }

                Text(
                    text = "ERROR" ,
                    modifier = Modifier.padding(5.dp),
                    fontSize = 14.sp,
                    color = Color(0xFFE70202),
                    maxLines =  1,
                    fontFamily = fontLight,
                    fontWeight = FontWeight.Bold,

                    )
            }

    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun ServerBankaPreviewERROR() {
    Column(modifier = Modifier.background(MyBackground)) {
        val jar = JarAccount("5375411422309984", "5375411422309984", 123123,  23432423, "asdasdas", 3434, "asdasd", maskedPan = null, "asdasd", 234234, "asdasd", "asdasd", "5375411422309984")
        ServerBankaDesignERROR(jar)
    }
}