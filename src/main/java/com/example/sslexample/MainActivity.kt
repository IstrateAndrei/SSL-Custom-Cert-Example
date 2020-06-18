package com.example.sslexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sslexample.CertConfig.Companion.config
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.tls.HandshakeCertificates
import retrofit2.Call
import retrofit2.Callback
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.cert.CertificateFactory
import java.security.cert.CertificateParsingException
import java.security.cert.X509Certificate

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input = this.resources.openRawResource(R.raw.certificate)
        config.addCertificate(input)
    }


    fun loginAction(){
        ApiClient.client.login().subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Void>{
                override fun onComplete() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onSubscribe(d: Disposable?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onNext(t: Void?) {
                    if(!config.isTrustedCertificate) {
                        //display subscription expired flow
                    }
                }

                override fun onError(e: Throwable?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
    }

}


