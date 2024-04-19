package com.hitachivantara.mobilecoe.android.data.api

import com.hitachivantara.mobilecoe.android.data.preferences.PreferencesStore
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule(private val preferencesStore: PreferencesStore) {
    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    fun <T> createService(serviceClazz: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient())
            .build().create(serviceClazz)
    }

    private fun getHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .addInterceptor(Interceptor { chain ->
                val request: Request = chain.request()
                val newRequest: Request = request.newBuilder()
                    .addHeader("Authorization", "Bearer ${preferencesStore.getToken()}")
                    .build()
                chain.proceed(newRequest)
            }).build()
    }
}