package by.sergei.testbanktmapp.utils

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalDBConverter {
    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.toBigDecimal()
    }

    @TypeConverter
    fun bigDecimalToString(value: BigDecimal?): String? {
        return value.toString()
    }
}