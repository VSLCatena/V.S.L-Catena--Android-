package nl.vslcatena.vslcatena.controllers

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.promo_item.view.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.firebase.FirebasePagingAdapter
import nl.vslcatena.vslcatena.models.News


class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // We create a new FirebasePagingAdapter object which will do all the loading of objects for us
        object: FirebasePagingAdapter<News, NewsViewHolder>(context!!){

            // The View the ViewHolder should display
            override fun getView() = R.layout.list_item
            // How the ViewHolder is created
            override fun createViewHolder(view: View): NewsViewHolder = NewsViewHolder(view)

            // Then we bind the item to the viewholder
            override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
                getItem(position)?.let { news ->
                    holder.apply {
                        mTitleView.text = news.title
                        mSubTitleView.apply {
                            visibility = View.VISIBLE
                            text = news.content
                        }
                    }
                } ?: holder.apply {
                    // If a view is getting loaded it returns null, so we just set everything to empty
                    // if that happens
                    mTitleView.text = ""
                    mSubTitleView.text = ""
                }
            }
        }.bindTo(this, news_items, News::class.java)
        // As last, we give the activity/fragment to bind it to, the recyclerview, and the class to load.
    }

    class NewsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val mTitleView: TextView = view.findViewById(R.id.item_title)
        val mSubTitleView: TextView = view.findViewById(R.id.item_subtitle)
    }
}
