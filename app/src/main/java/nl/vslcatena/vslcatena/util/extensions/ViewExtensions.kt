package nl.vslcatena.vslcatena.util.extensions

import android.widget.ImageView
import com.google.firebase.storage.FirebaseStorage
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.util.GlideApp


fun ImageView.setImageFromFirebaseStorage(path: String) {
    GlideApp.with(context)
        .load(FirebaseStorage.getInstance().getReference(path))
        .placeholder(R.drawable.logo)
        .into(this)
}
