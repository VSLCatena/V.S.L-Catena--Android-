package nl.vslcatena.vslcatena.controllers

import android.view.View
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.News
import nl.vslcatena.vslcatena.recyclerview.ListFragmentRecyclerViewAdapter
import nl.vslcatena.vslcatena.recyclerview.NewsViewHolder

class NewsFragment : FirebaseListFragment<News, NewsViewHolder>(News::class.java) {
    override fun createAdapter(): ListFragmentRecyclerViewAdapter<News, NewsViewHolder> {
        return object : ListFragmentRecyclerViewAdapter<News, NewsViewHolder>(mItems, null, R.layout.promo_item){

            override fun createViewHolder(view: View): NewsViewHolder {
                return NewsViewHolder(view)
            }

            override fun onBindViewHolder(holder: NewsViewHolder, item: News, position: Int) {
                super.onBindViewHolder(holder, item, position)
                holder.mContentView.text = item.content
            }

        }
    }
}