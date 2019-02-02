package nl.vslcatena.vslcatena.modules.news

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.Query
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Role
import nl.vslcatena.vslcatena.models.viewmodels.UserPool
import nl.vslcatena.vslcatena.util.abstractions.FirestorePagingFragment
import nl.vslcatena.vslcatena.util.componentholders.PostHeaderViewHolder
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.login.LoginProvider
import nl.vslcatena.vslcatena.util.login.AuthenticationLevel

@AuthenticationLevel(Role.USER)
class NewsListFragment : FirestorePagingFragment<News, NewsListFragment.NewsViewHolder>() {

    lateinit var userPool: UserPool

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        userPool = ViewModelProviders.of(this).get(UserPool::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_icon_menu, menu)

        LoginProvider.currentUser.observe(this, Observer { user ->
            menu?.findItem(R.id.add)?.isVisible = user.hasClearance(Role.ADMIN)
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.add) {
            if (LoginProvider.getUser()?.hasClearance(Role.ADMIN) == true) {
                findNavController().navigate(R.id.newsEditItemFragment)
            }
            return true
        }
        return false
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
                item.id?.let { id ->
                    findNavController().navigate(
                        NewsListFragmentDirections.actionNewsFragmentToNewsItemFragment(id.value)
                    )
                }
            }
        }
    }
}
