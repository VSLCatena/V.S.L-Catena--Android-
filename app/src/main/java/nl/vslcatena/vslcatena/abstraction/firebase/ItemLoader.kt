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
class ItemLoader<T:BaseModel>(val clazz: Class<T>) {

    private val dataCreator = DataCreator()
    private val itemsMap = HashMap<String, LiveData<T>>()


    fun addItemId(itemId: String){
        if(!contains(itemId)){
            itemsMap[itemId] = dataCreator.getSingleReference(clazz, itemId)
        }
    }

    fun getItem(userId: String): LiveData<T>? {
        if (!contains(userId))
            addItemId(userId)
        return itemsMap[userId]
    }

    fun contains(userId: String) = itemsMap.containsKey(userId)

}