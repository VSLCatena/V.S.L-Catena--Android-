package nl.vslcatena.vslcatena.modules.promo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_news_edit.*
import kotlinx.coroutines.launch
import nl.vslcatena.vslcatena.BaseCoroutineFragment
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.models.Role
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.extensions.await
import nl.vslcatena.vslcatena.util.extensions.observeOnce
import nl.vslcatena.vslcatena.util.login.AuthenticationLevel
import nl.vslcatena.vslcatena.util.login.UserProvider
import java.util.*

@AuthenticationLevel(Role.USER)
class PromoEditItemFragment : BaseCoroutineFragment() {

    private var editId: Identifier? = null
    private var editItem: PromoItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            editId = PromoEditItemFragmentArgs.fromBundle(arguments).editId?.let {
                Identifier(it)
            }
        }

        editId?.let { editId ->
            showProgress(true)
            DataCreator.getSingleReference(PromoItem::class.java, editId)
                .observeOnce(this, Observer {
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
        return inflater.inflate(R.layout.fragment_news_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        post.setOnClickListener {
            post.isClickable = false
            post.isEnabled = false

            // We launch a coroutine to post the news item
            launch { postPromoItem() }
        }
    }

    suspend fun postPromoItem() {
        val newTitle = title.text?.toString()
        val newContent = content.text?.toString()

        // We check if both title and content is filled in
        if (newTitle.isNullOrBlank() || newContent.isNullOrBlank()) {
            Toast.makeText(context, "Je moet wel alles invullen!", Toast.LENGTH_LONG).show()
            return
        }

        // If we are editing an item use that item, otherwise we create a new one
        val promoItem = editItem ?: PromoItem(editId, "", "")

        // Change the things that need to be changed
        promoItem.apply {
            title = newTitle
            content = newContent
            userLastEdited = UserProvider.getUser()!!.id
            dateLastEdited = Date()
        }

        // And then we await the result
        val result = promoItem.save().await()


        if (result.isSuccesful()) {
            // If we were successful we show a toast and we go back
            Toast.makeText(context, "Succesvol geplaatst!", Toast.LENGTH_LONG)
                .show()
            findNavController().popBackStack()
        } else {
            // If we failed we show a toast telling what went wrong, and then re-enable the button
            Toast.makeText(
                context,
                "Oops! Er ging iets fout: " + result.getExceptionOrNull()?.message,
                Toast.LENGTH_LONG
            )
                .show()
            post.isEnabled = true
            post.isClickable = true
        }
    }
}