package com.example.konvert.konvert.paymono.screens.screen_token

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.konvert.konvert.paymono.R
import com.example.konvert.konvert.paymono.screens.StateViewModel
import com.example.konvert.konvert.paymono.screens.custom_components.TextFieldValidationCard
import com.example.konvert.konvert.paymono.ui.theme.Konvert2payMonoTheme
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight

@Composable
fun ScreenToken(goNext: () -> Unit, viewModel: StateViewModel = viewModel()) {

    val context = LocalContext.current

    val shared = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MyBackground).imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.weight(5f)) {
            Text(text = "Monobank Token",
                textAlign = TextAlign.Center,
                color = MyMilkText,
                modifier = Modifier.padding(50.dp, 50.dp, 50.dp, 0.dp),
                fontWeight = FontWeight.Bold)
            Image(painter = painterResource(id = R.drawable.img),
                contentDescription = "logo",
                modifier = Modifier.size(150.dp))

            Text(text = "Для корректной работы приложения\n" +
                    "необходимо предоставить доступ к\n" +
                    " Monobank Token",
                textAlign = TextAlign.Center,
                color = MyMilkText,
                modifier = Modifier)
            Button(colors = ButtonDefaults.buttonColors(containerColor = MyMilkText), onClick = {
                val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.monobank.ua/index.html"))
                startActivity(context, browseIntent, null)
            }, modifier = Modifier
                .padding(30.dp, 15.dp, 30.dp, 0.dp)
                .fillMaxWidth(),
                shape = RoundedCornerShape(15)
            ) { Text(text = "Получить Monobank Token", color = MyBackground, fontFamily = fontLight, fontSize = 16.sp) }


        }

        Column(modifier = Modifier.fillMaxWidth().weight(3f).padding(0.dp, 20.dp)) {
            TextFieldValidationCard(
                value = viewModel.tokenValue,
                onChange = viewModel::setTokenName,
                placeholder = "Monobank Token",
                errorMessage = viewModel.tokenError,
                isError = viewModel.tokenError.isNotEmpty(),
                modifier = Modifier.padding(30.dp, 0.dp).height(50.dp),
                onClear = { viewModel.setTokenName("") }
            )


            Button(colors = ButtonDefaults.buttonColors(containerColor = MyMilkText), onClick = {
                viewModel.validateToken()
                shared.edit().putString("token", viewModel.tokenValue).apply()
                if (viewModel.tokenValue.isNotEmpty()) {
                    goNext()
                }
            }, modifier = Modifier
                .padding(30.dp, 10.dp, 30.dp, 0.dp)
                .fillMaxWidth(),
                shape = RoundedCornerShape(15)
            ) { Text(text = "Подтвердить", color = MyBackground, fontFamily = fontLight, fontSize = 16.sp) }
        }


    }
}


@Preview
@Composable
fun ScreenTokenPreview() {
    Konvert2payMonoTheme {
        ScreenToken ({ })
    }
}