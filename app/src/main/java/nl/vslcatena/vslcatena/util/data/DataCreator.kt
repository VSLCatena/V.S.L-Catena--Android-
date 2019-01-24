package nl.vslcatena.vslcatena.util.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import nl.vslcatena.vslcatena.models.Identifier

object DataCreator {

    fun createQuery(klass: Class<*>, filter: (CollectionReference) -> Query = { it }): Query {
        return filter.invoke(
            FirebaseFirestore.getInstance().collection(
                klass.getAnnotation(DataReference::class.java)!!.listReference
            )
        )
    }

    fun <T> getSingleReference(
        klass: Class<T>,
        objectId: Identifier? = null
    ): Reference.Single<T> {
        val reference = FirebaseFirestore.getInstance().document(
            klass.getAnnotation(DataReference::class.java)!!
                .singleReference
                .format(objectId?.value)
        )

        return Reference.Single(reference, klass)
    }

    fun <T> getListReference(
        klass: Class<T>,
        filter: (CollectionReference) -> Query = { it }
    ): Reference.List<T> {
        val reference = createQuery(klass, filter)

        return Reference.List(reference, klass)
    }
}