package com.vero.libnetwork

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ApiService {
    var okHttpClient: OkHttpClient? = null
    var sBaseUrl: String? = null
    var sConvert: Convert<*>? = null

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()

        val trustManagers = arrayOf<TrustManager>(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf<X509Certificate>()
                }
            }
        )
        try {
            val ssl = SSLContext.getInstance("SSL")
            ssl.init(null, trustManagers, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory())
            HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
    }

    fun init(baseUrl:String,convert: Convert<Any>?) {
        sBaseUrl=baseUrl
        sConvert=convert
        if (convert==null) {
            sConvert=JsonConvert()
        }
    }

}