package nl.vslcatena.vslcatena.util.componentholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.models.User
import nl.vslcatena.vslcatena.models.viewmodels.UserPool
import nl.vslcatena.vslcatena.util.abstractions.FirestorePagingFragment
import nl.vslcatena.vslcatena.util.abstractions.LifecycleAwareViewHolder
import nl.vslcatena.vslcatena.util.extensions.setImageFromFirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewHolder for post_header.xml that can be used in different viewholders
 */
abstract class PostHeaderViewHolder<T : PostHeaderViewHolder.PostHeaderProvider>(
    private val userPool: UserPool,
    view: View
) : LifecycleAwareViewHolder(view), Observer<User?>, FirestorePagingFragment.Binder<T> {

    val thumbnailView: ImageView = itemView.findViewById(R.id.postHeaderUserImage)
    val userNameView: TextView = itemView.findViewById(R.id.postHeaderUserName)
    val dateView: TextView = itemView.findViewById(R.id.postHeaderPostDate)

    override fun onChanged(user: User?) {
        if (user == null) return

        userNameView.text = user.name
        thumbnailView.setImageFromFirebaseStorage(user.getThumbnailRef())
    }

    @CallSuper
    override fun bind(item: T) {
        dateView.text = formatDate(item.getDatePosted())
        userPool.getUser(item.getUserPostingId()).observe(this, this)
    }

    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat.getDateTimeInstance()
        return sdf.format(date)
    }

    interface PostHeaderProvider {
        fun getUserPostingId(): Identifier
        fun getDatePosted(): Date
    }
}