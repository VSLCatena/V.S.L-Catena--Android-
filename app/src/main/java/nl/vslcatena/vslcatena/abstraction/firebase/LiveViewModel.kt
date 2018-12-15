package nl.vslcatena.vslcatena.abstraction.firebase

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import java.lang.ref.WeakReference

/**
 * Created by Thomas van den Bulk on 30-4-2018.
 */
class LiveViewModel: ViewModel() {
    private val references = HashMap<String, WeakReference<RawReference>>()

    fun get(query: Query): RawReference {
        val path = query.path.wireFormat()

        var reference = references[path]?.get()
        if (reference == null) {
            reference = RawReference(query)
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


        fun createQuery(reference: String, filter: (DatabaseReference) -> Query = { it }): Query {
            val fbReference = FirebaseDatabase.getInstance().getReference(reference)
            return filter.invoke(fbReference)
        }


        class Provider(_viewModel: LiveViewModel) {
            private val viewModel = WeakReference(_viewModel)
            private var filter: (DatabaseReference) -> Query = { it }

            fun filter(filter: (DatabaseReference) -> Query): Provider {
                this.filter = filter
                return this
            }

            fun enabledOnly(): Provider {
                filter = { it.child("enabled").equalTo(true) }
                return this
            }

            fun getReference(
                reference: String
            ): RawReference {
                return viewModel.get()!!.get(createQuery(reference, filter))
            }
        }
    }
}