package online.codyuhi.tongantourism.data.api

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

@Serializable
data class ExchangeRateResponse(
    val result: String = "success",
    val rates: Map<String, Double> = emptyMap()
)

interface CurrencyApi {
    @GET("v6/latest/{base}")
    suspend fun getLatestRates(@Path("base") baseCurrency: String = "TOP"): ExchangeRateResponse
}
