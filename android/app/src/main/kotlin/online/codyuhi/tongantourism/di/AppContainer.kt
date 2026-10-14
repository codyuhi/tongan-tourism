package online.codyuhi.tongantourism.di

import android.content.Context
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import online.codyuhi.tongantourism.audio.WhaleSoundEngine
import online.codyuhi.tongantourism.data.api.CurrencyApi
import online.codyuhi.tongantourism.data.api.WeatherApi
import online.codyuhi.tongantourism.data.db.TongaDatabase
import online.codyuhi.tongantourism.data.repository.CurrencyRepository
import online.codyuhi.tongantourism.data.repository.ItineraryRepository
import online.codyuhi.tongantourism.data.repository.SettingsRepository
import online.codyuhi.tongantourism.data.repository.TongaDataRepository
import online.codyuhi.tongantourism.data.repository.WeatherRepository
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val tongaDataRepository: TongaDataRepository
    val itineraryRepository: ItineraryRepository
    val settingsRepository: SettingsRepository
    val weatherRepository: WeatherRepository
    val currencyRepository: CurrencyRepository
    val soundEngine: WhaleSoundEngine
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private val weatherRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private val currencyRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://open.er-api.com/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private val weatherApi: WeatherApi by lazy {
        weatherRetrofit.create(WeatherApi::class.java)
    }

    private val currencyApi: CurrencyApi by lazy {
        currencyRetrofit.create(CurrencyApi::class.java)
    }

    private val database: TongaDatabase by lazy {
        TongaDatabase.getInstance(context)
    }

    override val tongaDataRepository: TongaDataRepository by lazy {
        TongaDataRepository(context)
    }

    override val itineraryRepository: ItineraryRepository by lazy {
        ItineraryRepository(database.itineraryDao(), tongaDataRepository)
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(context)
    }

    override val weatherRepository: WeatherRepository by lazy {
        WeatherRepository(weatherApi)
    }

    override val currencyRepository: CurrencyRepository by lazy {
        CurrencyRepository(currencyApi)
    }

    override val soundEngine: WhaleSoundEngine by lazy {
        WhaleSoundEngine()
    }
}
