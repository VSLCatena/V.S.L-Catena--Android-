package nl.vslcatena.vslcatena.lists.paged

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import nl.vslcatena.vslcatena.firebase.FirebaseModel
import nl.vslcatena.vslcatena.firebase.LiveViewModel
import nl.vslcatena.vslcatena.lists.normal.ListFragment
import nl.vslcatena.vslcatena.util.extensions.applyArguments

/**
 * Abstract child of the ListFragment. Gets the items from firebase. Has option to use single observation.
 */
abstract class FirebasePagedListFragment<T: FirebaseModel, VH: RecyclerView.ViewHolder>(val clazz: Class<T>): PagedListFragment<T, VH>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun createLiveDataPagedList(): LiveData<PagedList<T>> {
        val dataSourceFactory = FirebaseDataSourceFactory(clazz, LiveViewModel.of(this))
        val config = PagedList.Config.Builder().apply {
            setPageSize(3)
            setInitialLoadSizeHint(6)

        }.build()
        return LivePagedListBuilder(dataSourceFactory, config).build()
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