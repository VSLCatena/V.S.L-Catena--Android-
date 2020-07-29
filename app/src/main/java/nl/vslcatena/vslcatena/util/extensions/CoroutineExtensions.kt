package nl.vslcatena.vslcatena.util.extensions

import com.google.android.gms.tasks.Task
import nl.vslcatena.vslcatena.util.Result
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


suspend fun <T> Task<T>.await(): Result<T?> {
    return suspendCoroutine { continuation ->
        addOnCompleteListener { task ->
            continuation.resume(
                if (task.isSuccessful) Result.Success(task.result)
                else Result.Failure<T?>(task.exception ?: UnknownError("Something went wrong"))
            )
        }
    }
}