package com.example.sslexample

import com.example.sslexample.CertConfig.Companion.config
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    val client: ApiInterface
        get() {
              return Retrofit.Builder()
                        .baseUrl("https://someapi.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(if (config.isTrustedCertificate) httpClient else getCustomHttpClient())
                        .build()
                .create(ApiInterface::class.java)
        }

    private val httpClient: OkHttpClient
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder().addInterceptor(interceptor)
                    .sslSocketFactory(config.getHandshakeCertificates().sslSocketFactory(), config.getHandshakeCertificates().trustManager)
                    .build()
        }
}