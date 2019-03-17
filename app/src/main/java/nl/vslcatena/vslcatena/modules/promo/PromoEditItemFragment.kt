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
import android.content.Intent
import android.app.Activity.RESULT_OK
import android.net.Uri
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.FirebaseStorage
import nl.vslcatena.vslcatena.util.Paths
import nl.vslcatena.vslcatena.util.selectingFiles.letUserPickImage
import nl.vslcatena.vslcatena.util.selectingFiles.onUserPickedImage
import java.io.File


@AuthenticationLevel(Role.USER)
class PromoEditItemFragment : EditItemFragment<PromoItem>() {

    //The uri of the selected image (if any)
    var imageUri: Uri? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        addImage.setOnClickListener {
            letUserPickImage()
        }
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

        //using uuid for filename based upon https://stackoverflow.com/a/37444839
        if(imageUri != null) {
            //todo remove the old image (if any) from firestore?
            val uuid = UUID.randomUUID().toString()
            val extention = File(imageUri!!.path).extension
            promoItem.imageRef = "$uuid.$extention"
            val storage = FirebaseStorage.getInstance()
                    .getReference("promo/${promoItem.imageRef}")
            return storage.putFile(imageUri!!)
                    .continueWith { promoItem.save() }
                    .await()
        } else {
            return promoItem.save().await()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            onUserPickedImage(requestCode, resultCode, it) { imageUri ->
                selectedImageDisplay.setImageURI(imageUri)
                addImage.setText(R.string.select_image_edit)
                this.imageUri = imageUri
            }
        }

    }
}