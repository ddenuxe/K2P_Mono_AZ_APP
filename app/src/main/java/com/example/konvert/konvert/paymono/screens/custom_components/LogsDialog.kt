package com.example.konvert.konvert.paymono.screens.custom_components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.konvert.konvert.paymono.getCurrentDate
import com.example.konvert.konvert.paymono.ui.theme.LogsColor
import com.example.konvert.konvert.paymono.ui.theme.MyRed

@Composable
fun LogsDialog(logs: MutableState<List<String>>, dialogState: MutableState<Boolean>) {
    AlertDialog(onDismissRequest = { dialogState.value = false }, modifier = Modifier
        .size(300.dp, 600.dp), confirmButton = {}, dismissButton = {}, tonalElevation = 3.dp, containerColor = Color.Black.copy(alpha = 0.6f), text = {
        LazyColumn {
            items(logs.value) { logText ->
                Text(
                    text = logText,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 10.sp,
                    color = if(logText.contains("FAIL")) Color.Red else if (logText.contains("СТАРТ")) Color.Magenta else LogsColor,
                    fontFamily = FontFamily.Monospace,

                )
            }
        }
    })
}


@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun LogsDialogPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        val cardState = remember { mutableStateOf(false) }

        LogsDialog(mutableStateOf(listOf("${getCurrentDate()}|Хуйня з ТРАНЗАКЦІЄЮ е=EXCEPTION|FAIL", "|type+|iban+|card+| ")), cardState)
    }

}