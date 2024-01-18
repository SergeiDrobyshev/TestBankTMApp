package by.sergei.testbanktmapp.model

import java.math.BigDecimal

data class CurrencyRate(
    val curId: Long,
    val curName: String,
    val curScale: Int,
    val curOfficialRate: BigDecimal
)
