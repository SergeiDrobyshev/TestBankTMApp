package by.sergei.testbanktmapp.data

import by.sergei.testbanktmapp.utils.LocalDateGsonAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.LocalDate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val API_BASE_URL = "https://api.nbrb.by/exrates/"

    fun build(): CurrencyApiInterface {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(OkHttpClient.Builder().addInterceptor(interceptor()).build())
            .addConverterFactory(createGsonConverter())
            .build()
            .create(CurrencyApiInterface::class.java)
    }

    private fun createGsonConverter(): GsonConverterFactory{
       val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateGsonAdapter())
            .create()
        return GsonConverterFactory.create(gson)
    }

    private fun interceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}