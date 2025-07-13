package com.imtiaz.dictify.presentation.screen.mainscreen.history


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtiaz.dictify.data.local.entity.DailyWordEntity
import com.imtiaz.dictify.domain.dictionary.DictionaryRepository // Assuming you have a DictionaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository // Inject your DictionaryRepository
) : ViewModel() {

    // StateFlow to expose the list of daily words for the UI to observe
    val dailyWords: StateFlow<List<DailyWordEntity>> =
        dictionaryRepository.getAllDailyWords() // This should be a Flow<List<DailyWordEntity>> from your DAO
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000), // Keep the flow alive as long as there are active subscribers
                initialValue = emptyList() // Initial empty list
            )
}