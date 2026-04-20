package com.example.konvert.konvert.paymono.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.konvert.konvert.paymono.MonobankService
import com.example.konvert.konvert.paymono.R
import com.example.konvert.konvert.paymono.UtilsObject
import com.example.konvert.konvert.paymono.ui.theme.Konvert2payMonoTheme
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyDarkGrey
import com.example.konvert.konvert.paymono.ui.theme.MyLightGrey
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight

@Composable
fun Screen1(goNext: () -> Unit) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MyBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier)
        
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween) {

            Image(painter = painterResource(id = R.drawable.img),
                contentDescription = "logo",
                modifier = Modifier.size(150.dp))
            Text(text = "Это приложение поможет настроить\n" +
                    "автоматизацию для Платформы\n" +
                    "Konvert2pay для Monobank",
                textAlign = TextAlign.Center,
                color = MyMilkText,
                modifier = Modifier.padding(20.dp))
        }


        Button(colors = ButtonDefaults.buttonColors(containerColor = MyMilkText), onClick = {
                goNext()
        }, modifier = Modifier.padding(30.dp, 15.dp, 30.dp, 100.dp).fillMaxWidth(),
            shape = RoundedCornerShape(15)
        ) { Text(text = "Приступить к настройке", color = MyBackground, fontFamily = fontLight, fontSize = 16.sp) }
    }
}


@Preview
@Composable
fun Screen1Preview() {
    Konvert2payMonoTheme {
        Screen1 ({ })
    }
}