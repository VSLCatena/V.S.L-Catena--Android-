package nl.vslcatena.vslcatena.controllers

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.home.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.firebase.LiveViewModel
import nl.vslcatena.vslcatena.models.News

class HomeFragment : Fragment() {

    var newsList: List<News> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        news_items.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = NewsAdapter()
        }

        LiveViewModel.of(this).observe(News::class.java){
            newsList = it
            news_items.adapter.notifyDataSetChanged()
        }
    }

    inner class NewsAdapter :  RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)

        override fun getItemCount() = newsList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                ViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            newsList[position].let { news ->
                holder.itemView?.apply {
                    findViewById<TextView>(R.id.item_title).text = news.title
                    findViewById<TextView>(R.id.item_subtitle).apply {
                        visibility = View.VISIBLE
                        text = news.content
                    }
                }
            }
        }
    }

}
