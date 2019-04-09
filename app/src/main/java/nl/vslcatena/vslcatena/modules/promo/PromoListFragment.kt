package nl.vslcatena.vslcatena.modules.promo

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Role
import nl.vslcatena.vslcatena.util.abstractions.FirestorePagingFragment
import nl.vslcatena.vslcatena.util.componentholders.PostHeaderViewHolder
import nl.vslcatena.vslcatena.util.extensions.setImageFromFirebaseStorage
import nl.vslcatena.vslcatena.util.login.AuthenticationLevel

@AuthenticationLevel(Role.USER)
class PromoListFragment : FirestorePagingFragment<PromoItem, PromoListFragment.PromoViewHolder>() {

    override fun addNavigationId() = R.id.promoEditItemFragment
    override fun requiredAddRole() = Role.USER

    override fun getItemClass() = PromoItem::class.java
    override fun getItemLayout() = R.layout.promo_item
    override fun createItemViewHolder(view: View) = PromoViewHolder(view)

    inner class PromoViewHolder(val view: View) : PostHeaderViewHolder<PromoItem>(userPool, view) {

        val titleView: TextView = itemView.findViewById(R.id.title)
        val contentView: TextView = itemView.findViewById(R.id.content)
        val imageView: ImageView = itemView.findViewById(R.id.promoImage)

        override fun bind(item: PromoItem) {
            super.bind(item)

            titleView.text = item.title
            contentView.text = item.content
            item.imageRef?.let { imageView.setImageFromFirebaseStorage(it)}

            view.setOnClickListener {
                item.id?.let { id ->
                    findNavController().navigate(
                        PromoListFragmentDirections.actionPromoFragmentToPromoItemFragment(id.value)
                    )
                }
            }
        }
    }

}


