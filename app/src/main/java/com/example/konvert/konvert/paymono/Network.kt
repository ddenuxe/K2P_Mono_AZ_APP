package com.example.konvert.konvert.paymono

import com.example.konvert.konvert.paymono.data.ClientInfo
import com.example.konvert.konvert.paymono.data.PostData
import com.example.konvert.konvert.paymono.data.MyTransaction
import com.example.konvert.konvert.paymono.data.PingData
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

object Network {

    val BASE_URL_FAKE = "api.monobank.ua"

    suspend fun getInitData(): HttpResponse {
        val builder = HttpRequestBuilder()
        builder.url {
            protocol = URLProtocol.HTTPS
            host = BASE_URL_FAKE
            path("personal/client-info")
        }
        builder.headers {
            append("X-Token", UtilsObject.token_monobank)

        }
        return ClientHtppsus.httpClient.get(builder)
    }

    suspend fun getTransactions(accountId: String, timeLast5min: Long): HttpResponse {
        val builder = HttpRequestBuilder()
        val toTime = System.currentTimeMillis() / 1000
        builder.url {
            protocol = URLProtocol.HTTPS
            host = BASE_URL_FAKE
            path("personal/statement/${accountId}/${timeLast5min}/${toTime}")
        }
        builder.headers {
            append("X-Token", UtilsObject.token_monobank)
        }
        return ClientHtppsus.httpClient.get(builder)
    }

    suspend fun sendServer(myJson: PostData): HttpResponse {
        //Log.e("MESSI getInitData URL: ", BASE_URL_FAKE.toString())
        //return ClientHtppsus.httpClient.post("https://httpbin.org/post") {
        return ClientHtppsus.httpClient.post("https://az-konvert2pay.me/AZ/MonoBankExternal.ashx") {
            contentType(ContentType.Application.Json)
            setBody(myJson)
        }
    }

    suspend fun sendPingData(myJson: PingData): HttpResponse {
        //Log.e("MESSI getInitData URL: ", BASE_URL_FAKE.toString())
        //return ClientHtppsus.httpClient.post("https://httpbin.org/post") {
        return ClientHtppsus.httpClient.post("https://az-konvert2pay.me/AZ/Ping.ashx") {
            contentType(ContentType.Application.Json)
            setBody(myJson)
        }
    }

    suspend fun sendTestData(myJson: PostData): HttpResponse {
        //Log.e("MESSI getInitData URL: ", BASE_URL_FAKE.toString())
        //return ClientHtppsus.httpClient.post("https://httpbin.org/post") {
        return ClientHtppsus.httpClient.post(UtilsObject.server_name) {
            contentType(ContentType.Application.Json)
            setBody(myJson)
        }
    }


}
