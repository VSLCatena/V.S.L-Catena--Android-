package nl.vslcatena.vslcatena.firebase

import android.arch.lifecycle.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.firebase.database.*
import java.lang.ref.WeakReference

/**
 * Created by Thomas van den Bulk on 30-4-2018.
 */
class LiveViewModel: ViewModel() {
    private val references = HashMap<String, Reference>()

    fun get(reference: Query): LiveData<DataSnapshot> {
        if(!references.contains(reference.toString())){
            references[reference.toString()] = Reference(reference)
        }
        return references[reference.toString()]!!.liveData
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
            private var enabledOnly = false
            private var filter: (DatabaseReference) -> Query = { it }

            fun getViewModel() = viewModel.get()

            private fun getReference(reference: String): Query {
                val fbReference = FirebaseDatabase.getInstance().getReference(reference)
                return if(enabledOnly) {
                    fbReference.child("enabled").equalTo(true)
                } else filter.invoke(fbReference)
            }

            fun filter(filter: (DatabaseReference) -> Query): Provider {
                this.filter = filter
                return this
            }

            fun observeRaw(reference: String,
                           observeOnce: Boolean = false,
                           enabledOnly: Boolean = false,
                           block: (DataSnapshot?) -> Unit) {
                this.enabledOnly = enabledOnly
                viewModel.get()?.let { viewModel ->
                    lifecycleOwner.get()?.let { lifecycleOwner ->
                        val liveData = viewModel.get(getReference(reference))
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

            fun <T> observeSingle(clazz: Class<T>,
                            objectId: String? = null,
                            reference: String = clazz.getAnnotation(FirebaseReference::class.java).singleReference,
                            observeOnce: Boolean = false,
                            block: (T?) -> Unit){
                val iReference = if(objectId == null) reference else reference.format(objectId)
                observeRaw(iReference, observeOnce){
                    block.invoke(if(it == null || !it.exists()) null else Deserializer.deserialize(it, clazz))
                }
            }

            fun <T> observeMap(clazz: Class<T>,
                               reference: String = clazz.getAnnotation(FirebaseReference::class.java).listReference,
                               observeOnce: Boolean = false,
                               enabledOnly: Boolean = false,
                               block: (Map<String, T>) -> Unit){
                this.enabledOnly = enabledOnly
                observeRaw(reference, observeOnce){
                    block.invoke(if(it == null || !it.exists()) HashMap() else Deserializer.deserializeMap(it, clazz))
                }
            }

            fun <T> observe(clazz: Class<T>,
                                reference: String = clazz.getAnnotation(FirebaseReference::class.java).listReference,
                                observeOnce: Boolean = false,
                                enabledOnly: Boolean = false,
                                block: (List<T>) -> Unit){
                this.enabledOnly = enabledOnly
                observeRaw(reference, observeOnce){
                    block.invoke(if(it == null || !it.exists()) ArrayList() else Deserializer.deserializeList(it, clazz))
                }
            }
        }
    }

    inner class Reference(private val reference: Query){
        val liveData = MutableLiveData<DataSnapshot>()
        private val listener = object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(LOG_TAG, p0.message+" "+p0.details)
            }

            override fun onDataChange(p0: DataSnapshot) {
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