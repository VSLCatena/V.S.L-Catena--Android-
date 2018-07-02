package nl.vslcatena.vslcatena.controllers

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.home.*
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
        object: FirebasePagingAdapter<News>(context!!){
            // We give it the view we want to load the ViewHolder with
            override fun getView() = R.layout.list_item

            // Then we bind the item to the viewholder
            override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
                getItem(position)?.let { news ->
                    holder.itemView?.apply {
                        findViewById<TextView>(R.id.item_title).text = news.title
                        findViewById<TextView>(R.id.item_subtitle).apply {
                            visibility = View.VISIBLE
                            text = news.content
                        }
                    }
                } ?: holder.itemView?.apply {
                    // If a view is getting loaded it returns null, so we just set everything to empty
                    // if that happens
                    findViewById<TextView>(R.id.item_title).text = ""
                    findViewById<TextView>(R.id.item_subtitle).text = ""
                }
            }
        }.bindTo(this, news_items, News::class.java)
        // As last, we give the activity/fragment to bind it to, the recyclerview, and the class to load.
    }
}
