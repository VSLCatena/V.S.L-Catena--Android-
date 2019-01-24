package nl.vslcatena.vslcatena.util.data

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*

sealed class Reference<S> : LiveData<S>() {
    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        listenerRegistration = registerListener()
    }

    override fun onInactive() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }

    abstract fun registerListener(): ListenerRegistration


    class Single<T>(
        private val raw: DocumentReference,
        private val klass: Class<T>
    ) : Reference<T>(), EventListener<DocumentSnapshot> {
        override fun onEvent(p0: DocumentSnapshot?, p1: FirebaseFirestoreException?) {
            value = if (p0 == null) null
            else Deserializer.deserialize(p0, klass)
        }

        override fun registerListener() = raw.addSnapshotListener(this)
    }

    class List<T>(
        private val raw: Query,
        private val klass: Class<T>
    ) : Reference<kotlin.collections.List<T>>(), EventListener<QuerySnapshot> {
        override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
            value = if (p0 == null) emptyList()
            else Deserializer.deserializeList(p0, klass)
        }

        override fun registerListener() = raw.addSnapshotListener(this)
    }
}