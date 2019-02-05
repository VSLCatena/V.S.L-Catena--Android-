package nl.vslcatena.vslcatena.util.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import nl.vslcatena.vslcatena.models.Identifier
import java.lang.IllegalStateException

object DataCreator {

    fun createQuery(klass: Class<out BaseModel>, filter: (CollectionReference) -> Query = { it }): Query {
        return filter.invoke(
            FirebaseFirestore.getInstance().collection(getListReferenceString(klass))
        )
    }

    fun <T : BaseModel> getSingleReference(
        klass: Class<T>,
        objectId: Identifier? = null
    ): Reference.Single<T> {
        val reference = FirebaseFirestore.getInstance().document(
            DataCreator.getSingleReferenceString(klass, objectId!!)
        )

        return Reference.Single(reference, klass)
    }

    fun <T : BaseModel> getListReference(
        klass: Class<T>,
        filter: (CollectionReference) -> Query = { it }
    ): Reference.List<T> {
        val reference = createQuery(klass, filter)

        return Reference.List(reference, klass)
    }

    private fun getSingleReferenceString(klass: Class<out BaseModel>, id: Identifier): String {
        return klass.getAnnotation(DataReference::class.java)!!
            .singleReference
            .format(id.value)
    }

    private fun getSingleReferenceString(item: BaseModel): String {
        return getSingleReferenceString(item.javaClass, item.id!!)
    }

    private fun getListReferenceString(klass: Class<out BaseModel>): String {
        return klass.getAnnotation(DataReference::class.java)!!.listReference
    }

    fun set(item: BaseModel): Task<out Any> {
        return if (item.id == null) add(item)
        else edit(item)
    }

    fun add(item: BaseModel): Task<DocumentReference> {
        return FirebaseFirestore.getInstance().collection(
            getListReferenceString(item.javaClass)
        ).add(item)
    }

    fun edit(item: BaseModel): Task<out Any> {
        return FirebaseFirestore.getInstance().document(
            getSingleReferenceString(item)
        ).set(item)
    }

    fun delete(item: BaseModel): Task<out Any> {
        if (item.id == null) throw IllegalStateException("Id is null")

        return FirebaseFirestore.getInstance().document(
            getSingleReferenceString(item)
        ).delete()
    }
}