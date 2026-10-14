package online.codyuhi.tongantourism.ui.screens.whales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.codyuhi.tongantourism.audio.WhaleSoundEngine
import online.codyuhi.tongantourism.data.repository.TongaDataRepository
import online.codyuhi.tongantourism.domain.WhaleData

data class WhalesUiState(
    val whaleData: WhaleData,
    val isPlayingSound: Boolean = false
)

class WhalesViewModel(
    private val dataRepo: TongaDataRepository,
    private val soundEngine: WhaleSoundEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        WhalesUiState(whaleData = dataRepo.whales)
    )
    val uiState: StateFlow<WhalesUiState> = _uiState.asStateFlow()

    fun playWhaleSong() {
        if (_uiState.value.isPlayingSound) return
        _uiState.value = _uiState.value.copy(isPlayingSound = true)
        soundEngine.playWhaleSong()

        viewModelScope.launch {
            delay(3200)
            _uiState.value = _uiState.value.copy(isPlayingSound = false)
        }
    }
}
