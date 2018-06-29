package nl.vslcatena.vslcatena.controllers

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import nl.vslcatena.vslcatena.firebase.LiveViewModel
import nl.vslcatena.vslcatena.util.extensions.applyArguments

/**
 * Abstract child of the ListFragment. Gets the items from firebase. Has option to use single observation.
 */
abstract class FirebaseListFragment<T, VH: RecyclerView.ViewHolder>(val clazz: Class<T>): ListFragment<T, VH>() {
    final override var mItems: List<T> = ArrayList()

    private var singleObservation: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            singleObservation = it.getBoolean(ARG_SINGLE_OBSERVATION)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        LiveViewModel.of(this).observeList(clazz, singleObservation) {
            setItems(it)
        }
    }
    companion object {
        protected const val ARG_SINGLE_OBSERVATION = "singleObservation"

        fun applyArguments(instance: ListFragment<*,*>,
                           columnCount: Int = 1,
                           singleObservation: Boolean = true){
            ListFragment.applyArguments(instance, columnCount)
            instance.applyArguments {
                it.putBoolean(ARG_SINGLE_OBSERVATION, singleObservation)
            }
        }
    }

}