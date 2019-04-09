package nl.vslcatena.vslcatena.modules.promo


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.post_header.*
import kotlinx.android.synthetic.main.promo_item.*
import nl.vslcatena.vslcatena.BaseFragment
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.models.Role
import nl.vslcatena.vslcatena.models.viewmodels.UserPool
import nl.vslcatena.vslcatena.util.componentholders.PostHeaderViewHolder
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.extensions.observeOnce
import nl.vslcatena.vslcatena.util.extensions.setImageFromFirebaseStorage
import nl.vslcatena.vslcatena.util.login.AuthenticationLevel
import nl.vslcatena.vslcatena.util.login.UserProvider

/**
 * Fragment for showing a single promo item.
 */
@AuthenticationLevel(Role.USER)
class PromoItemFragment : BaseFragment() {

    private lateinit var userPool: UserPool
    private lateinit var promoItem: LiveData<PromoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPool = ViewModelProviders.of(this).get(UserPool::class.java)
        promoItem = DataCreator.getSingleReference(
            PromoItem::class.java,
            Identifier(PromoItemFragmentArgs.fromBundle(arguments).itemId)
        )
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promo_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewHolder = PostHeaderViewHolder<PromoItem>(userPool, view)
        // Hook the promo user to the user observer
        promoItem.observe(this, Observer {
            title.text = it.title
            content.text = it.content
            it.imageRef?.let { it1 -> promoImage.setImageFromFirebaseStorage(it1) }
            viewHolder.bind(it)
        })

        promoItem.observeOnce(this, Observer { item ->
            userPool.getUser(item.user).observeOnce(this, Observer {
                postHeaderUserName.text = it.name
                postHeaderUserImage.setImageFromFirebaseStorage(it.getThumbnailRef())
            })
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_icon_menu, menu)
        inflater.inflate(R.menu.delete_icon_menu, menu)

        UserProvider.currentUser.observe(this, Observer {
            (it?.hasClearance(Role.ADMIN) == true).let { hasClearance ->
                menu?.findItem(R.id.edit)?.isVisible = hasClearance
                menu?.findItem(R.id.delete)?.isVisible = hasClearance
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.edit -> {
                findNavController().navigate(
                    PromoItemFragmentDirections
                        .actionPromoItemFragmentToPromoEditItemFragment()
                        .setEditId(promoItem.value?.id?.value)
                )
            }
            R.id.delete -> {
                AlertDialog.Builder(context!!)
                    .setTitle(R.string.promo_item_delete_confirmation)
                    .setMessage(
                        getString(
                            R.string.promo_item_delete_confirmation_content,
                            promoItem.value?.title ?: "???"
                        )
                    )
                    .setPositiveButton(R.string.general_yes) { _, _ -> delete() }
                    .setNegativeButton(R.string.general_no) { _, _ -> }
                    .show()

            }
            else -> return false
        }
        return true
    }

    private fun delete() {
        promoItem.value?.let { item ->
            item.delete()
                .addOnCompleteListener {
                    Toast.makeText(
                        context,
                        R.string.promo_item_delete_successful,
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack()
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG)
                        .show()
                }

        }
    }
}


