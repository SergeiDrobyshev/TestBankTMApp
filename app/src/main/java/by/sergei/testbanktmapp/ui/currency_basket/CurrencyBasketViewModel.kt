package by.sergei.testbanktmapp.ui.currency_basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class CurrencyBasketViewModel(private val interactor: CurrencyBasketInteractor): ViewModel() {
    private val _uiState = MutableStateFlow<Result<CurrencyRateBasket>?>(
        null
        //Result.success<CurrencyRateBasket>(emptyList())
    )
    val uiState: StateFlow<Result<CurrencyRateBasket>?> get() = _uiState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        refreshBasket(LocalDate.now())
    }

    fun refreshBasket(date: LocalDate) {
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = interactor.getCurrencyRatesForBasketByDate(date = date)
            _isLoading.value = false
        }
    }
}
