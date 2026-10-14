package online.codyuhi.tongantourism.ui.screens.culture

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import online.codyuhi.tongantourism.data.repository.TongaDataRepository
import online.codyuhi.tongantourism.domain.CultureItem

data class CultureUiState(
    val items: List<CultureItem> = emptyList(),
    val selectedItem: CultureItem? = null
)

class CultureViewModel(
    private val dataRepo: TongaDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CultureUiState(
            items = dataRepo.culture,
            selectedItem = dataRepo.culture.firstOrNull()
        )
    )
    val uiState: StateFlow<CultureUiState> = _uiState.asStateFlow()

    fun selectItem(item: CultureItem) {
        _uiState.value = _uiState.value.copy(selectedItem = item)
    }
}
