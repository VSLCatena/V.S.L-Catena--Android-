package nl.vslcatena.vslcatena.modules.promo

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.Query
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.viewmodels.UserPool
import nl.vslcatena.vslcatena.util.abstractions.FirestorePagingFragment
import nl.vslcatena.vslcatena.util.componentholders.PostHeaderViewHolder
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.login.NeedsAuthentication

@NeedsAuthentication
class PromoListFragment : FirestorePagingFragment<PromoItem, PromoListFragment.PromoViewHolder>() {
    private lateinit var userPool: UserPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPool = ViewModelProviders.of(this).get(UserPool::class.java)
    }

    override fun getItemClass() = PromoItem::class.java
    override fun getItemLayout() = R.layout.promo_item
    override fun createItemViewHolder(view: View) = PromoViewHolder(view)
    override fun createQuery() = DataCreator
        .createQuery(PromoItem::class.java)
        .orderBy("date", Query.Direction.DESCENDING)

    inner class PromoViewHolder(view: View) : PostHeaderViewHolder<PromoItem>(userPool, view) {

        val titleView: TextView = itemView.findViewById(R.id.title)
        val contentView: TextView = itemView.findViewById(R.id.content)

        override fun bind(item: PromoItem) {
            super.bind(item)
            titleView.text = item.title
            contentView.text = item.content


            view?.setOnClickListener {
                findNavController().navigate(
                    PromoListFragmentDirections.actionPromoFragmentToPromoItemFragment(item.id.value)
                )
            }
        }
    }

}


