package nl.vslcatena.vslcatena.abstraction.firebase

import androidx.lifecycle.Observer
import nl.vslcatena.vslcatena.abstraction.fragment.BaseFragment

fun BaseFragment.getReference(reference: String): RawReference {
    return LiveViewModel.of(this).getReference(reference)
}

inline fun <reified T> BaseFragment.observeSingle(
    objectId: String?,
    observer: Observer<T>
) {
    getReference(LiveViewModel.getSingleReference(T::class.java, objectId) ?: "")
        .toTypedSingle(T::class.java)
        .observe(this, observer)
}

inline fun <reified T: Any> BaseFragment.observeList(
    observer: Observer<List<T>>
) {
    getReference(LiveViewModel.getCollectionReference(T::class.java) ?: "")
        .toTypedList(T::class.java)
        .observe(this, observer)
}

inline fun <reified T: Any> BaseFragment.observeMap(
    observer: Observer<Map<String, T>>
) {
    getReference(LiveViewModel.getCollectionReference(T::class.java) ?: "")
        .toTypedMap(T::class.java)
        .observe(this, observer)
}