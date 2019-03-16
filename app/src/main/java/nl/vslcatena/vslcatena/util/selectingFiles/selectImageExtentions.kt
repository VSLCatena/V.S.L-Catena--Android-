package nl.vslcatena.vslcatena.util.selectingFiles

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment


private const val OPEN_IMAGE_CODE = 10001

/**
 * Lets the user pick an image from i.e. their gallery app.
 * To handle the callback add onUserPickedImage() to the onActivityResult method.
 */
fun Fragment.letUserPickImage(){
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "image/*"
    }
    startActivityForResult(intent, OPEN_IMAGE_CODE)
}

fun Fragment.onUserPickedImage(requestCode: Int, resultCode: Int, data: Intent, action: (imageUri: Uri?) -> Unit){
    if (requestCode == OPEN_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
        action(data.data)
    }
}