package nl.vslcatena.vslcatena.modules.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.recyclerview.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.viewmodels.UserPool
import nl.vslcatena.vslcatena.util.abstractions.FirestorePagingFragment
import nl.vslcatena.vslcatena.util.componentholders.PostHeaderViewHolder
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.login.NeedsAuthentication

@NeedsAuthentication
class NewsListFragment : FirestorePagingFragment<News, NewsListFragment.NewsViewHolder>() {

    lateinit var userPool: UserPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPool = ViewModelProviders.of(this).get(UserPool::class.java)
    }

    override fun createItemViewHolder(view: View) = NewsViewHolder(view)
    override fun getItemClass() = News::class.java
    override fun getItemLayout() = R.layout.news_item
    override fun createQuery() = DataCreator
        .createQuery(News::class.java)
        .orderBy("date", Query.Direction.DESCENDING)


    inner class NewsViewHolder(val view: View) : PostHeaderViewHolder<News>(userPool, view) {
        val mTitleView: TextView = view.findViewById(R.id.item_title)
        val mSubTitleView: TextView = view.findViewById(R.id.item_content)

        override fun bind(item: News) {
            super.bind(item)

            mTitleView.text = item.title
            mSubTitleView.apply {
                visibility = View.VISIBLE
                text = item.content
            }

            view.setOnClickListener {
                findNavController().navigate(
                    NewsListFragmentDirections.actionNewsFragmentToNewsItemFragment(item.id.value)
                )
            }
        }
    }
}
