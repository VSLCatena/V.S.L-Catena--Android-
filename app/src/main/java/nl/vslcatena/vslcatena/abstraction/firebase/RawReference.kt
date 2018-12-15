package nl.vslcatena.vslcatena.abstraction.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class RawReference(private val reference: Query): LiveData<DataSnapshot>(){
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

    fun <T> toTypedSingle(klass: Class<T>) = TypedReference.Single(this, klass)
    fun <T> toTypedList(klass: Class<T>) = TypedReference.List(this, klass)
    fun <T> toTypedMap(klass: Class<T>) = TypedReference.Map(this, klass)



    companion object {
        const val LOG_TAG = "RawRef"
    }
}