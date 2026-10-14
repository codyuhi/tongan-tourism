package online.codyuhi.tongantourism.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import online.codyuhi.tongantourism.data.api.WeatherApi
import online.codyuhi.tongantourism.domain.WeatherInfo
import kotlin.math.roundToInt

class WeatherRepository(private val weatherApi: WeatherApi) {

    val fallbackWeather = WeatherInfo(
        tempC = 26,
        tempF = 79,
        windSpeedKmH = 18.0,
        condition = "Nuku'alofa Tropical Warmth",
        isLive = false
    )

    fun getWeather(): Flow<WeatherInfo> = flow {
        // Emit fallback immediately for instant offline render
        emit(fallbackWeather)

        try {
            val response = weatherApi.getLiveWeather()
            val current = response.currentWeather
            if (current != null) {
                val tempC = current.temperature.roundToInt()
                val tempF = ((tempC * 9.0 / 5.0) + 32.0).roundToInt()
                emit(
                    WeatherInfo(
                        tempC = tempC,
                        tempF = tempF,
                        windSpeedKmH = current.windspeed,
                        condition = "Nuku'alofa Live (${current.windspeed} km/h wind)",
                        isLive = true
                    )
                )
            }
        } catch (_: Exception) {
            // Keep fallback on error or offline
        }
    }
}
