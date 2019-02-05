package nl.vslcatena.vslcatena.modules.news

import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Role
import nl.vslcatena.vslcatena.util.abstractions.FirestorePagingFragment
import nl.vslcatena.vslcatena.util.componentholders.PostHeaderViewHolder
import nl.vslcatena.vslcatena.util.login.AuthenticationLevel

@AuthenticationLevel(Role.USER)
class NewsListFragment : FirestorePagingFragment<News, NewsListFragment.NewsViewHolder>() {

    override fun requiredAddRole() = Role.ADMIN
    override fun addNavigationId() = R.id.newsEditItemFragment

    override fun createItemViewHolder(view: View) = NewsViewHolder(view)
    override fun getItemClass() = News::class.java
    override fun getItemLayout() = R.layout.news_item


    inner class NewsViewHolder(val view: View) : PostHeaderViewHolder<News>(userPool, view) {
        private val titleView: TextView = view.findViewById(R.id.title)
        private val subTitleView: TextView = view.findViewById(R.id.content)

        override fun bind(item: News) {
            super.bind(item)

            titleView.text = item.title
            subTitleView.apply {
                visibility = View.VISIBLE
                text = item.content
            }

            view.setOnClickListener {
                item.id?.let { id ->
                    findNavController().navigate(
                        NewsListFragmentDirections.actionNewsFragmentToNewsItemFragment(id.value)
                    )
                }
            }
        }
    }
}
