package nl.vslcatena.vslcatena.firebase

import android.arch.lifecycle.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.ref.WeakReference

/**
 * Created by Thomas van den Bulk on 30-4-2018.
 */
class LiveViewModel: ViewModel() {
    private val references = HashMap<String, Reference>()

    fun get(reference: String): LiveData<DataSnapshot> {
        if(!references.contains(reference)){
            references[reference] = Reference(reference)
        }
        return references[reference]!!.liveData
    }

    override fun onCleared() {
        super.onCleared()
        references.values.forEach { it.remove() }
    }

    companion object {
        const val LOG_TAG = "LiveViewModel"

        fun of(activity: FragmentActivity) = Provider(ViewModelProviders.of(activity).get(LiveViewModel::class.java), activity)
        fun of(fragment: Fragment) = Provider(ViewModelProviders.of(fragment).get(LiveViewModel::class.java), fragment)

        class Provider(_viewModel: LiveViewModel, lifecycleOwner: LifecycleOwner) {
            private val viewModel = WeakReference(_viewModel)
            private val lifecycleOwner = WeakReference(lifecycleOwner)

            fun observeRaw(reference: String, observeOnce: Boolean = false, block: (DataSnapshot?) -> Unit) {
                viewModel.get()?.let { viewModel ->
                    lifecycleOwner.get()?.let { lifecycleOwner ->
                        val liveData = viewModel.get(reference)
                        liveData.observe(lifecycleOwner, object : Observer<DataSnapshot> {
                            override fun onChanged(dataSnapshot: DataSnapshot?) {
                                block.invoke(dataSnapshot)
                                if (observeOnce) {
                                    liveData.removeObserver(this)
                                }
                            }
                        })
                    }
                }
            }

            fun <T> observe(clazz: Class<T>, observeOnce: Boolean = false, block: (T?) -> Unit){
                observeRaw(clazz.getAnnotation(FirebaseReference::class.java).singleReference, observeOnce){
                    block.invoke(if(it == null) null else Deserializer.deserialize(it, clazz))
                }
            }

            fun <T> observeList(clazz: Class<T>, observeOnce: Boolean = false, block: (List<T>) -> Unit){
                observeRaw(clazz.getAnnotation(FirebaseReference::class.java).listReference, observeOnce){
                    block.invoke(if(it == null) ArrayList() else Deserializer.deserializeList(it, clazz))
                }
            }
        }
    }

    inner class Reference(reference: String){
        private val reference = FirebaseDatabase.getInstance().getReference(reference)
        val liveData = MutableLiveData<DataSnapshot>()
        private val listener = object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d(LOG_TAG, p0?.message+" "+p0?.details)
            }

            override fun onDataChange(p0: DataSnapshot?) {
                liveData.value = p0
            }
        }

        fun remove(){
            this.reference.removeEventListener(listener)
        }

        init {
            this.reference.apply {
                keepSynced(true)
                addValueEventListener(listener)
            }
        }
    }
}