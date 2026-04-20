package com.example.konvert.konvert.paymono.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds

class StateViewModel: ViewModel() {


    private var firstStart = true
    var tick: Int by mutableStateOf(0)

    var myToken: String by mutableStateOf("")

    ///////////////////////////////////////////////////////////
    private var black_card: String by mutableStateOf("")
    private var white_card: String by mutableStateOf("")
    private var fop_card: String by mutableStateOf("")

    private var black_card_IBAN: String by mutableStateOf("")
    private var white_card_IBAN: String by mutableStateOf("")
    private var fop_card_IBAN: String by mutableStateOf("")

    private var black_card_link: String by mutableStateOf("")
    private var white_card_link: String by mutableStateOf("")


    ///////////////////////////////////////////////////////////
    var black_card_temp: String by mutableStateOf("")
    var white_card_temp: String by mutableStateOf("")
    var fop_card_temp: String by mutableStateOf("")
    var black_card_IBAN_temp: String by mutableStateOf("")
    var white_card_IBAN_temp: String by mutableStateOf("")
    var fop_card_IBAN_temp: String by mutableStateOf("")
    var black_card_link_temp: String by mutableStateOf("")
    var white_card_link_temp: String by mutableStateOf("")


    ///////////////////////////////////////////////////////////
    private var jar_1: String by mutableStateOf("")
    private var jar_2: String by mutableStateOf("")
    private var jar_3: String by mutableStateOf("")
    private var jar_card_1: String by mutableStateOf("")
    private var jar_card_2: String by mutableStateOf("")
    private var jar_card_3: String by mutableStateOf("")

    var jar_1_link_temp: String by mutableStateOf("")
    var jar_2_link_temp: String by mutableStateOf("")
    var jar_3_link_temp: String by mutableStateOf("")
    var jar_card_1_temp: String by mutableStateOf("")
    var jar_card_2_temp: String by mutableStateOf("")
    var jar_card_3_temp: String by mutableStateOf("")


    fun init(
        token: String,
        server: String,
        black_card: String,
        white_card: String,
        jar_1: String,
        jar_2: String,
        jar_3: String,
        device_id: String,
        user_id: String,
        jar_card_1: String,
        jar_card_2: String,
        jar_card_3: String,
        fop_card: String,
        black_card_IBAN: String,
        white_card_IBAN: String,
        fop_card_IBAN: String,
        black_card_link: String,
        white_card_link: String

    ) {
        if (firstStart) {
            setTokenName(token)
            setAddress(server)
            setBlackCards(black_card)
            setWhiteCards(white_card)
            setJarCardLinkValue_1(jar_1)
            setJarCardLinkValue_2(jar_2)
            setJarCardLinkValue_3(jar_3)
            setJarCardNumerValue_1(jar_card_1)
            setJarCardNumerValue_2(jar_card_2)
            setJarCardNumerValue_3(jar_card_3)
            setDeviceId(device_id)
            setUserId(user_id)
            setFOPCards(fop_card)
            setWhiteCardsIBAN(white_card_IBAN)
            setBlackCardsIBAN(black_card_IBAN)
            setFOPCardsIBAN(fop_card_IBAN)
            setBlackCardsLink(black_card_link)
            setWhiteCardsLink(white_card_link)


        }
        firstStart = false
    }

    fun startTimer(_tick: Long) {
        var temp = (_tick.toDouble() / 1000).roundToInt()
        viewModelScope.launch {
            while(true) {
                delay(1.seconds)
                temp -= 1
                tick = temp
                //Log.e("MESSI TISK: ", tick.toString())
                if (tick < 0) {
                    return@launch
                }
            }
        }
    }

    fun isTheSame() : Boolean {
        if (myToken == tokenValue && black_card == blackCardValue && white_card == whiteCardValue && jar_1 == jarCardLink_1 && jar_2 == jarCardLink_2 && jar_3 == jarCardLink_3 && jar_card_1 == jarCardNumber_1 && jar_card_2 == jarCardNumber_2 && jar_card_3 == jarCardNumber_3 &&
            fop_card == FOPCardValue && white_card_IBAN == whiteCardValueIBAN && black_card_IBAN == blackCardValueIBAN && fop_card_IBAN == fopCardValueIBAN &&
            black_card_link == blackCardValueLink && white_card_link == whiteCardValueLink) {
            return true
        } else {
            return false
        }
    }

    fun checkAllData(_token: String, _black_card: String, _white_card: String, _jar_1: String, _jar_2: String, _jar_3: String, _jar_card_1: String, _jar_card_2: String, _jar_card_3: String, _fop_card: String,
                     _white_card_IBAN: String, _black_card_IBAN: String, _fop_card_IBAN: String, _white_card_link: String, _black_card_link: String) {
        myToken = _token
        black_card = _black_card
        white_card = _white_card
        jar_1 = _jar_1
        jar_2 = _jar_2
        jar_3 = _jar_3
        jar_card_1 = _jar_card_1
        jar_card_2 = _jar_card_2
        jar_card_3 = _jar_card_3
        fop_card = _fop_card
        white_card_IBAN = _white_card_IBAN
        black_card_IBAN = _black_card_IBAN
        fop_card_IBAN = _fop_card_IBAN
        black_card_link = _black_card_link
        white_card_link = _white_card_link
    }

    var tokenValue: String by mutableStateOf("")
        private set
    var tokenError by mutableStateOf("")
        private set
    fun setTokenName(value: String) {
        tokenValue = value
    }

    fun validateToken() {
        if (tokenValue.isEmpty()) {
            tokenError = "Пустой токен"
        } else {
            tokenError = ""
        }
    }

    var serverValue: String by mutableStateOf("")
        private set
    fun setAddress(value: String){
        serverValue = value
    }

//    var serverPingValue: String by mutableStateOf("")
//        private set
//    fun setServerPingAddress(value: String){
//        serverPingValue = value
//    }

///////////////////////////////////////////////////////////////////
    var userValue: String by mutableStateOf("")
        private set
    var userError: String by mutableStateOf("")
        private set

    fun setUserId(value: String){
        userValue = value
    }

    fun validateUserId() {
        if (userValue.isEmpty()) {
            userError = "Пустой user id"
        } else {
            userError = ""
        }
    }

/////////////////////////////////////////

    var deviceValue: String by mutableStateOf("")
        private set
    var deviceError: String by mutableStateOf("")
        private set

    fun setDeviceId(value: String){
        deviceValue = value
    }

    fun validateDevice() {
        if (deviceValue.isEmpty()) {
            deviceError = "Пустой device id"
        } else {
            deviceError = ""
        }
    }
//////////////////////////////////////////////////////////


    var blackCardValue: String by mutableStateOf("")
        private set
    fun setBlackCards(value: String){
        if (value == "") {
            blackCardValue = value
        }
        if (value.matches(Regex("-?[0-9]+(\\.[0-9]+)?")) && value.length < 17) {
            blackCardValue = value
        }
    }

    var whiteCardValue: String by mutableStateOf("")
        private set
    fun setWhiteCards(value: String){
        if (value == "") {
            whiteCardValue = value
        }
        if (value.matches(Regex("-?[0-9]+(\\.[0-9]+)?")) && value.length < 17) {
            whiteCardValue = value
        }
    }

    var FOPCardValue: String by mutableStateOf("")
        private set
    fun setFOPCards(value: String){
        if (value == "") {
            FOPCardValue = value
        }
        if (value.matches(Regex("-?[0-9]+(\\.[0-9]+)?")) && value.length < 17) {
            FOPCardValue = value
        }
    }

    ///////////////////////////////////////////////////////

    var blackCardValueIBAN: String by mutableStateOf("")
        private set
    fun setBlackCardsIBAN(value: String){
       // if (value == "") {
            blackCardValueIBAN = value
       // }
//        if (value.matches(Regex("-?[0-9]+(\\.[0-9]+)?")) && value.length < 17) {
//            blackCardValueIBAN = value
//        }
    }

    var whiteCardValueIBAN: String by mutableStateOf("")
        private set
    fun setWhiteCardsIBAN(value: String){
       // if (value == "") {
            whiteCardValueIBAN = value
       // }
//        if (value.matches(Regex("-?[0-9]+(\\.[0-9]+)?")) && value.length < 17) {
//            whiteCardValueIBAN = value
//        }
    }

    var fopCardValueIBAN: String by mutableStateOf("")
        private set
    fun setFOPCardsIBAN(value: String){
       // if (value == "") {
            fopCardValueIBAN = value
       // }
//        if (value.matches(Regex("-?[0-9]+(\\.[0-9]+)?")) && value.length < 17) {
//            fopCardValueIBAN = value
//        }

    }

    //////////////////////////////////////////////////////

    var blackCardValueLink: String by mutableStateOf("")
        private set
    fun setBlackCardsLink(value: String){
        blackCardValueLink = value
    }

    var whiteCardValueLink: String by mutableStateOf("")
        private set
    fun setWhiteCardsLink(value: String){
        whiteCardValueLink = value
    }

//    var fopCardValueLink: String by mutableStateOf("")
//        private set
//    fun setFOPCardsLink(value: String){
//        fopCardValueLink = value
//    }

    ///////////////////////////////////////////////////////

    var jarCardNumber_1: String by mutableStateOf("")
        private set
    var jarCardLink_1: String by mutableStateOf("")
        private set

    fun setJarCardNumerValue_1(value: String) {
        if (value == "") {
            jarCardNumber_1 = value
        }
        if (value.matches(Regex("-?[0-9]+(\\.[0-9]+)?"))) {
            jarCardNumber_1 = value
        }
    }
    fun setJarCardLinkValue_1(value: String){
        jarCardLink_1 = value
    }


    var jarCardNumber_2: String by mutableStateOf("")
        private set
    var jarCardLink_2: String by mutableStateOf("")
        private set

    fun setJarCardNumerValue_2(value: String){
        if (value == "") {
            jarCardNumber_2 = value
        }
        if (value.matches(Regex("-?[0-9]+(\\.[0-9]+)?"))) {
            jarCardNumber_2 = value
        }
    }
    fun setJarCardLinkValue_2(value: String){
        jarCardLink_2 = value
    }


    var jarCardNumber_3: String by mutableStateOf("")
        private set
    var jarCardLink_3: String by mutableStateOf("")
        private set

    fun setJarCardNumerValue_3(value: String){
        if (value == "") {
            jarCardNumber_3 = value
        }
        if (value.matches(Regex("-?[0-9]+(\\.[0-9]+)?"))) {
            jarCardNumber_3 = value
        }
    }
    fun setJarCardLinkValue_3(value: String){
        jarCardLink_3 = value
    }



}