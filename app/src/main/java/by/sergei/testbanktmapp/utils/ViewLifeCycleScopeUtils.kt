package by.sergei.testbanktmapp.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun <T> CoroutineScope.observeOnRepeatedLifeCycle(
    lifecycleOwner: LifecycleOwner,
    state: StateFlow<T>,
    block: (T) -> Unit
): Job = this.launch {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        state.collect(block)
    }
}