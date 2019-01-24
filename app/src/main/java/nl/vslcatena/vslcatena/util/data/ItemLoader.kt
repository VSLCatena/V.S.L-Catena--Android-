package nl.vslcatena.vslcatena.util.data

import androidx.lifecycle.LiveData
import nl.vslcatena.vslcatena.models.Identifier

/**
 * Class for retrieving and updating the items that are add using their id. Makes use of the LiveViewModel.
 */
class ItemLoader<T : BaseModel>(val clazz: Class<T>) {

    private val itemsMap = HashMap<Identifier, LiveData<T>>()


    fun addItemId(itemId: Identifier) {
        if (!contains(itemId)) {
            itemsMap[itemId] = DataCreator.getSingleReference(clazz, itemId)
        }
    }

    fun getItem(userId: Identifier): LiveData<T>? {
        if (!contains(userId))
            addItemId(userId)
        return itemsMap[userId]
    }

    fun contains(userId: Identifier) = itemsMap.containsKey(userId)

}