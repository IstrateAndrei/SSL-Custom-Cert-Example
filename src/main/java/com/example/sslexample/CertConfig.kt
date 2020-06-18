package com.example.sslexample

import okhttp3.tls.HandshakeCertificates
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.cert.CertificateFactory
import java.security.cert.CertificateParsingException
import java.security.cert.X509Certificate

class CertConfig {

    private lateinit var handshakeBuilder: HandshakeCertificates.Builder

    var ipAddress = "10.30.1.100"
    var isTrustedCertificate = false

    companion object {
        private lateinit var instance: CertConfig
        @JvmStatic
        val config: CertConfig
            get() {
                if (!::instance.isInitialized) {
                    instance = CertConfig()
                }
                return instance
            }
    }

    fun addCertificate(input: InputStream) {
        if (!::handshakeBuilder.isInitialized) handshakeBuilder = HandshakeCertificates.Builder()
        handshakeBuilder.addTrustedCertificate(decodeCertificatePem(input) as X509Certificate)
    }

    fun getHandshakeCertificates(): HandshakeCertificates {
        if (!::handshakeBuilder.isInitialized)
            handshakeBuilder = HandshakeCertificates.Builder()

        return handshakeBuilder.build()
    }

    private fun decodeCertificatePem(inputStream: InputStream): java.security.cert.Certificate {
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")
            return certificateFactory.generateCertificate(inputStream)
        } catch (nsee: NoSuchElementException) {
            throw CertificateParsingException(nsee.toString())
        } catch (iae: IllegalArgumentException) {
            throw CertificateParsingException(iae.toString())
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
            throw IllegalArgumentException("failed to decode certificate", e)
        }
    }
}