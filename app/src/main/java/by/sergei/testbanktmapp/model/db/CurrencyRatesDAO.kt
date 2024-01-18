package by.sergei.testbanktmapp.model.db

import androidx.room.Query
import by.sergei.testbanktmapp.model.db.entity.CurrencyRateDbDto
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import org.threeten.bp.LocalDate

@Dao
interface CurrencyRatesDAO {
    @Query("SELECT * FROM ${CurrencyRateDbDto.TABLE_NAME} WHERE ${CurrencyRateDbDto.DATE} = :date")
    suspend fun getRates(date: LocalDate): List<CurrencyRateDbDto>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listRates: List<CurrencyRateDbDto>)

    @Query("SELECT * FROM ${CurrencyRateDbDto.TABLE_NAME} WHERE ${CurrencyRateDbDto.CUR_ID} = :curId AND ${CurrencyRateDbDto.DATE} = :date")
    suspend fun getRateById(curId: Long, date: LocalDate): CurrencyRateDbDto

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertById(rate: CurrencyRateDbDto)
}