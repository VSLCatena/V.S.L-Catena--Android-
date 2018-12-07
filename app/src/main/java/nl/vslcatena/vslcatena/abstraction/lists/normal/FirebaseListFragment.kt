package nl.vslcatena.vslcatena.abstraction.lists.normal

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.lifecycle.Observer
import nl.vslcatena.vslcatena.abstraction.firebase.LiveViewModel
import nl.vslcatena.vslcatena.util.extensions.applyArguments
import nl.vslcatena.vslcatena.util.extensions.observeOnce

/**
 * Abstract child of the ListFragment. Gets the items from firebase. Has option to use single observation.
 */
abstract class FirebaseListFragment<T, VH: RecyclerView.ViewHolder>(private val clazz: Class<T>): ListFragment<T, VH>() {
    final override var mItems: List<T> = ArrayList()

    private var singleObservation: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            singleObservation = it.getBoolean(ARG_SINGLE_OBSERVATION)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val liveData = LiveViewModel.of(this)
            .getReference(LiveViewModel.getCollectionReference(clazz)!!)
            .toTypedList(clazz)

        if(singleObservation) {
            liveData.observeOnce(this, Observer { setItems(it) })
        } else {
            liveData.observe(this, Observer { setItems(it) })
        }
    }
    companion object {
        protected const val ARG_SINGLE_OBSERVATION = "singleObservation"

        fun applyArguments(instance: ListFragment<*, *>,
                           columnCount: Int = 1,
                           singleObservation: Boolean = true){
            ListFragment.applyArguments(instance, columnCount)
            instance.applyArguments {
                it.putBoolean(ARG_SINGLE_OBSERVATION, singleObservation)
            }
        }
    }

}