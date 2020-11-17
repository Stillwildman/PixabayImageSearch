package com.vincent.imagesearch.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.brotli.BrotliInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Vincent on 2020/11/17.
 */
object RetrofitAgent {

    private var retrofit: Retrofit? = null

    fun getRetrofit(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = newRetrofit(baseUrl)
        }
        else if (retrofit!!.baseUrl().toString() != baseUrl) {
            retrofit = newRetrofit(baseUrl)
        }
        return retrofit!!
    }

    private fun newRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .client(getOkHttpClient())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().run {
            val logInterceptor = HttpLoggingInterceptor { message ->
                Log.d("RetrofitAgent", "HttpLog: $message")
            }
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            addInterceptor(logInterceptor)
            addInterceptor(BrotliInterceptor)

            build()
        }
    }

}