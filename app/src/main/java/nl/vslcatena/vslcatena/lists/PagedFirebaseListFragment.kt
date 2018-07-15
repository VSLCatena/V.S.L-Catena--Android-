package nl.vslcatena.vslcatena.lists

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.firebase.BaseModel
import nl.vslcatena.vslcatena.firebase.FirebasePagingAdapter

abstract class PagedFirebaseListFragment<T:BaseModel, VH: RecyclerView.ViewHolder>(private val clazz: Class<T>) : Fragment(), OnListItemClickedListener<T> {

    //The click listener in which the ListFragments listener and a adapter level listener get called.
    private val mOnClickListener: View.OnClickListener
    init {
        mOnClickListener = View.OnClickListener { v ->
            @Suppress("UNCHECKED_CAST")
            val item = v.tag as T
            onListItemClicked(item)
        }
    }

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt("columnCount")
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // We create a new FirebasePagingAdapter object which will do all the loading of objects for us
        object: FirebasePagingAdapter<T, VH>(context!!, columnCount){
            override fun getView()=  itemView
            override fun createViewHolder(view: View): VH = this@PagedFirebaseListFragment.createViewHolder(view)

            //Sets the item as the Tag when binding, so that it can be used to save lookups
            override fun onBindViewHolder(holder: VH, position: Int) {
                val item = getItem(position)!!
                with(holder.itemView) {
                    tag = item
                    setOnClickListener(mOnClickListener)
                }
                this@PagedFirebaseListFragment.onBindViewHolder(holder, position, item)
            }

        }.bindTo(this, list, clazz)
        // As last, we give the activity/fragment to bind it to, the recyclerview, and the class to load.
    }
    abstract val itemView: Int
    abstract fun createViewHolder(view: View): VH
    abstract fun onBindViewHolder(holder: VH, position: Int, item: T)
}