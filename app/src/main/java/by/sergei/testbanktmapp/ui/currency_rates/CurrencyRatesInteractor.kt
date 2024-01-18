package by.sergei.testbanktmapp.ui.currency_rates

import by.sergei.testbanktmapp.model.CurrencyRate
import by.sergei.testbanktmapp.model.CurrencyRatesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.threeten.bp.LocalDate
import java.lang.IllegalStateException

class CurrencyRatesInteractor(private val currencyRatesRepository: CurrencyRatesRepository) {

    val today = LocalDate.now()
    private val yesterday = today.minusDays(1)

    suspend fun getRatesListForToday(): Result<List<CurrencyRateListItem>> = coroutineScope {
        val todayCurrencyRatesDeferredResult =
            async { currencyRatesRepository.getCurrencyRates(today) }
        val yesterdayCurrencyDeferredRatesResult =
            async { currencyRatesRepository.getCurrencyRates(yesterday) }

        val todayCurrencyRatesResult = todayCurrencyRatesDeferredResult.await()
        val yesterdayCurrencyRatesResult = yesterdayCurrencyDeferredRatesResult.await()

        if (todayCurrencyRatesResult.isSuccess && yesterdayCurrencyRatesResult.isSuccess) {
            val yesterdayCurrencyRatesMap =
                yesterdayCurrencyRatesResult.getOrThrow().associateBy(CurrencyRate::curId)
            val listItems = todayCurrencyRatesResult.getOrThrow().map { todaySingleCurrencyRate ->
                val yesterdaySingleCurrencyRate =
                    yesterdayCurrencyRatesMap[todaySingleCurrencyRate.curId]
                todaySingleCurrencyRate.diff(currencyRateForDiff = yesterdaySingleCurrencyRate)
            }
            Result.success(listItems)
        } else {
            Result.failure(IllegalStateException())
        }
    }
}