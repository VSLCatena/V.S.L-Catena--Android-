package nl.vslcatena.vslcatena.abstraction.firebase

import androidx.lifecycle.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import android.util.Log
import com.google.firebase.database.*
import java.lang.ref.WeakReference

/**
 * Created by Thomas van den Bulk on 30-4-2018.
 */
class LiveViewModel: ViewModel() {
    private val references = HashMap<String, WeakReference<Reference.RawReference>>()

    fun get(query: Query): Reference.RawReference {
        val path = query.path.wireFormat()

        var reference = references[path]?.get()
        if (reference == null) {
            reference = Reference.RawReference(query)
            references[path] = WeakReference(reference)
        }

        return reference
    }

    companion object {
        const val LOG_TAG = "LiveViewModel"

        fun of(activity: FragmentActivity) = Provider(ViewModelProviders.of(activity).get(LiveViewModel::class.java))
        fun of(fragment: Fragment) = Provider(ViewModelProviders.of(fragment).get(LiveViewModel::class.java))

        fun getSingleReference(
            klass: Class<out Any?>,
            objectId: String? = null,
            reference: String? = null
        ): String? {
            return reference
                ?: klass.getAnnotation(DataReference::class.java)
                    ?.singleReference?.format(objectId)
        }

        fun getCollectionReference(
            klass: Class<out Any?>,
            reference: String? = null
        ): String? {
            return reference
                ?: klass.getAnnotation(DataReference::class.java)
                    ?.listReference
        }

        class Provider(_viewModel: LiveViewModel) {
            private val viewModel = WeakReference(_viewModel)
            private var enabledOnly = false
            private var filter: (DatabaseReference) -> Query = { it }

            private fun createQuery(reference: String): Query {
                val fbReference = FirebaseDatabase.getInstance().getReference(reference)
                return if (enabledOnly) {
                    fbReference.child("enabled").equalTo(true)
                } else filter.invoke(fbReference)
            }

            fun filter(filter: (DatabaseReference) -> Query): Provider {
                this.filter = filter
                return this
            }

            fun getReference(
                reference: String
            ): Reference.RawReference {
                return viewModel.get()!!.get(createQuery(reference))
            }
        }
    }
}