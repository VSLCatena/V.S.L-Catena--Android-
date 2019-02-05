package nl.vslcatena.vslcatena.modules.promo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.fragment_promo_edit.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Role
import nl.vslcatena.vslcatena.util.Result
import nl.vslcatena.vslcatena.util.abstractions.EditItemFragment
import nl.vslcatena.vslcatena.util.extensions.await
import nl.vslcatena.vslcatena.util.login.AuthenticationLevel
import nl.vslcatena.vslcatena.util.login.UserProvider
import java.util.*

@AuthenticationLevel(Role.USER)
class PromoEditItemFragment : EditItemFragment<PromoItem>() {

    override fun getItemClass() = PromoItem::class.java
    override fun getSubmitButton() = post

    override fun onChanged(promoItem: PromoItem?) {
        if (promoItem == null) return

        title.setText(promoItem.title)
        content.setText(promoItem.content)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_promo_edit, container, false)
    }

    override suspend fun doSubmit(): Result<out Any?> {
        val newTitle = title.text?.toString()
        val newContent = content.text?.toString()

        // We check if both title and content is filled in
        if (newTitle.isNullOrBlank() || newContent.isNullOrBlank()) {
            return Result.Failure(IllegalStateException("Je moet wel alles invullen!"))
        }

        // If we are editing an item use that item, otherwise we create a new one
        val promoItem = editItem ?: PromoItem(editId, "", "")

        // Change the things that need to be changed
        promoItem.apply {
            title = newTitle
            content = newContent
            userLastEdited = UserProvider.getUser()!!.id
            dateLastEdited = Timestamp(Date())
        }

        // And then we await the result
        return promoItem.save().await()
    }
}