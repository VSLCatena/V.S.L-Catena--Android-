package nl.vslcatena.vslcatena.abstraction.firebase

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import nl.vslcatena.vslcatena.util.extensions.observeOnce

/**
 * Class for retrieving and updating the items that are add using their id. Makes use of the LiveViewModel.
 */
class ItemLoader<T:BaseModel> private constructor (val lifecycleOwner: LifecycleOwner, val clazz: Class<T>, private val observeOnce: Boolean = false) {

    private val provider: LiveViewModel.Companion.Provider = when (lifecycleOwner) {
        is FragmentActivity -> LiveViewModel.of(lifecycleOwner)
        is Fragment -> LiveViewModel.of(lifecycleOwner)
        else -> throw RuntimeException("The lifecycle owner must be an Activity or Fragment")
    }
    private val itemsMap = HashMap<String, MutableLiveData<T>>()


    fun addItemId(itemId: String){
        if(!itemCurrentlyInList(itemId)){
            itemsMap[itemId] = MutableLiveData()
            provider.getReference(LiveViewModel.getSingleReference(clazz, itemId)!!)
                .toTypedSingle(clazz)
                .observeOnce(lifecycleOwner, Observer {
                    itemsMap[itemId]!!.value = it
                })
        }
    }

    fun getItem(userId: String): LiveData<T>? {
        if (!itemCurrentlyInList(userId))
            addItemId(userId)
        return itemsMap[userId]
    }

    fun itemCurrentlyInList(userId: String) = itemsMap.containsKey(userId)

    companion object {
        fun<T:BaseModel> of(activity: FragmentActivity, clazz: Class<T>, observeOnce: Boolean = false) = ItemLoader(activity, clazz, observeOnce)
        fun<T:BaseModel> of(fragment: Fragment, clazz: Class<T>, observeOnce: Boolean = false) = ItemLoader(fragment, clazz, observeOnce)
    }
}