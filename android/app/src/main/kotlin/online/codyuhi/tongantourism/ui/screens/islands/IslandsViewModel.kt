package online.codyuhi.tongantourism.ui.screens.islands

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import online.codyuhi.tongantourism.data.repository.TongaDataRepository
import online.codyuhi.tongantourism.domain.Island

data class IslandsUiState(
    val islands: List<Island> = emptyList(),
    val selectedFilter: String = "all", // "all", "tongatapu", "vavau", "haapai", "eua"
    val selectedIslandDetail: Island? = null
)

class IslandsViewModel(
    private val dataRepo: TongaDataRepository
) : ViewModel() {

    private val allIslands = dataRepo.islands

    private val _uiState = MutableStateFlow(
        IslandsUiState(
            islands = allIslands,
            selectedFilter = "all"
        )
    )
    val uiState: StateFlow<IslandsUiState> = _uiState.asStateFlow()

    fun setFilter(filter: String) {
        val filtered = if (filter == "all") {
            allIslands
        } else {
            allIslands.filter { it.id == filter }
        }
        _uiState.value = _uiState.value.copy(
            selectedFilter = filter,
            islands = filtered
        )
    }

    fun openIslandDetail(island: Island) {
        _uiState.value = _uiState.value.copy(selectedIslandDetail = island)
    }

    fun openIslandById(islandId: String) {
        val island = allIslands.find { it.id == islandId }
        if (island != null) {
            _uiState.value = _uiState.value.copy(selectedIslandDetail = island)
        }
    }

    fun closeIslandDetail() {
        _uiState.value = _uiState.value.copy(selectedIslandDetail = null)
    }
}
