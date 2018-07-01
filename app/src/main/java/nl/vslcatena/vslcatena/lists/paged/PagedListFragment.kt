package nl.vslcatena.vslcatena.lists.paged

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.util.extensions.applyArguments

/**
 * Abstract implementation for a fragment containing a list of model objects. Manages the RecyclerViewAdapter
 */
abstract class PagedListFragment<T, VH: RecyclerView.ViewHolder> : Fragment() {

    //The amount of columns that should be shown.
    private var columnCount = 1
    private var mListener: OnListItemClickedListener<T>? = null
    protected lateinit var mItems: LiveData<PagedList<T>>

    private lateinit var mAdapter: PagedListFragmentRecyclerViewAdapter<T, VH>

    abstract fun createAdapter(): PagedListFragmentRecyclerViewAdapter<T, VH>
    abstract fun createLiveDataPagedList() : LiveData<PagedList<T>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        mAdapter = createAdapter()
        mItems = createLiveDataPagedList()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = mAdapter
                mItems.observe(this@PagedListFragment, Observer { mAdapter.submitList(it) })

            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            @Suppress("UNCHECKED_CAST")
            mListener = context  as OnListItemClickedListener<T>
        }catch (e: ClassCastException){
            //todo check if we want this
//            throw RuntimeException(context.toString() + " must implement OnListItemClickedListener with the correct type")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnListItemClickedListener<T> {
        fun onListItemClicked(item: T)
    }

    companion object {
        protected const val ARG_COLUMN_COUNT = "column-count"


        fun applyArguments(instance: PagedListFragment<*, *>, columnCount: Int = 1) =
            instance.applyArguments {
                it.putInt(ARG_COLUMN_COUNT, columnCount)
            }
    }
}