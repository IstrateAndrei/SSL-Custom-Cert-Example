package com.example.sslexample

import com.example.sslexample.CertConfig.Companion.config
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

fun getCustomHttpClient(): OkHttpClient {

    return try {
        val customBuilder = OkHttpClient.Builder()
        val customTrustManager = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                chain?.forEach { x509Certificate ->
                    x509Certificate.subjectAlternativeNames.forEach { san ->
                        san?.let { sanItem ->
                            sanItem[1]?.let {
                                if (it == config.ipAddress) {
                                    config.isTrustedCertificate = true
                                    customBuilder.sslSocketFactory(config.getHandshakeCertificates().sslSocketFactory(), config.getHandshakeCertificates().trustManager)
                                    return
                                }
                            }
                        }
                    }
                }
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

        }

        if (!config.isTrustedCertificate) {
            val trustAllContext = SSLContext.getInstance("SSL")
            trustAllContext.init(null, arrayOf(customTrustManager), SecureRandom())
            customBuilder.sslSocketFactory(trustAllContext.socketFactory, customTrustManager)
        } else customBuilder.sslSocketFactory(config.getHandshakeCertificates().sslSocketFactory(), config.getHandshakeCertificates().trustManager)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        customBuilder.addInterceptor(interceptor)
            .build()

        customBuilder.build()
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}