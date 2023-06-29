package pl.mikorosa.webco

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CountDownLatch

class APIManager {
    companion object {
        fun fetch(url: String, method: String, token: String?, reqBody: FormBody?): String {
            val request = Request.Builder()
                .url(url)
                .method(method, reqBody)
                .addHeader("Authorization", token ?: "")
                .build()

            val client = OkHttpClient()
            var body = ""

            val countDownLatch = CountDownLatch(1)
            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    body = response?.body()?.string().toString()
                    countDownLatch.countDown()
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    println("Failed to execute request")
                    e?.printStackTrace()
                    countDownLatch.countDown()
                }
            })

            countDownLatch.await()
            return body
        }

        fun getOwnResources(accessToken: String): JsonObject {
            val request = Request.Builder()
                .url("https://col.ael.ovh/api/getResources/own")
                .addHeader("Authorization", accessToken)
                .build()

            val client = OkHttpClient()
            var body: JsonObject = Gson().fromJson("{code:500, message: \"Internal server error\"}", JsonObject::class.java)

            val countDownLatch = CountDownLatch(1)
            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    body = Gson().fromJson(response?.body()?.string().toString(), JsonObject::class.java)
                    countDownLatch.countDown()
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    println("Failed to execute request")
                    e?.printStackTrace()
                    countDownLatch.countDown()
                }
            })

            countDownLatch.await()
            return body.asJsonObject.get("resources").asJsonObject
        }

        fun getDefaultMiningRates(): JsonObject {
            val request = Request.Builder()
                .url("https://col.ael.ovh/api/getDefaultMiningRates")
                .build()

            val client = OkHttpClient()
            var body: JsonObject = Gson().fromJson("{code:500, message: \"Internal server error\"}", JsonObject::class.java)

            val countDownLatch = CountDownLatch(1)
            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    body = Gson().fromJson(response?.body()?.string().toString(), JsonObject::class.java)
                    countDownLatch.countDown()
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    println("Failed to execute request")
                    e?.printStackTrace()
                    countDownLatch.countDown()
                }
            })


            countDownLatch.await()
            return body.asJsonObject.get("rates").asJsonObject
        }

        fun login(username: String, password: String): String {
            val reqBody = FormBody.Builder()
                .add("login", username)
                .add("password", password)
                .build()

            val request = Request.Builder()
                .url("https://col.ael.ovh/api/authorize")
                .method("POST", reqBody)
                .build()

            val client = OkHttpClient()
            var body = ""

            val countDownLatch = CountDownLatch(1)
            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    body = response?.body()?.string().toString()
                    countDownLatch.countDown()
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    println("Failed to execute request")
                    e?.printStackTrace()
                    countDownLatch.countDown()
                }
            })

            countDownLatch.await()
            return body
        }
    }
}