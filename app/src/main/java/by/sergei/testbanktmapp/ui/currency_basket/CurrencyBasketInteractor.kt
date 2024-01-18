package by.sergei.testbanktmapp.ui.currency_basket

import by.sergei.testbanktmapp.model.CurrencyRate
import by.sergei.testbanktmapp.model.CurrencyRatesRepository
import by.sergei.testbanktmapp.utils.Currencies
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

class CurrencyBasketInteractor(private val currencyRatesRepository: CurrencyRatesRepository) {

    private val hardDate: LocalDate = LocalDate.of(2022, Month.DECEMBER, 31)

    suspend fun getCurrencyRatesForBasketByDate(date: LocalDate): Result<CurrencyRateBasket> =
        coroutineScope {
            val currencyRatesByDateDeferredResult =
                async { currencyRatesRepository.getCurrencyRates(date) }
            val currencyRatesByHardDateDeferredResult =
                async { currencyRatesRepository.getCurrencyRates(hardDate) }
            val currencyRatesByDateYesterdayDeferredResult =
                async { currencyRatesRepository.getCurrencyRates(date.minusDays(1)) }

            val currencyRatesByDateResult = currencyRatesByDateDeferredResult.await()
            val currencyRatesByHardDateResult = currencyRatesByHardDateDeferredResult.await()
            val currencyRatesByDateYesterdayResult =
                currencyRatesByDateYesterdayDeferredResult.await()

            if (currencyRatesByDateResult.isSuccess && currencyRatesByHardDateResult.isSuccess && currencyRatesByDateYesterdayResult.isSuccess) {
                val listCurrenciesForBasket =
                    listOf(Currencies.RUB to 6, Currencies.USD to 3, Currencies.CNY to 1)

                val currencyRatesByDateMap =
                    currencyRatesByDateResult.getOrThrow().associateBy(CurrencyRate::curId)
                val currencyRatesByHardDateMap =
                    currencyRatesByHardDateResult.getOrThrow().associateBy(CurrencyRate::curId)
                val currencyRatesByDateYesterdayMap =
                    currencyRatesByDateYesterdayResult.getOrThrow()
                        .associateBy(CurrencyRate::curId)

                val basket = currencyRateBasket(
                    listCurrenciesForBasket,
                    currencyRatesByDateMap,
                    currencyRatesByHardDateMap,
                    currencyRatesByDateYesterdayMap
                )
                Result.success(basket)
            } else {
                Result.failure(java.lang.IllegalStateException())
            }
        }
}


