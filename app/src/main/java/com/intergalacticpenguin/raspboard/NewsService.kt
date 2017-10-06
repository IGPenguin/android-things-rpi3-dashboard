package com.intergalacticpenguin.raspboard

import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

var API_KEY:String = "9a1faf50080147ca9e63b80c12972c50"

interface NewsService {

    @GET("v1/articles")
    fun getNews(@Query("source") source: String,
             @Query("sortBy") sortBy: String,
             @Query("apiKey") apiKey: String): Observable<ResultArticle>

    /**
     * Companion object to create the NewsService
     */

    companion object Factory {

        fun create(): NewsService {
            val okhttpClient = OkHttpClient.Builder()
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://newsapi.org/")
                    .client(okhttpClient.addInterceptor(loggingInterceptor).build())
                    .build()

            return retrofit.create(NewsService::class.java);
        }
    }
}
