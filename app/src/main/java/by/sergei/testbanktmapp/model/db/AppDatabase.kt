package by.sergei.testbanktmapp.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.sergei.testbanktmapp.model.db.entity.CurrencyRateDbDto
import by.sergei.testbanktmapp.utils.BigDecimalDBConverter
import by.sergei.testbanktmapp.utils.LocalDateDbConverter

@Database(entities = [CurrencyRateDbDto::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateDbConverter::class, BigDecimalDBConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAppDao(): CurrencyRatesDAO

    companion object {
        fun getAppDBInstance(context: Context): AppDatabase {
               return Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "APP_DB"
                )
                    .build()
        }
    }
}