package nl.vslcatena.vslcatena.modules.news


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.news_item.*
import kotlinx.android.synthetic.main.post_header.*
import nl.vslcatena.vslcatena.BaseFragment
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.models.Role
import nl.vslcatena.vslcatena.models.User
import nl.vslcatena.vslcatena.util.LiveDataWrapper
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.extensions.formatReadable
import nl.vslcatena.vslcatena.util.extensions.observeOnce
import nl.vslcatena.vslcatena.util.extensions.setImageFromFirebaseStorage
import nl.vslcatena.vslcatena.util.login.LoginProvider
import nl.vslcatena.vslcatena.util.login.AuthenticationLevel
import java.text.SimpleDateFormat

/**
 * Fragment for showing a single newsItem.
 *
 */
@AuthenticationLevel(Role.USER)
class NewsItemFragment : BaseFragment() {

    private val user = LiveDataWrapper<User>()
    private lateinit var news: LiveData<News>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        news = DataCreator.getSingleReference(
            News::class.java,
            Identifier(NewsItemFragmentArgs.fromBundle(arguments).itemId)
        )

        // Hook the news user to the user observer
        news.observeOnce(this, Observer { user.wrap(it.userReference()) })

        news.observe(this, Observer {
            item_title.text = it.title
            item_content.text = it.content
            postHeaderPostDate.text = it.date.formatReadable()

            if (it.date != it.dateLastEdited) {
                DataCreator.getSingleReference(User::class.java, it.lastEditedUserId())
                    .observeOnce(this, Observer { editUser ->
                        postHeaderEditText.visibility = View.VISIBLE
                        postHeaderEditText.text =
                            getString(R.string.post_header_edited, it.dateLastEdited.formatReadable(), editUser.name)
                    })
            }
        })

        user.observe(this, Observer {
            postHeaderUserName.text = it.name
            postHeaderUserImage.setImageFromFirebaseStorage(it.getThumbnailRef())
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_icon_menu, menu)
        inflater.inflate(R.menu.delete_icon_menu, menu)

        LoginProvider.currentUser.observe(this, Observer {
            if (it?.hasClearance(Role.ADMIN) == true) {
                menu?.findItem(R.id.edit)?.isVisible = true
                menu?.findItem(R.id.delete)?.isVisible = true
            } else {
                menu?.findItem(R.id.edit)?.isVisible = false
                menu?.findItem(R.id.delete)?.isVisible = false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
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
                    .setPositiveButton(R.string.general_yes) { _, _ ->
                        news.value?.let { news ->
                            if (news.id != null) {
                                DataCreator.delete(news)
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
                    .setNegativeButton(R.string.general_no) { _, _ -> }
                    .show()

            }
            else -> return false
        }
        return true
    }
}
