package nl.vslcatena.vslcatena.modules.news


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.news_item.*
import kotlinx.android.synthetic.main.post_header.*
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
 * Fragment for showing a single newsItem.
 *
 */
@AuthenticationLevel(Role.USER)
class NewsItemFragment : BaseFragment() {

    private lateinit var userPool: UserPool
    private lateinit var news: LiveData<News>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments ?: return // TODO
        userPool = ViewModelProviders.of(this).get(UserPool::class.java)
        news = DataCreator.getSingleReference(
            News::class.java,
            Identifier(NewsItemFragmentArgs.fromBundle(arguments).itemId)
        )
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.news_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewHolder = PostHeaderViewHolder<News>(userPool, view)
        // Hook the news user to the user observer
        news.observe(this, Observer {
            title.text = it.title
            content.text = it.content

            viewHolder.bind(it)
        })

        news.observeOnce(this, Observer { news ->
            userPool.getUser(news.user).observeOnce(this, Observer {
                postHeaderUserName.text = it.name
                postHeaderUserImage.setImageFromFirebaseStorage(it.getProfileImage())
            })
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.edit -> {
                findNavController().navigate(
                    NewsItemFragmentDirections
                        .actionNewsItemFragmentToNewsEditItemFragment()
                        .setEditId(news.value?.id?.value)
                )
            }
            R.id.delete -> {
                AlertDialog.Builder(context!!)
                    .setTitle(R.string.news_item_delete_confirmation)
                    .setMessage(
                        getString(
                            R.string.news_item_delete_confirmation_content,
                            news.value?.title ?: "???"
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
        news.value?.let { news ->
            news.delete()
                .addOnCompleteListener {
                    Toast.makeText(
                        context,
                        R.string.news_item_delete_successful,
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
