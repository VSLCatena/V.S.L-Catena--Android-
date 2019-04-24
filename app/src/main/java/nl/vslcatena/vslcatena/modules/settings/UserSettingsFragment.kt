package nl.vslcatena.vslcatena.modules.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_settings.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.util.Paths
import nl.vslcatena.vslcatena.util.extensions.setImageFromFirebaseStorage
import nl.vslcatena.vslcatena.util.login.UserProvider
import nl.vslcatena.vslcatena.util.selectingFiles.letUserPickImage
import nl.vslcatena.vslcatena.util.selectingFiles.onUserPickedImage

/**
 * Fragment where the user can adjust setting relating to his account:
 *  - Change his profile picture
 *  - Manage subscriptions?
 *
 *
 * For the view go to: fragment_settings.xml
 * This view is loaded using a style in values/styles.xml
 */
class UserSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserProvider.getUser()?.let {
            username.text = it.name
            userId.text = "LIT-nummer?"
            userId.text = it.email
            profileImage.setImageFromFirebaseStorage(Paths.getStorageThumbnailPath(it.id))

            profileImage.setOnClickListener {
                letUserPickImage()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            onUserPickedImage(requestCode, resultCode, it) { imageUri ->
                profileImage.setImageURI(imageUri)

                //todo should upload to somewhere else than thumbnail and then create on using a firebase function
                FirebaseStorage.getInstance()
                        .getReference(Paths.getStorageThumbnailPath(UserProvider.getUser()!!.id))
                        .putFile(imageUri!!)
                        .addOnSuccessListener { Toast.makeText(this.context, "profile picture uploaded succesfully", Toast.LENGTH_LONG).show() }
                        .addOnFailureListener { Toast.makeText(this.context, "profile picture upload failed", Toast.LENGTH_LONG).show()}


            }
        }

    }
}
