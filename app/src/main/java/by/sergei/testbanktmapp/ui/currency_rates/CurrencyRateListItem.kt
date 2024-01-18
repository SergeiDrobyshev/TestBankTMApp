package by.sergei.testbanktmapp.ui.currency_rates

import by.sergei.testbanktmapp.model.CurrencyRate
import java.math.BigDecimal

data class CurrencyRateListItem(
    val currencyRate: CurrencyRate,
    val diffRate: BigDecimal
)

fun CurrencyRate.diff(currencyRateForDiff: CurrencyRate?): CurrencyRateListItem {
    val currencyRateForDiffOfficialValue = (currencyRateForDiff?.curOfficialRate ?: BigDecimal.ZERO)
    return CurrencyRateListItem(
        currencyRate = this,
        diffRate = (this.curOfficialRate - currencyRateForDiffOfficialValue)
    )
}
