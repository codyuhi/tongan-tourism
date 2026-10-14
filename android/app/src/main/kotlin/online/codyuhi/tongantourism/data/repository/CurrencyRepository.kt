package online.codyuhi.tongantourism.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import online.codyuhi.tongantourism.data.api.CurrencyApi
import online.codyuhi.tongantourism.domain.CurrencyRate

class CurrencyRepository(private val currencyApi: CurrencyApi) {

    val defaultRates = mapOf(
        "USD" to CurrencyRate("USD", 0.42, "$"),
        "AUD" to CurrencyRate("AUD", 0.64, "A$"),
        "NZD" to CurrencyRate("NZD", 0.70, "NZ$"),
        "EUR" to CurrencyRate("EUR", 0.39, "€"),
        "GBP" to CurrencyRate("GBP", 0.33, "£")
    )

    fun getRates(): Flow<Map<String, CurrencyRate>> = flow {
        // Emit fallback immediately
        emit(defaultRates)

        try {
            val response = currencyApi.getLatestRates("TOP")
            val liveRates = response.rates
            if (liveRates.isNotEmpty()) {
                val updated = defaultRates.mapValues { (code, defaultVal) ->
                    val apiRate = liveRates[code] ?: defaultVal.rate
                    CurrencyRate(code, apiRate, defaultVal.symbol)
                }
                emit(updated)
            }
        } catch (_: Exception) {
            // Keep defaults offline
        }
    }

    fun convert(amountTOP: Double, targetCode: String, rates: Map<String, CurrencyRate>): Double {
        val rate = rates[targetCode]?.rate ?: defaultRates[targetCode]?.rate ?: 0.42
        return amountTOP * rate
    }
}
