package nl.vslcatena.vslcatena.abstraction.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot

sealed class TypedReference<T, R>(private val raw: RawReference, internal val klass: Class<T>) : LiveData<R>() {
    private val listener = Observer<DataSnapshot> {
        if(it == null) {
            value = null
            return@Observer
        }

        value = deserialize(it)
    }

    override fun onActive() {
        raw.observeForever(listener)
    }

    override fun onInactive() {
        raw.removeObserver(listener)
    }

    internal abstract fun deserialize(dataSnapshot: DataSnapshot): R

    class Single<T>(
        raw: RawReference, klass: Class<T>
    ) : TypedReference<T, T>(raw, klass) {
        override fun deserialize(dataSnapshot: DataSnapshot) =
            Deserializer.deserialize(dataSnapshot, klass)
    }

    class List<T>(
        raw: RawReference, klass: Class<T>
    ) : TypedReference<T, kotlin.collections.List<T>>(raw, klass) {
        override fun deserialize(dataSnapshot: DataSnapshot) =
            Deserializer.deserializeList(dataSnapshot, klass)
    }

    class Map<T>(
        raw: RawReference, klass: Class<T>
    ) : TypedReference<T, kotlin.collections.Map<String, T>>(raw, klass) {
        override fun deserialize(dataSnapshot: DataSnapshot) =
            Deserializer.deserializeMap(dataSnapshot, klass)
    }
}