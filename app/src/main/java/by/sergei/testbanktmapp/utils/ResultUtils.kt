package by.sergei.testbanktmapp.utils

import kotlin.coroutines.cancellation.CancellationException

inline fun <R> runSuspendCatching( block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}