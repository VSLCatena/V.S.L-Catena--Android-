package nl.vslcatena.vslcatena.lists.normal

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class ListFragmentRecyclerViewAdapter<T, VH: RecyclerView.ViewHolder>(
        private var mItems: List<T>,
        private val mListener: ListFragment.OnListItemClickedListener<T>?,
        private val mViewId: Int)
    : RecyclerView.Adapter<VH>() {

    //The click listener in which the ListFragments listener and a adapter level listener get called.
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            @Suppress("UNCHECKED_CAST")
            val item = v.tag as T
            onItemClicked(v, item)
            mListener?.onListItemClicked(item)
        }
    }

    abstract fun createViewHolder(view: View): VH


    //Gives the option to add click listener functionality at Adapter Level.
    open fun onItemClicked(view: View, item: T){}

    open fun onBindViewHolder(holder: VH, item: T, position: Int){}


    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return createViewHolder(LayoutInflater.from(parent.context).inflate(mViewId, parent, false))
    }

    final override fun getItemCount(): Int {
        return mItems.size
    }


    fun setItems(items: List<T>){
        mItems = items
        notifyDataSetChanged()
    }


    /**
     * Binds the listener to the ViewHolder and sets the tag of the itemView to the item.
     * Extra functionality can be added in subclasses when the onBindViewHolder(holder: VH, item: T, position: Int) is overridden.
     */
    final override fun onBindViewHolder(holder: VH, position: Int) {
        val item = mItems[position]
        with(holder.itemView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
        onBindViewHolder(holder, item, position)
    }

}
