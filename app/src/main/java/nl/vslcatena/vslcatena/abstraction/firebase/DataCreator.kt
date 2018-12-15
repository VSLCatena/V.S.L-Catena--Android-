package nl.vslcatena.vslcatena.abstraction.firebase

import android.provider.ContactsContract
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query

class DataCreator {
    fun getRawReference(
        reference: String,
        filter: (DatabaseReference) -> Query = { it }
    ): RawReference {
        return RawReference(LiveViewModel.createQuery(reference, filter))
    }

    fun <T> getSingleReference(
        klass: Class<T>,
        objectId: String? = null
    ): TypedReference.Single<T> {
        var reference = klass.getAnnotation(DataReference::class.java)!!.singleReference
        if (objectId == null) reference = reference.format(objectId)

        return getRawReference(reference).toTypedSingle(klass)
    }

    fun <T> getListReference(
        klass: Class<T>,
        filter: (DatabaseReference) -> Query = { it },
        enabledOnly: Boolean = false
    ): TypedReference.List<T> {
        val reference = klass.getAnnotation(DataReference::class.java)!!.listReference

        val newFilter = if (enabledOnly) {
            { it.child("enabled").equalTo(true) }
        } else filter

        return getRawReference(reference, newFilter).toTypedList(klass)
    }

    fun <T> getMapReference(
        klass: Class<T>,
        filter: (DatabaseReference) -> Query = { it },
        enabledOnly: Boolean = false
    ): TypedReference.Map<T> {
        val reference = klass.getAnnotation(DataReference::class.java)!!.listReference

        val newFilter = if (enabledOnly) {
            { it.child("enabled").equalTo(true) }
        } else filter

        return getRawReference(reference, newFilter).toTypedMap(klass)
    }
}