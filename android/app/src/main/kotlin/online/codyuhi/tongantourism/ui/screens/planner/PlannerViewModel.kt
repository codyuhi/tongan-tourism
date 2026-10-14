package online.codyuhi.tongantourism.ui.screens.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import online.codyuhi.tongantourism.data.repository.ItineraryRepository
import online.codyuhi.tongantourism.domain.SavedItinerary

data class PlannerUiState(
    val selectedDuration: Int = 7,
    val selectedIslands: Set<String> = setOf("tongatapu", "vavau"),
    val selectedVibe: String = "Balanced Explorer",
    val generatedItinerary: SavedItinerary? = null,
    val savedItineraries: List<SavedItinerary> = emptyList(),
    val isSavedSuccess: Boolean = false
)

class PlannerViewModel(
    private val itineraryRepo: ItineraryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlannerUiState())
    val uiState: StateFlow<PlannerUiState> = _uiState.asStateFlow()

    init {
        observeSavedItineraries()
        generate()
    }

    private fun observeSavedItineraries() {
        itineraryRepo.savedItineraries.onEach { list ->
            _uiState.value = _uiState.value.copy(savedItineraries = list)
        }.launchIn(viewModelScope)
    }

    fun setDuration(days: Int) {
        _uiState.value = _uiState.value.copy(selectedDuration = days, isSavedSuccess = false)
    }

    fun toggleIsland(islandId: String) {
        val current = _uiState.value.selectedIslands.toMutableSet()
        if (current.contains(islandId)) {
            if (current.size > 1) { // Keep at least 1 island
                current.remove(islandId)
            }
        } else {
            current.add(islandId)
        }
        _uiState.value = _uiState.value.copy(selectedIslands = current, isSavedSuccess = false)
    }

    fun setVibe(vibe: String) {
        _uiState.value = _uiState.value.copy(selectedVibe = vibe, isSavedSuccess = false)
    }

    fun generate() {
        val s = _uiState.value
        val itin = itineraryRepo.generateItinerary(
            daysCount = s.selectedDuration,
            selectedIslands = s.selectedIslands.toList(),
            vibe = s.selectedVibe
        )
        _uiState.value = _uiState.value.copy(
            generatedItinerary = itin,
            isSavedSuccess = false
        )
    }

    fun saveCurrentItinerary() {
        val itin = _uiState.value.generatedItinerary ?: return
        viewModelScope.launch {
            itineraryRepo.saveItinerary(itin)
            _uiState.value = _uiState.value.copy(isSavedSuccess = true)
        }
    }

    fun deleteItinerary(id: String) {
        viewModelScope.launch {
            itineraryRepo.deleteItinerary(id)
        }
    }
}
