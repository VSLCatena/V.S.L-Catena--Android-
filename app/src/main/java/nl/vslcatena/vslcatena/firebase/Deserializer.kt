package nl.vslcatena.vslcatena.firebase

import com.google.firebase.database.DataSnapshot
import java.lang.reflect.Field

/**
 * Created by Thomas van den Bulk on 30-4-2018.
 */
object Deserializer {
    fun <T> deserialize(dataSnapshot: DataSnapshot, clazz: Class<T>): T {
        val obj: T = clazz.newInstance()
        if(dataSnapshot.hasChildren()) {
            dataSnapshot.children.forEach {
                val field = try {
                    clazz.getDeclaredField(it.key)
                } catch(e: NoSuchFieldException) {
                    return@forEach
                }

                field.isAccessible = true
                field.set(obj, deserializeObject(it, field.type, field))
            }
        }

        return obj
    }

    fun <T> deserializeObject(dataSnapshot: DataSnapshot, clazz: Class<T>, field: Field? = null): T {
        return when {
            List::class.java.isAssignableFrom(clazz) -> {
                deserializeList(dataSnapshot, field?.getAnnotation(ListType::class.java)?.value?.java ?: Any::class.java) as T
            }
            Map::class.java.isAssignableFrom(clazz) -> {
                deserializeMap(dataSnapshot, field?.getAnnotation(ListType::class.java)?.value?.java ?: Any::class.java) as T
            }
            BaseModel::class.java.isAssignableFrom(clazz) -> {
                deserialize(dataSnapshot, clazz)
            }
            else -> dataSnapshot.getValue(clazz) as T
        }
    }

    fun <T> deserializeList(dataSnapshot: DataSnapshot, clazz: Class<T>): List<T> {
        val list = dataSnapshot.children.map { deserializeObject(it, clazz) }.toMutableList()
        try {
            val orderField = clazz.getDeclaredField("orderNumber")
            orderField.isAccessible = true
            list.sortBy { orderField.get(it)?.toString() ?: "0" }
        } catch(e: NoSuchFieldException){}

        return list
    }

    fun <T> deserializeMap(dataSnapshot: DataSnapshot, clazz: Class<T>): Map<String, T> {
        val list = dataSnapshot.children.map { it.key!! to deserializeObject(it, clazz) }.toMutableList()

        try {
            val orderField = clazz.getDeclaredField("orderNumber")
            orderField.isAccessible = true
            list.sortBy { orderField.get(it.second)?.toString() ?: "0" }
        } catch(e: NoSuchFieldException){}

        return list.toMap().toMutableMap()
    }
}