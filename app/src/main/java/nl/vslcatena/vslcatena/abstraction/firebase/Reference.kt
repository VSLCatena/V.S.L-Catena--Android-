package nl.vslcatena.vslcatena.abstraction.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

sealed class Reference<T> : LiveData<T>() {
    class RawReference(private val reference: Query): Reference<DataSnapshot>(){
        private val listener = object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                value = p0
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d(LOG_TAG, p0.message+" "+p0.details)
            }
        }

        override fun onActive() {
            reference.addValueEventListener(listener)
        }

        override fun onInactive() {
            reference.removeEventListener(listener)
        }

        fun <T> toTypedSingle(klass: Class<T>) = Typed.Single(this, klass)
        fun <T> toTypedList(klass: Class<T>) = Typed.List(this, klass)
        fun <T> toTypedMap(klass: Class<T>) = Typed.Map(this, klass)
    }

    sealed class Typed<T, R>(private val raw: RawReference, internal val klass: Class<T>) : Reference<R>() {
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

        class Single<T>(raw: RawReference, klass: Class<T>) : Typed<T, T>(raw, klass) {
            override fun deserialize(dataSnapshot: DataSnapshot) =
                    Deserializer.deserialize(dataSnapshot, klass)
        }

        class List<T>(raw: RawReference, klass: Class<T>) : Typed<T, kotlin.collections.List<T>>(raw, klass) {
            override fun deserialize(dataSnapshot: DataSnapshot) =
                    Deserializer.deserializeList(dataSnapshot, klass)
        }

        class Map<T>(raw: RawReference, klass: Class<T>) : Typed<T, kotlin.collections.Map<String, T>>(raw, klass) {
            override fun deserialize(dataSnapshot: DataSnapshot) =
                    Deserializer.deserializeMap(dataSnapshot, klass)
        }
    }

    companion object {
        const val LOG_TAG = "Reference"
    }
}