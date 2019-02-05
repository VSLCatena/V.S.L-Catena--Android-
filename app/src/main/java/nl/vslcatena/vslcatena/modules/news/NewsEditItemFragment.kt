package nl.vslcatena.vslcatena.modules.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.fragment_news_edit.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Role
import nl.vslcatena.vslcatena.util.Result
import nl.vslcatena.vslcatena.util.abstractions.EditItemFragment
import nl.vslcatena.vslcatena.util.extensions.await
import nl.vslcatena.vslcatena.util.login.AuthenticationLevel
import nl.vslcatena.vslcatena.util.login.UserProvider
import java.util.*

@AuthenticationLevel(Role.ADMIN)
class NewsEditItemFragment : EditItemFragment<News>() {

    override fun getItemClass() = News::class.java
    override fun getSubmitButton() = post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_edit, container, false)
    }

    override fun onChanged(news: News?) {
        if (news == null) return

        title.setText(news.title)
        content.setText(news.content)
    }

    override suspend fun doSubmit(): Result<out Any?> {
        val newTitle = title.text?.toString()
        val newContent = content.text?.toString()

        // We check if both title and content is filled in
        if (newTitle.isNullOrBlank() || newContent.isNullOrBlank()) {
            return Result.Failure(IllegalStateException("Je moet wel alles invullen!"))
        }

        // If we are editing an item use that item, otherwise we create a new one
        val newsItem = editItem
            ?: News(editId, "", "", UserProvider.getUser()!!.id, Timestamp(Date()))

        // Change the things that need to be changed
        newsItem.apply {
            title = newTitle
            content = newContent
            userLastEdited = UserProvider.getUser()!!.id
            dateLastEdited = Timestamp(Date())
        }

        // And then we await the result
        return newsItem.save().await()
    }
}