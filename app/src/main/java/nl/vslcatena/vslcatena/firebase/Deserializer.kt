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
                field.set(obj, deserializeObject(it, field))
            }
        }

        return obj
    }

    fun <T> deserializeObject(dataSnapshot: DataSnapshot, field: Field): T {
        return when {
            List::class.java.isAssignableFrom(field.type) -> {
                deserializeList(dataSnapshot, field.getAnnotation(ListType::class.java)?.value?.java ?: Any::class.java) as T
            }
            Map::class.java.isAssignableFrom(field.type) -> {
                deserializeMap(dataSnapshot.child(field.name), field.getAnnotation(ListType::class.java)?.value?.java ?: Any::class.java) as T
            }
            BaseModel::class.java.isAssignableFrom(field.type) -> {
                deserialize(dataSnapshot.child(field.name), field.type) as T
            }
            else -> dataSnapshot.getValue(field.type) as T
        }
    }

    fun <T> deserializeList(dataSnapshot: DataSnapshot, clazz: Class<T>): List<T> {
        return dataSnapshot.children.map { deserialize(it, clazz) }.toList()
    }

    fun <T> deserializeMap(dataSnapshot: DataSnapshot, clazz: Class<T>): Map<String, T> {
        return dataSnapshot.children.map { it.key to deserialize(it, clazz) }.toMap()
    }
}