package nl.vslcatena.vslcatena.util.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

/**
 * Created by Thomas van den Bulk on 30-4-2018.
 */
object Deserializer {
    fun <T> deserialize(dataDocument: DocumentSnapshot, clazz: Class<T>): T {
        val obj: T = clazz.newInstance()

        clazz.declaredFields.forEach { field ->
            if (field.name == "id" && (!dataDocument.contains(field.name) || dataDocument.get(field.name) == null)) {
                field.isAccessible = true
                field.set(obj, dataDocument.id)
                return@forEach
            }
            if (!dataDocument.contains(field.name)) return@forEach

            field.isAccessible = true

            field.set(obj, dataDocument.get(field.name))
        }
        return obj
    }

    fun <T> deserializeList(dataCollection: QuerySnapshot, clazz: Class<T>): List<T> {
        val list = dataCollection
            .map { deserialize(it, clazz) }
            .toMutableList()
        try {
            val orderField = clazz.getDeclaredField("orderNumber")
            orderField.isAccessible = true
            list.sortBy { orderField.get(it)?.toString() ?: "0" }
        } catch (e: NoSuchFieldException) {
        }

        return list
    }
}