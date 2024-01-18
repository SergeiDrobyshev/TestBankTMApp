package by.sergei.testbanktmapp.ui.currency_basket

import java.math.BigDecimal

data class BasketCurrencyRateItem(
    val basketCurrencyLabel: String,
    val basketCurrencyRateByDate: BigDecimal,
    val basketCurrencyRateDiff: BigDecimal,
    val basketCurrencyRateDiffYesterday: BigDecimal
)
