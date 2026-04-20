package com.example.konvert.konvert.paymono.data

import kotlinx.serialization.Serializable

@Serializable
data class ClientInfo(
    val clientId: String? = null,
    val name: String? = null,
    val webHookUrl: String? = null,
    val permissions: String? = null,
    val accounts: List<Account>? = null,
    val jars: List<Jar>? = null
)

@Serializable
data class Account(
    val id: String? = null,
    val sendId: String? = null,
    val balance: Int? = null,
    val creditLimit: Int? = null,
    val type: String? = null,
    val currencyCode: Int? = null,
    val cashbackType: String? = null,
    val maskedPan: List<String>? = null,
    val iban: String? = null

)

@Serializable
data class Jar(
    val id: String? = null,
    val sendId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val currencyCode: Int? = null,
    val balance: Int? = null,
    val goal: Int? = null
)

//
//                        for (i in initData.accounts) {
//                            try {
//                                val card = i.maskedPan?.get(0)?.takeLast(4)
//                                val preAccounts: Map<String, String> = UtilsObject.all_cards.map { it.key to it.value.takeLast(4)  }.toMap()
//                                if (card in preAccounts.values) {
//                                    //Log.e("MESSI CARD: ", card.toString())
//                                    var full_card_number: String? = ""
//
//                                    try {
//                                        val trueAccount = UtilsObject.all_cards.values.find {
//                                            it.takeLast(4) == card
//                                        }
//                                        full_card_number = trueAccount
//                                    } catch (e: Exception) {
//                                        Log.e("MESSI CARD exception: ", e.toString())
//                                    }
//
//                                    val temp = JarAccount(i.id ?: "", i.sendId ?: "", i.balance ?: 0, i.creditLimit, i.type, i.currencyCode ?: 0, i.cashbackType, i.maskedPan, i.iban, card = full_card_number ?: "")
//
//                                    jarsAndAccounts.add(temp)
//                                    delay(50)
//
//                                    jarsAndAccountsForShow.add(temp)
//                                    //val getType = preAccounts.keys.find { preAccounts.get(it) == i.type }
//                                    if (blackCard == full_card_number && i.type == "white") {
//                                        shared.edit().putString("white_card", full_card_number).apply()
//                                    }
//                                    if (whiteCard == full_card_number && i.type == "black") {
//                                        shared.edit().putString("black_card", full_card_number).apply()
//
//                                    }
//                                    //Log.e("MESSI jarsAndAccounts: ", JarAccount(i.id ?: "", i.sendId ?: "", i.balance ?: 0, i.creditLimit, i.type, i.currencyCode ?: 0, i.cashbackType, i.maskedPan, i.iban, card = full_card_number ?: "").toString())
//
//                                }
//                            } catch (e: Exception) {
//                                Log.e("MESSI CARD Exception index: ", e.toString())
//                            }
//                        }
//                    }
//                    if (initData.jars != null) {
//                        for (j in initData.jars) {
//                            try {
//                                //Log.e("MESSI AllJarsValues: ", UtilsObject.all_jars_cards.values.toString())
//                                val jarsKeys: List<String> = UtilsObject.all_jars_cards.values.map { it.substringBefore("###") }
//
//                                if (j.sendId in jarsKeys) {
//                                    var full_card_number: String? = ""
//
//                                    try {
//                                        val card = UtilsObject.all_jars_cards.values.find { j.sendId == it.substringBefore("###") }
//                                        full_card_number = card?.substringAfter("###")
//                                    } catch (e: Exception) {
//                                        Log.e("MESSI Jar exception: ", e.toString())
//                                    }
//
//                                    val temp = mutableListOf<JarAccount>()
//                                    temp.add(JarAccount(id = j.id ?: "", sendId = j.sendId ?: "", balance = j.balance ?: 0, currencyCode = j.currencyCode ?: 0, goal = j.goal, description = j.description, title = j.title, card = full_card_number ?: ""))
//                                    jarsAndAccounts.addAll(temp)
//                                    //jarsAndAccountsForShow.addAll(temp)
//                                    delay(50)
//                                    jarsAndAccountsForShow.add(JarAccount(id = j.id ?: "", sendId = j.sendId ?: "", balance = j.balance ?: 0, currencyCode = j.currencyCode ?: 0, goal = j.goal, description = j.description, title = j.title, card = full_card_number ?: ""))
//
//                                    //Log.e("MESSI Add Jar: ", JarAccount(id = j.id ?: "", sendId = j.sendId ?: "", balance = j.balance ?: 0, currencyCode = j.currencyCode ?: 0, goal = j.goal, description = j.description, title = j.title, card = full_card_number ?: "").toString())
//                                }
//                            } catch (e: Exception) {
//                                Log.e("MESSI Exception jars: ", e.toString())
//                            }
//                        }
//                    }
//
//                    if (initData.clientId != null) {
//                        val approvedCardList: List<String> = jarsAndAccounts.map { it.card }
//
//                        Log.e("MESSI all_cards: ", UtilsObject.all_cards.toString())
//
//                        UtilsObject.all_cards.forEach { card ->
//                            if (!approvedCardList.contains(card.value)) {
//                                val item = JarAccount(id = card.value, balance = 0, sendId = "errorCard", currencyCode = 0, card = card.value)
//                                delay(50)
//                                jarsAndAccountsForShow.add(item)
//
//                            } else {
//
//                                val acc: JarAccount? = jarsAndAccounts.find { it.type == card.key && it.card == card.value }
//                                Log.e("MESSI 2 acc: ", acc.toString())
//
//                                if (acc == null) {
//                                    val item = JarAccount(id = card.value, balance = 0, sendId = "errorCard", currencyCode = 0, card = card.value)
//                                    delay(50)
//                                    jarsAndAccountsForShow.add(item)
//                                }
//                            }
//
//                        }

//                        UtilsObject.all_jars_cards.forEach { jar ->
//
//                            if (!approvedCardList.contains(jar.value.substringAfter("###"))) {
//                                val item = JarAccount(id = jar.value.substringAfter("###"), balance = 0, sendId = "errorBank", currencyCode = 0, card = jar.value.substringAfter("###"))
//
//                                delay(50)
//                                jarsAndAccountsForShow.add(item)
//
//                            } else {
//                                val acc: JarAccount? = jarsAndAccounts.find { it.sendId == jar.value.substringBefore("###") && it.card == jar.value.substringAfter("###")}
//                                if (acc == null) {
//                                    val item = JarAccount(id = jar.value.substringAfter("###"), balance = 0, sendId = "errorBank", currencyCode = 0, card = jar.value.substringAfter("###"))
//                                    delay(50)
//                                    jarsAndAccountsForShow.add(item)
//                                }
//
//                            }
//                        }

//                    jarsAndAccountsForShow.forEachIndexed { index, item ->
//                        jarsAndAccountsForShow[index] = jarsAndAccountsForShow[index].copy()
//                    }

//                    val newList = mutableListOf<JarAccount>()
//                    jarsAndAccountsForShow.forEachIndexed { index, item ->
//                        newList.add(item.copy())
//                    }
//                    jarsAndAccountsForShow.clear()
//                    jarsAndAccountsForShow.addAll(newList)


