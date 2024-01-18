package by.sergei.testbanktmapp.model

import by.sergei.testbanktmapp.data.CurrencyApiInterface
import by.sergei.testbanktmapp.data.response.CurrencyRateDto
import by.sergei.testbanktmapp.data.response.toDomain
import by.sergei.testbanktmapp.model.db.CurrencyRatesDAO
import by.sergei.testbanktmapp.model.db.entity.CurrencyRateDbDto
import by.sergei.testbanktmapp.model.db.entity.toDomain
import by.sergei.testbanktmapp.model.db.entity.toDbDto
import by.sergei.testbanktmapp.utils.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class CurrencyRatesRepository(
    private val currencyApiInterface: CurrencyApiInterface,
    private val currencyRatesDAO: CurrencyRatesDAO
) {

    suspend fun getCurrencyRates(date: LocalDate): Result<List<CurrencyRate>> =
        withContext(Dispatchers.IO) {
            val dbResult = getCurrencyRatesFromDB(date)
                .getOrDefault(emptyList())
            if (dbResult.isNotEmpty()) {
                Result.success(dbResult)
            } else {
                getAndStoreCurrencyRatesFromRemote(date)
            }
        }

    private suspend fun insertAllRates(listRates: List<CurrencyRateDbDto>): Result<Unit> =
        withContext(Dispatchers.IO) {
            runSuspendCatching {
                currencyRatesDAO.insertAll(
                    listRates
                )
            }
        }

    private suspend fun getCurrencyRatesFromDB(date: LocalDate): Result<List<CurrencyRate>> =
        withContext(Dispatchers.IO) {
            val result = runSuspendCatching { currencyRatesDAO.getRates(date) }
            result.map { list -> list.map(CurrencyRateDbDto::toDomain) }
        }

    private suspend fun getAndStoreCurrencyRatesFromRemote(date: LocalDate): Result<List<CurrencyRate>> =
        withContext(Dispatchers.IO) {
            val remoteResult = getCurrencyRatesFromRemote(date)
            if (remoteResult.isSuccess) {
                val dbRecords = remoteResult.getOrThrow().map { rate -> rate.toDbDto(date) }
                insertAllRates(dbRecords)
            }
            remoteResult
        }

    private suspend fun getCurrencyRatesFromRemote(date: LocalDate): Result<List<CurrencyRate>> =
        withContext(Dispatchers.IO) {
            runSuspendCatching {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                currencyApiInterface.getCurrencyRatesOnDate(formatter.format(date))
                    .map(CurrencyRateDto::toDomain)
            }
        }


    //получения курса одной валюты по id

    /*suspend fun getCurrencyRateById(curId: Long, date: LocalDate): Result<CurrencyRate> =
        withContext(Dispatchers.IO) {
            val dbResult = getCurrencyRateByIdFromDB(curId, date)
            if (dbResult.isSuccess) {
                Result.success(dbResult.getOrThrow())
            } else {
                getAndStoreCurrencyRateByIdFromRemote(curId, date)
            }
        }

    private suspend fun getCurrencyRateByIdFromDB(
        curId: Long,
        date: LocalDate
    ): Result<CurrencyRate> =
        withContext(Dispatchers.IO) {
            val result = runSuspendCatching { currencyRatesDAO.getRateById(curId, date) }
            result.map { it.toDomain() }
        }

    private suspend fun getAndStoreCurrencyRateByIdFromRemote(
        curId: Long,
        date: LocalDate
    ): Result<CurrencyRate> =
        withContext(Dispatchers.IO) {
            val remoteResult = runSuspendCatching {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                currencyApiInterface.getCurrencyRateByCurId(curId, formatter.format(date))
                    .toDomain()
            }
            if (remoteResult.isSuccess) {
                val dbRecords = remoteResult.getOrThrow().toDbDto(date)
                insertRateById(dbRecords)
            }
            remoteResult
        }

    private suspend fun insertRateById(rate: CurrencyRateDbDto): Result<Unit> =
        withContext(Dispatchers.IO) {
            runSuspendCatching {
                currencyRatesDAO.insertById(rate)
            }
        }*/
}