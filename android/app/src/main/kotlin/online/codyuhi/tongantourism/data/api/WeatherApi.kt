package online.codyuhi.tongantourism.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable
data class CurrentWeatherDto(
    val temperature: Double,
    val windspeed: Double,
    @SerialName("weathercode") val weatherCode: Int = 0
)

@Serializable
data class OpenMeteoResponse(
    @SerialName("current_weather") val currentWeather: CurrentWeatherDto? = null
)

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getLiveWeather(
        @Query("latitude") latitude: Double = -21.1789,
        @Query("longitude") longitude: Double = -175.1982,
        @Query("current_weather") currentWeather: Boolean = true
    ): OpenMeteoResponse
}
