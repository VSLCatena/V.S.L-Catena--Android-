package nl.vslcatena.vslcatena.util.data

import androidx.lifecycle.Observer
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import nl.vslcatena.vslcatena.BaseFragment
import nl.vslcatena.vslcatena.models.Identifier

inline fun <reified T> BaseFragment.observeSingle(
    objectId: String? = "",
    observer: Observer<T>
): Reference.Single<T> {
    val id =
        if (objectId != null) Identifier(objectId)
        else null

    return observeSingleId(id, observer)
}

inline fun <reified T> BaseFragment.observeSingleId(
    objectId: Identifier? = Identifier(""),
    observer: Observer<T>
): Reference.Single<T> {
    return DataCreator.getSingleReference(T::class.java, objectId).also {
        it.observe(this, observer)
    }
}

inline fun <reified T: Any> BaseFragment.observeList(
    noinline filter: (CollectionReference) -> Query = { it },
    observer: Observer<List<T>>
): Reference.List<T> {
    return DataCreator.getListReference(T::class.java, filter).also {
        it.observe(this, observer)
    }
}