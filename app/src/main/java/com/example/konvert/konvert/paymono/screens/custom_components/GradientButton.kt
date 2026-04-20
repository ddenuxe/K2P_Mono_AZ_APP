package com.example.konvert.konvert.paymono.screens.custom_components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.konvert.konvert.paymono.R
import com.example.konvert.konvert.paymono.data.JarAccount
import com.example.konvert.konvert.paymono.ui.theme.MyBackground
import com.example.konvert.konvert.paymono.ui.theme.MyMilkText
import com.example.konvert.konvert.paymono.ui.theme.fontLight
import com.example.konvert.konvert.paymono.ui.theme.gradient1
import com.example.konvert.konvert.paymono.ui.theme.gradient2


@Composable
fun GradientButton(
    onClick: () -> Unit,
    gradient: Brush,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: Dp,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0f),
    disabledBackgroundColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        .compositeOver(MaterialTheme.colorScheme.surface),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val actualContentColor = if (enabled) contentColor else disabledContentColor
    val background = if (enabled) {
        Modifier.background(brush = gradient)
    } else {
        Modifier.background(color = disabledBackgroundColor)
    }

    Surface(
        modifier = modifier
            .shadow(elevation, shape, true)
            .then(background),
        shape = shape,
        color = Color.Transparent,
        contentColor = actualContentColor.copy(alpha = 1f),
        border = border,
        shadowElevation = 0.dp,
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        CompositionLocalProvider(LocalContentColor provides actualContentColor) {
            ProvideTextStyle(
                value = MaterialTheme.typography.bodyMedium
            ) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .padding(contentPadding)
                            ,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun GradientPreview() {
    val accountOrJar = JarAccount("asdasd", "", 5555,  23432423, "black", 980, "asdasd", maskedPan = null, "asdasd", null, "asdasd", title = null, "5375411422309984")

    GradientButton(
        elevation = 1.dp,
        gradient = if (accountOrJar.type == "black") gradient1 else gradient2,
        onClick = { },
        modifier = Modifier
            .padding(0.dp, 4.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp, 16.dp, 0.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(0.dp),
                    painter = if (accountOrJar.type == "black") painterResource(R.drawable.card) else painterResource(id = R.drawable.white),
                    contentDescription = "icon",
                )
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier) {
                    Text(
                        text = accountOrJar.card,
                        fontSize = 12.sp,
                        maxLines = 1, fontFamily = fontLight,
                        modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp), color = if (accountOrJar.type == "black") MyMilkText else MyBackground
                    )
                    Text(
                        text = getCardName(accountOrJar.type),
                        fontSize = 12.sp,
                        maxLines = 1, fontFamily = fontLight,
                        modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp), color = if (accountOrJar.type == "black") MyMilkText else MyBackground
                    )
                }

            }
            Text(
                text = (accountOrJar.balance / 100.0 + 0.00).toString(),
                modifier = Modifier.padding(5.dp),
                fontSize = 14.sp,
                maxLines = 1,
                color = if (accountOrJar.type == "black") MyMilkText else MyBackground,
                fontFamily = fontLight
            )
        }
    }
}