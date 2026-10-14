package online.codyuhi.tongantourism

import online.codyuhi.tongantourism.data.api.CurrencyApi
import online.codyuhi.tongantourism.data.api.ExchangeRateResponse
import online.codyuhi.tongantourism.data.repository.CurrencyRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyRepositoryTest {

    private val mockApi = object : CurrencyApi {
        override suspend fun getLatestRates(baseCurrency: String): ExchangeRateResponse {
            return ExchangeRateResponse(
                result = "success",
                rates = mapOf("USD" to 0.45, "AUD" to 0.65)
            )
        }
    }

    private val repo = CurrencyRepository(mockApi)

    @Test
    fun `default rates contain expected keys and values`() {
        val defaults = repo.defaultRates
        assertEquals(0.42, defaults["USD"]?.rate ?: 0.0, 0.001)
        assertEquals(0.64, defaults["AUD"]?.rate ?: 0.0, 0.001)
        assertEquals(0.70, defaults["NZD"]?.rate ?: 0.0, 0.001)
        assertEquals(0.39, defaults["EUR"]?.rate ?: 0.0, 0.001)
        assertEquals(0.33, defaults["GBP"]?.rate ?: 0.0, 0.001)
    }

    @Test
    fun `currency converter computes correct conversions`() {
        val convertedUsd = repo.convert(100.0, "USD", repo.defaultRates)
        assertEquals(42.0, convertedUsd, 0.01)

        val convertedAud = repo.convert(100.0, "AUD", repo.defaultRates)
        assertEquals(64.0, convertedAud, 0.01)
    }
}
