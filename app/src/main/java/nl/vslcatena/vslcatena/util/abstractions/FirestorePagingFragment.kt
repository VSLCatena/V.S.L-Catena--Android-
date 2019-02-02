package nl.vslcatena.vslcatena.util.abstractions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.recyclerview.*
import nl.vslcatena.vslcatena.BaseFragment
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.login.AuthenticationLevel


abstract class FirestorePagingFragment<T : BaseModel, B> : BaseFragment()
        where B : RecyclerView.ViewHolder, B : FirestorePagingFragment.Binder<T> {

    abstract fun createQuery(): Query

    open fun getRecyclerView(): RecyclerView = recyclerview

    abstract fun getItemClass(): Class<T>

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // The "add_icon_menu query" is a query with no startAt/endAt/limit clauses that the adapter can use
        // to form smaller queries for each page.  It should only include where() and orderBy() clauses
        val baseQuery = createQuery()

        // This configuration comes from the Paging Support Library
        // https://developer.android.com/reference/android/arch/paging/PagedList.Config.html
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(10)
            .setPageSize(20)
            .build()

        // The options for the adapter combine the paging configuration with query information
        // and application-specific options for lifecycle, etc.
        val options = FirestorePagingOptions.Builder<T>()
            .setLifecycleOwner(this)
            .setQuery(baseQuery, config, getItemClass())
            .build()


        getRecyclerView().apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PagingAdapter(options)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recyclerview, container, false)
    }

    /**
     * Used to inflate the item layout
     *
     * @return  The layout of the item
     */
    @LayoutRes
    abstract fun getItemLayout(): Int

    /**
     * Inflates the view using the layout returned from #getItemLayout
     *
     * @param parent    The parent viewGroup the layout is inflated in
     * @return          The newly inflated view
     */
    open fun getItemLayoutView(parent: ViewGroup) =
        LayoutInflater.from(context).inflate(getItemLayout(), parent, false)

    /**
     * Creates the viewHolder and will be used internally
     */
    abstract fun createItemViewHolder(view: View): B


    open inner class PagingAdapter(
        options: FirestorePagingOptions<T>
    ) : FirestorePagingAdapter<T, B>(options) {

        override fun onViewAttachedToWindow(holder: B) {
            if (holder is LifecycleAwareViewHolder)
                holder.markAttach()
        }

        override fun onViewDetachedFromWindow(holder: B) {
            if (holder is LifecycleAwareViewHolder)
                holder.markDetach()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): B {
            return createItemViewHolder(getItemLayoutView(parent))
        }

        override fun onBindViewHolder(
            viewHolder: B,
            position: Int,
            item: T
        ) {
            if (item.id == null || item.id == Identifier("")) {
                item::class.java.declaredFields
                    .find { it.name == "id" }
                    ?.apply {
                        isAccessible = true
                    }?.set(item, currentList?.get(position)?.id)
            }
            viewHolder.bind(item)
        }
    }

    interface Binder<T> {
        fun bind(item: T)
    }

}