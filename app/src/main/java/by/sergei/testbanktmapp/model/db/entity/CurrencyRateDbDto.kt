package by.sergei.testbanktmapp.model.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.sergei.testbanktmapp.model.CurrencyRate
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import org.threeten.bp.LocalDate

@Entity(tableName = CurrencyRateDbDto.TABLE_NAME)
data class CurrencyRateDbDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CurrencyRateDbDto.ID)
    val id: Long = 0,
    @ColumnInfo(name = CurrencyRateDbDto.CUR_ID)
    val curId: Long,
    @ColumnInfo(name = CurrencyRateDbDto.CUR_NAME)
    val curName: String,
    @SerializedName("Cur_Scale")
    val curScale: Int,
    @ColumnInfo(name = CurrencyRateDbDto.CUR_OFFICIAL_RATE)
    val curOfficialRate: BigDecimal,
    @ColumnInfo(name = CurrencyRateDbDto.DATE)
    val date: LocalDate
) {
    companion object {
        const val TABLE_NAME = "rates"
        const val ID = "id"
        const val CUR_ID = "curId"
        const val CUR_NAME = "curName"
        const val CUR_OFFICIAL_RATE = "curOfficialRate"
        const val DATE = "date"
    }
}

fun CurrencyRateDbDto.toDomain(): CurrencyRate =
    CurrencyRate(curId = this.curId, curName = this.curName, curScale = this.curScale, curOfficialRate = this.curOfficialRate)

fun CurrencyRate.toDbDto(date: LocalDate): CurrencyRateDbDto =
    CurrencyRateDbDto(
        curId = this.curId,
        curName = this.curName,
        curScale = this.curScale,
        curOfficialRate = this.curOfficialRate,
        date = date
    )
