package com.example.konvert.konvert.paymono.screens.screen_user_id

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.konvert.konvert.paymono.R
import com.example.konvert.konvert.paymono.screens.StateViewModel
import com.example.konvert.konvert.paymono.screens.custom_components.TextFieldValidationCard
import com.example.konvert.konvert.paymono.ui.theme.Konvert2payMonoTheme
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight

@Composable
fun ScreenUserId(goNext: () -> Unit, goBack: () -> Unit, viewModel: StateViewModel = viewModel()) {

    val localContext = LocalContext.current
    val shared = localContext.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MyBackground).imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
            .fillMaxWidth().weight(1f)
            .padding(30.dp, 30.dp, 0.dp, 0.dp)) {

            IconButton(onClick = { goBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.circle_left),
                    contentDescription = "back button",
                    modifier = Modifier.size(35.dp),
                    tint = MyMilkText)
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.weight(5f)) {
            Text(text = "User ID",
                textAlign = TextAlign.Center,
                color = MyMilkText,
                modifier = Modifier.padding(20.dp),
                fontWeight = FontWeight.Bold)
            Image(painter = painterResource(id = R.drawable.img),
                contentDescription = "logo",
                modifier = Modifier.size(150.dp))

            Text(text = "Для корректной работы приложения\n" +
                    "необходимо указать ваш User ID. \n" +
                    "User ID вы сможете найти во вкладке  \n" +
                    "«Настройки АЗ» на Платформе,\n" +
                    "в правом верхнем углу. ",
                textAlign = TextAlign.Center,
                color = MyMilkText,
                modifier = Modifier)


        }

        Column(modifier = Modifier.fillMaxWidth().weight(3f).padding(0.dp, 20.dp)) {
            TextFieldValidationCard(
                value = viewModel.userValue,
                onChange = viewModel::setUserId,
                placeholder = "User ID",
                errorMessage = viewModel.userError,
                isError = viewModel.userError.isNotEmpty(),
                modifier = Modifier.padding(30.dp, 0.dp).height(50.dp), onClear = { viewModel.setUserId("") }
            )

            Button(colors = ButtonDefaults.buttonColors(containerColor = MyMilkText), onClick = {
                viewModel.validateUserId()
                shared.edit().putString("user_id", viewModel.userValue).apply()
                if (viewModel.userValue.isNotEmpty()) {
                    goNext()
                }
            }, modifier = Modifier
                .padding(30.dp, 30.dp, 30.dp, 0.dp)
                .fillMaxWidth(),
                shape = RoundedCornerShape(15)
            ) { Text(text = "Подтвердить", color = MyBackground, fontFamily = fontLight, fontSize = 16.sp) }
        }


    }
}


@Preview
@Composable
fun ScreenUserIdPreview() {
    Konvert2payMonoTheme {
        ScreenUserId ({ }, { })
    }
}