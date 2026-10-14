package online.codyuhi.tongantourism.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import online.codyuhi.tongantourism.data.repository.CurrencyRepository
import online.codyuhi.tongantourism.data.repository.TongaDataRepository
import online.codyuhi.tongantourism.data.repository.WeatherRepository
import online.codyuhi.tongantourism.domain.CurrencyRate
import online.codyuhi.tongantourism.domain.Island
import online.codyuhi.tongantourism.domain.KingdomStats
import online.codyuhi.tongantourism.domain.WeatherInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class DiscoverUiState(
    val stats: KingdomStats = KingdomStats(),
    val islands: List<Island> = emptyList(),
    val tongaTime: String = "12:00",
    val weather: WeatherInfo? = null,
    val topUsdRate: Double = 0.42
)

class DiscoverViewModel(
    private val dataRepo: TongaDataRepository,
    private val weatherRepo: WeatherRepository,
    private val currencyRepo: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DiscoverUiState(
            stats = dataRepo.stats,
            islands = dataRepo.islands,
            weather = weatherRepo.fallbackWeather
        )
    )
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        startTongaClock()
        observeWeather()
        observeCurrency()
    }

    private fun startTongaClock() {
        viewModelScope.launch {
            val formatter = SimpleDateFormat("HH:mm", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("Pacific/Tongatapu")
            }
            while (true) {
                val nowStr = formatter.format(Date())
                _uiState.value = _uiState.value.copy(tongaTime = nowStr)
                delay(1000)
            }
        }
    }

    private fun observeWeather() {
        weatherRepo.getWeather().onEach { info ->
            _uiState.value = _uiState.value.copy(weather = info)
        }.launchIn(viewModelScope)
    }

    private fun observeCurrency() {
        currencyRepo.getRates().onEach { ratesMap ->
            val usdRate = ratesMap["USD"]?.rate ?: 0.42
            _uiState.value = _uiState.value.copy(topUsdRate = usdRate)
        }.launchIn(viewModelScope)
    }
}
