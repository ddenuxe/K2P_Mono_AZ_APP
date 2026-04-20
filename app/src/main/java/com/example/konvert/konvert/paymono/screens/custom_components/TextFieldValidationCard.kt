package com.example.konvert.konvert.paymono.screens.custom_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight


@Composable
@JvmName("functionOfKotlin")
fun TextFieldValidationCard(
    value: String,
    placeholder: String = "",
    onChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = "",
    onClear: () -> Unit
) {

//    BackHandler {
//
//    }

    Column(horizontalAlignment = Alignment.Start, modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (!it.contains("\n"))
                    onChange(it)
            },
            placeholder = { Text(text = placeholder, Modifier.alpha(0.16f), fontFamily = fontLight) },
            singleLine = true,
            textStyle = TextStyle(color = MyMilkText, fontFamily = fontLight),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = Color.Gray,
                unfocusedBorderColor = MyMilkText,
                focusedTextColor = MyMilkText,
                focusedBorderColor = MyMilkText,
                errorBorderColor = Color.Red,
                unfocusedContainerColor = MyBackground,
                focusedContainerColor = MyBackground,
                unfocusedPlaceholderColor = MyMilkText,
                errorTrailingIconColor = Color.Red
            ),
            shape = RoundedCornerShape(10.dp),
            isError = isError,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    tint = MyMilkText,
                    contentDescription = "Back button",
                    modifier = Modifier.clickable {
                        onClear()
                    }
                )
            }

        )
        if (isError){
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 2.dp),
                textAlign = TextAlign.Start, fontFamily = fontLight
            )
        }

    }
}


@Preview
@Composable
fun TextFieldValidationPreview() {
    TextFieldValidationCard(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        value = "1212313123234234",
        onChange = { },
        placeholder = "", modifier = Modifier.fillMaxWidth(),
        onClear = {

        },
//        isError = true,
//        errorMessage = "*Required field"
    )
}