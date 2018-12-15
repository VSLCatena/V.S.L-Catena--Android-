package nl.vslcatena.vslcatena.modules.promo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_promo_item.*
import kotlinx.android.synthetic.main.post_header.*
import kotlinx.android.synthetic.main.promo_item.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.abstraction.firebase.ItemLoader
import nl.vslcatena.vslcatena.abstraction.fragment.NeedsAuthentication
import nl.vslcatena.vslcatena.abstraction.fragment.SingleItemFragment
import nl.vslcatena.vslcatena.modules.login.User
import nl.vslcatena.vslcatena.util.extensions.observeOnce
import nl.vslcatena.vslcatena.util.extensions.setImageFromFirebaseStorage
import java.text.DateFormat
import java.util.*

/**
 * Fragment for showing a single promo item.
 */
@NeedsAuthentication
class PromoItemFragment : SingleItemFragment<PromoItem>(PromoItem::class.java) {
    private var userLoader: ItemLoader<User> = ItemLoader(User::class.java)

    override fun onItemRetrieved(item: PromoItem) {
        userLoader.getItem(item.metaData.postedBy)?.let {
            it.observeOnce(this, Observer {
                it?.let {
                    postHeaderUserName.text = it.name
                    postHeaderPostDate.text = DateFormat.getDateInstance().format(Date(item.metaData.postedTime))
                    postHeaderUserImage.setImageFromFirebaseStorage(context!!, it.getThumbnailRef())
                }
            })
        }

        content.text = item.metaData.content
        title.text = item.metaData.title

        if(item.adventureId != -1){
            goToAdventureButton.apply {
                visibility = View.VISIBLE
                setOnClickListener { findNavController().navigate(
                    PromoItemFragmentDirections.actionPromoItemFragmentToAdventureItemFragment(
                        item.id
                    )
                ) }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promo_item, container, false)
    }
}


