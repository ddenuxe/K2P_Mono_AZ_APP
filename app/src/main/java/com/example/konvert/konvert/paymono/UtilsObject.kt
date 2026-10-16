package com.example.konvert.konvert.paymono

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.example.konvert.konvert.paymono.data.Card
import com.example.konvert.konvert.paymono.data.ClientInfo
import com.example.konvert.konvert.paymono.data.JarAccount
import com.example.konvert.konvert.paymono.data.MyTransaction
import com.example.konvert.konvert.paymono.data.UpTrans
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


object UtilsObject {
    var token_monobank = ""
    //var pre_account_numbers = mutableListOf<String>()
    var server_name = ""
    //var server_ping = ""

    var device_id = ""
    var user_id = ""
    var startedTimestamp = 0L

    var all_cards = mutableSetOf<Card>()
    var all_jars_cards = mutableMapOf<String, String>()

    var suka = false

//    var whiteCardNumber = ""
//    var blackCardNumber = ""
//    var fopCardNumber = ""
 //   var all_cards_ERRORS = mutableListOf<String>()

//    var whiteCardIBAN = ""
//    var blackCardIBAN = ""
//    var fopCardIBAN = ""

//    var whiteCardLink = ""
//    var blackCardLink = ""

    var fop_carta =
        "fop"
        //"madeInUkraine"

}

//fun getCorrectData(calendar: Calendar) : String {
//    var finalString = ""
//    if (calendar.get(Calendar.DAY_OF_MONTH) < 10) { finalString += "0${calendar.get(Calendar.DAY_OF_MONTH)}." } else { finalString += "${calendar.get(Calendar.DAY_OF_MONTH)}." }
//    if (calendar.get(Calendar.MONTH) < 10){ finalString += "0${calendar.get(Calendar.MONTH)+1}." } else { finalString += "${calendar.get(Calendar.MONTH)+1}." }
//    finalString += "${calendar.get(Calendar.YEAR)} | "
//    if (calendar.get(Calendar.HOUR_OF_DAY) < 10){ finalString += "0${calendar.get(Calendar.HOUR_OF_DAY)}:" } else { finalString += "${calendar.get(Calendar.HOUR_OF_DAY)}:" }
//    if (calendar.get(Calendar.MINUTE) < 10){ finalString += "0${calendar.get(Calendar.MINUTE)}" } else { finalString += "${calendar.get(Calendar.MINUTE)}" }
//   return finalString
//}

fun getCorrectDay(calendar: Calendar) : String {
    var finalString = ""
    if (calendar.get(Calendar.DAY_OF_MONTH) < 10) { finalString += "0${calendar.get(Calendar.DAY_OF_MONTH)}." } else { finalString += "${calendar.get(Calendar.DAY_OF_MONTH)}." }
    if (calendar.get(Calendar.MONTH) < 9){ finalString += "0${calendar.get(Calendar.MONTH)+1}." } else { finalString += "${calendar.get(Calendar.MONTH)+1}." }
    finalString += "${calendar.get(Calendar.YEAR)}"
    return finalString
}

fun getCorrectTime(calendar: Calendar) : String {
    var finalString = ""
    if (calendar.get(Calendar.HOUR_OF_DAY) < 10){ finalString += "0${calendar.get(Calendar.HOUR_OF_DAY)}:" } else { finalString += "${calendar.get(Calendar.HOUR_OF_DAY)}:" }
    if (calendar.get(Calendar.MINUTE) < 10){ finalString += "0${calendar.get(Calendar.MINUTE)}:" } else { finalString += "${calendar.get(Calendar.MINUTE)}:" }
    if (calendar.get(Calendar.SECOND) < 10){ finalString += "0${calendar.get(Calendar.SECOND)}" } else { finalString += "${calendar.get(Calendar.SECOND)}" }

    return finalString
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun isIbanTransaction(upTrans: UpTrans): Boolean {
    val desc = upTrans.description ?: return false

    return desc.startsWith("Від:") ||
            desc.startsWith("Вiд:") ||
            (desc != "Переказ на картку" && !desc.contains("картк"))
}

// Допоміжна перевірка: чи є транзакція IBAN-переказом
fun isIbanTransaction(myTransaction: MyTransaction): Boolean {
    val desc = myTransaction.description ?: return false

    return desc.startsWith("Від:") ||
            desc.startsWith("Вiд:") ||
            (desc != "Переказ на картку" && !desc.contains("картк"))
}

fun getIban(myTransaction: MyTransaction, jarAccount: JarAccount?): String {
    return if (isIbanTransaction(myTransaction)) {
        jarAccount?.iban ?: ""
    } else {
        ""
    }
}

fun getCardNumber(myTransaction: MyTransaction, jarAccount: JarAccount?): String {
    return if (isIbanTransaction(myTransaction)) {
        ""
    } else {
        jarAccount?.card ?: ""
    }
}

fun getCardLink(myTransaction: MyTransaction, jarAccount: JarAccount?): String {
    return if (isIbanTransaction(myTransaction)) {
        ""
    } else {
        // Перевіряємо, чи є sendId, щоб не генерувати пустий лінк
        val sendId = jarAccount?.sendId
        if (!sendId.isNullOrEmpty()) "https://send.monobank.ua/$sendId" else ""
    }
}
//fun getIban(myTransaction: MyTransaction, jarAccount: JarAccount?): String {
//    if (myTransaction.counterIban != null && myTransaction.counterIban != "") {
//        return jarAccount?.iban ?: ""
//    } else if (myTransaction.description?.startsWith("Вiд:") == true) {
//        return jarAccount?.iban ?: ""
//    } else {
//        return ""
//    }
//}
//
//fun getCardNumber(myTransaction: MyTransaction, jarAccount: JarAccount?): String {
//    if (myTransaction.counterIban != null && myTransaction.counterIban != "") {
//        return ""
//    } else if (myTransaction.description?.startsWith("Вiд:") == true) {
//        return ""
//    } else {
//        return jarAccount?.card ?: ""
//    }
//}
//
//fun getCardLink(myTransaction: MyTransaction, jarAccount: JarAccount?): String {
//    if (myTransaction.counterIban != null && myTransaction.counterIban != "") {
//        return ""
//    } else if (myTransaction.description?.startsWith("Вiд:") == true) {
//        return ""
//    } else {
//        return "https://send.monobank.ua/${jarAccount?.sendId}"
//    }
//}

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date())
}

fun SnapshotStateList<JarAccount>.sortedMonobank(): SnapshotStateList<JarAccount> {
    val listOfJarsAcc = mutableStateListOf<JarAccount>()

    val blacks = this.filter { it.type == "black" }
    listOfJarsAcc.addAll(blacks)

    val whites = this.filter { it.type == "white" }
    listOfJarsAcc.addAll(whites)

    val banks = this.filter { it.title != null }.sortedBy { it.hashCode() }
    listOfJarsAcc.addAll(banks)

    val errorCards = this.filter { it.sendId == "errorCard" }.sortedBy { it.hashCode() }
    listOfJarsAcc.addAll(errorCards)

    val errorBanks = this.filter { it.sendId == "errorBank" }.sortedBy { it.hashCode() }
    listOfJarsAcc.addAll(errorBanks)



//    this.forEach {
//
//        if (it.type == "black") {
//            listOfJarsAcc.add(it)
//        } else if (it.type == "white") {
//            listOfJarsAcc.add(it)
//        } else if (it.title != null) {
//            listOfJarsAcc.add(it)
//        } else if(it.sendId == "errorCard") {
//            listOfJarsAcc.add(it)
//        } else if (it.type == "errorBank") {
//            listOfJarsAcc.add(it)
//        } else {
//            listOfJarsAcc.add(it)
//        }
//
//    }

    Log.e("MESSI: EXTENSION: ", listOfJarsAcc.toList().toString())
    return listOfJarsAcc
}