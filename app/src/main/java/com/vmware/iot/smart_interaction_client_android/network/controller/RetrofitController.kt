package com.vmware.iot.smart_interaction_client_android.network.controller

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.vmware.iot.smart_interaction_client_android.network.IoTApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by Asim on 26/04/17.
 */

// controller to get the instance of retrofit and api calls

class RetrofitController private constructor() {
    private val jacksonConverterFactory: JacksonConverterFactory
    private val okHttpClient: OkHttpClient

    val IoTDetails: IoTApi
        get() {
            val retrofit = getRetrofitBuilder(IoTApi.BASE_URL)
            return retrofit.create(IoTApi::class.java)
        }

    init {

        //Make http client
        okHttpClient = OkHttpClient.Builder()
                .connectTimeout(IoTApi.OKHTTP_CLIENT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(IoTApi.OKHTTP_CLIENT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .followRedirects(false)
                .addInterceptor(HeaderInterceptor())
                .build()

        //Make JSON converter
        val objectMapper = ObjectMapper()
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        jacksonConverterFactory = JacksonConverterFactory.create(objectMapper)
    }

    private inner class HeaderInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val newRequest = request.newBuilder()
                    .addHeader(IoTApi.CONTENT_TYPE, IoTApi.CONTENT_TYPE)
                    .build()
            return chain.proceed(newRequest)
        }
    }

    private fun getRetrofitBuilder(url: String): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(url)
                .addConverterFactory(jacksonConverterFactory)
                .build()
    }

    companion object {
        private var retrofitInstance: RetrofitController? = null

        val instance: RetrofitController
            get() {
                if (retrofitInstance == null) {
                    synchronized(RetrofitController::class.java) {
                        if (retrofitInstance == null) {
                            retrofitInstance = RetrofitController()
                        }
                    }
                }
                return retrofitInstance!!
            }
    }
}