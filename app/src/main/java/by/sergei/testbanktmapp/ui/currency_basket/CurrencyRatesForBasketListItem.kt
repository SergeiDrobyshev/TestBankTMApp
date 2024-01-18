package by.sergei.testbanktmapp.ui.currency_basket

import by.sergei.testbanktmapp.model.CurrencyRate
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

data class CurrencyRatesForBasketListItem(
    val currencyRate: CurrencyRate,
    val diffRateByHardDatePercent: BigDecimal,
    val diffRateByDateYesterdayPercent: BigDecimal
)

private fun CurrencyRate.diffCurrencyRatesForBasket(
    currencyRateForHardDateDiff: CurrencyRate?,
    currencyRateForYesterdayDiff: CurrencyRate?
): CurrencyRatesForBasketListItem {
    val currencyRateBasketForDiffOfficialValue =
        (currencyRateForYesterdayDiff?.curOfficialRate ?: BigDecimal.ZERO)
    val currencyRateBasketForHardDateDiffOfficialValue =
        (currencyRateForHardDateDiff?.curOfficialRate ?: BigDecimal.ZERO)
    return CurrencyRatesForBasketListItem(
        currencyRate = this,
        diffRateByHardDatePercent = calcDiffRatePercent(
            rateByDate = this.curOfficialRate,
            rateByDiffDate = currencyRateBasketForHardDateDiffOfficialValue
        ),
        diffRateByDateYesterdayPercent = calcDiffRatePercent(
            rateByDate = this.curOfficialRate,
            rateByDiffDate = currencyRateBasketForDiffOfficialValue
        )
    )
}

private fun calcDiffRatePercent(rateByDate: BigDecimal, rateByDiffDate: BigDecimal): BigDecimal {
    return (rateByDiffDate - rateByDate).divide(rateByDate, RoundingMode.FLOOR) * BigDecimal(100)
}

data class CurrencyRateBasket(
    val header: Header,
    val ratesItems: List<CurrencyRatesForBasketListItem>
) {
    data class Header(
        val label: String,
        val rateBasketByDate: BigDecimal,
        val diffRateHardDate: BigDecimal,
        val diffRateByDateYesterday: BigDecimal
    )
}

private fun calculateBasket(list: List<Pair<CurrencyRate, Int>>): BigDecimal {
    return list.map { (currencyRate, fractionPower) ->
        calculateFraction(
            currencyRate,
            fractionPower
        )
    }
        .fold(BigDecimal.ONE) { accumulator, element -> accumulator * element }
}

private fun calculateFraction(currencyRate: CurrencyRate, fractionPower: Int): BigDecimal {
    val poweredBigDecimal =
        currencyRate.curOfficialRate.divide(BigDecimal(currencyRate.curScale), RoundingMode.FLOOR)
            .pow(fractionPower)
    val fractionDouble = poweredBigDecimal.toDouble().pow(0.1)
    return fractionDouble.toBigDecimal()
}

private fun calculateBasketHeader(
    currencies: List<Pair<Long, Int>>,
    currencyRatesByDateMap: Map<Long, CurrencyRate>,
    currencyRatesByHardDateMap: Map<Long, CurrencyRate>,
    currencyRatesByDateYesterdayMap: Map<Long, CurrencyRate>
): CurrencyRateBasket.Header {
    val basketForDate = calculateBasket(list = currencies.mapNotNull { (curId, fractionPower) ->
        currencyRatesByDateMap[curId]?.to(fractionPower)
    })
    val basketForHardDate = calculateBasket(list = currencies.mapNotNull { (curId, fractionPower) ->
        currencyRatesByHardDateMap[curId]?.to(fractionPower)
    })
    val basketForDateYesterday =
        calculateBasket(list = currencies.mapNotNull { (curId, fractionPower) ->
            currencyRatesByDateYesterdayMap[curId]?.to(fractionPower)
        })

    return CurrencyRateBasket.Header(
        label = "корзина валют",
        rateBasketByDate = basketForDate,
        diffRateHardDate = calcDiffRatePercent(rateByDate = basketForDate, rateByDiffDate = basketForHardDate),
        diffRateByDateYesterday = calcDiffRatePercent(rateByDate = basketForDate, rateByDiffDate = basketForDateYesterday)
    )
}

internal fun currencyRateBasket(
    listCurrenciesForBasket: List<Pair<Long, Int>>,
    currencyRatesByDateMap: Map<Long, CurrencyRate>,
    currencyRatesByHardDateMap: Map<Long, CurrencyRate>,
    currencyRatesByDateYesterdayMap: Map<Long, CurrencyRate>
): CurrencyRateBasket {
    val basketHeader = calculateBasketHeader(
        currencies = listCurrenciesForBasket,
        currencyRatesByDateMap = currencyRatesByDateMap,
        currencyRatesByHardDateMap = currencyRatesByHardDateMap,
        currencyRatesByDateYesterdayMap = currencyRatesByDateYesterdayMap
    )

    val listItems = listCurrenciesForBasket.mapNotNull { (currencyId, fractionPower) ->
        val dateCurrencyDate = currencyRatesByDateMap[currencyId]
        val hardDateCurrencyDate = currencyRatesByHardDateMap[currencyId]
        val yesterdayCurrencyRate = currencyRatesByDateYesterdayMap[currencyId]
        dateCurrencyDate?.diffCurrencyRatesForBasket(
            currencyRateForHardDateDiff = hardDateCurrencyDate,
            currencyRateForYesterdayDiff = yesterdayCurrencyRate,
        )
    }
    val basket = CurrencyRateBasket(header = basketHeader, ratesItems = listItems)
    return basket
}
