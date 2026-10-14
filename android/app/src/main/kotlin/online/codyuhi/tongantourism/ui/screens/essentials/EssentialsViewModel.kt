package online.codyuhi.tongantourism.ui.screens.essentials

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import online.codyuhi.tongantourism.data.repository.CurrencyRepository
import online.codyuhi.tongantourism.data.repository.TongaDataRepository
import online.codyuhi.tongantourism.domain.BudgetTier
import online.codyuhi.tongantourism.domain.CurrencyRate
import online.codyuhi.tongantourism.domain.Phrase
import online.codyuhi.tongantourism.domain.VisaInfo

data class EssentialsUiState(
    val amountInputTop: String = "100",
    val selectedCurrencyCode: String = "USD",
    val rates: Map<String, CurrencyRate> = emptyMap(),
    val budgetTiers: List<BudgetTier> = emptyList(),
    val visaInfo: List<VisaInfo> = emptyList(),
    val phrases: List<Phrase> = emptyList(),
    val emergencyContacts: List<Triple<String, String, String>> = emptyList()
)

class EssentialsViewModel(
    private val dataRepo: TongaDataRepository,
    private val currencyRepo: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        EssentialsUiState(
            rates = currencyRepo.defaultRates,
            budgetTiers = dataRepo.budgetTiers,
            visaInfo = dataRepo.visaInfo,
            phrases = dataRepo.phrases,
            emergencyContacts = dataRepo.emergencyContacts
        )
    )
    val uiState: StateFlow<EssentialsUiState> = _uiState.asStateFlow()

    init {
        observeCurrency()
    }

    private fun observeCurrency() {
        currencyRepo.getRates().onEach { ratesMap ->
            _uiState.value = _uiState.value.copy(rates = ratesMap)
        }.launchIn(viewModelScope)
    }

    fun setAmountInput(input: String) {
        val sanitized = input.filter { it.isDigit() || it == '.' }
        _uiState.value = _uiState.value.copy(amountInputTop = sanitized)
    }

    fun setSelectedCurrency(code: String) {
        _uiState.value = _uiState.value.copy(selectedCurrencyCode = code)
    }

    fun calculateConverted(amountTop: Double): Double {
        val state = _uiState.value
        return currencyRepo.convert(amountTop, state.selectedCurrencyCode, state.rates)
    }
}
