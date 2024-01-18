package by.sergei.testbanktmapp.data

import by.sergei.testbanktmapp.data.response.CurrencyRateDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyApiInterface {

    @GET("rates?periodicity=0")
    suspend fun getCurrencyRatesOnDate(@Query("ondate") date: String): List<CurrencyRateDto>

    @GET("rates/{cur_id}periodicity=0")
    suspend fun getCurrencyRateByCurId(
        @Path("cur_id") curId: Long,
        @Query("ondate") date: String
    ): CurrencyRateDto //todo test
}