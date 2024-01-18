package by.sergei.testbanktmapp.data.response

import by.sergei.testbanktmapp.model.CurrencyRate
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CurrencyRateDto(
    @SerializedName("Cur_ID")
    val curId: Long,
    @SerializedName("Cur_Name")
    val curName: String,
    @SerializedName("Cur_Scale")
    val curScale: Int,
    @SerializedName("Cur_OfficialRate")
    val curOfficialRate: BigDecimal
)

fun CurrencyRateDto.toDomain(): CurrencyRate =
    CurrencyRate(curId = this.curId, curName = this.curName, curScale = this.curScale, curOfficialRate = this.curOfficialRate)

