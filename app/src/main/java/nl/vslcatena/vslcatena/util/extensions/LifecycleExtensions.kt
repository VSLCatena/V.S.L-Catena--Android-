package nl.vslcatena.vslcatena.util.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
    observe(owner, object : Observer<T> {
        override fun onChanged(data: T) {
            observer.onChanged(data)
            removeObserver(this)
        }
    })
}

suspend fun <T> LiveData<T>.awaitFirstObservation(): T? {
    return suspendCoroutine { continuation ->
        var observer: Observer<T>? = null
        observer = Observer {
            removeObserver(observer!!)
            continuation.resume(it)
        }
        observeForever(observer)
    }
}