package nl.vslcatena.vslcatena.modules.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_news_add.*
import nl.vslcatena.vslcatena.BaseFragment
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.models.Role
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.extensions.observeOnce
import nl.vslcatena.vslcatena.util.login.AuthenticationLevel
import nl.vslcatena.vslcatena.util.login.LoginProvider
import java.util.*

@AuthenticationLevel(Role.ADMIN)
class NewsEditItemFragment : BaseFragment() {

    private var editId: Identifier? = null
    private var editItem: News? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            editId = NewsEditItemFragmentArgs.fromBundle(arguments).editId?.let {
                Identifier(it)
            }
        }

        editId?.let { editId ->
            showProgress(true)
            DataCreator.getSingleReference(News::class.java, editId).observeOnce(this, Observer {
                editItem = it
                title.setText(it.title)
                content.setText(it.content)
                showProgress(false)
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        post.setOnClickListener {
            post.isClickable = false
            post.isEnabled = false

            post()
        }
    }

    fun post() {
        val title = title.text?.toString()
        val content = content.text?.toString()

        if (title == null || content == null) {
            Toast.makeText(context, "Je moet wel alles invullen!", Toast.LENGTH_LONG).show()
            return
        }

        val news = News(
            editId,
            title = title,
            content = content,
            user = editItem?.user ?: LoginProvider.getUser()!!.id,
            date = editItem?.date ?: Date(),
            userLastEdited = LoginProvider.getUser()!!.id,
            dateLastEdited = Date()
        )

        DataCreator.set(news)
            .addOnSuccessListener {
                Toast.makeText(context, "Succesvol geplaatst!", Toast.LENGTH_LONG)
                    .show()
                findNavController().popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Oops! Er ging iets fout: " + it.message, Toast.LENGTH_LONG)
                    .show()
                post.isEnabled = true
                post.isClickable = true
            }
}
}