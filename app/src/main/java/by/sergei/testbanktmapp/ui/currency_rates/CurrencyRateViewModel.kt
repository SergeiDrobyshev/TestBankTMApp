package by.sergei.testbanktmapp.ui.currency_rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class CurrencyRateViewModel(private val interactor: CurrencyRatesInteractor): ViewModel() {

    val today: LocalDate get() = interactor.today
    private val _uiState = MutableStateFlow(Result.success<List<CurrencyRateListItem>>(emptyList()))
    val uiState: StateFlow<Result<List<CurrencyRateListItem>>> get() = _uiState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        refresh()
    }

    fun refresh(){
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = interactor.getRatesListForToday()
            _isLoading.value = false
        }
    }
}
