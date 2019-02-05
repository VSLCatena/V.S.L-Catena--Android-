package nl.vslcatena.vslcatena.util.componentholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import com.google.firebase.Timestamp
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.models.User
import nl.vslcatena.vslcatena.models.viewmodels.UserPool
import nl.vslcatena.vslcatena.util.abstractions.BaseFirestorePagingFragment
import nl.vslcatena.vslcatena.util.abstractions.LifecycleAwareViewHolder
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.extensions.formatReadable
import nl.vslcatena.vslcatena.util.extensions.observeOnce
import nl.vslcatena.vslcatena.util.extensions.setImageFromFirebaseStorage

/**
 * ViewHolder for post_header.xml that can be used in different viewholders
 */
open class PostHeaderViewHolder<T : PostHeaderViewHolder.PostHeaderProvider>(
    private val userPool: UserPool,
    view: View
) : LifecycleAwareViewHolder(view), Observer<User>, BaseFirestorePagingFragment.Binder<T> {

    val thumbnailView: ImageView = itemView.findViewById(R.id.postHeaderUserImage)
    val userNameView: TextView = itemView.findViewById(R.id.postHeaderUserName)
    val dateView: TextView = itemView.findViewById(R.id.postHeaderPostDate)
    val editedView: TextView = itemView.findViewById(R.id.postHeaderEditText)

    override fun onChanged(user: User?) {
        if (user == null) return

        userNameView.text = user.name
        thumbnailView.setImageFromFirebaseStorage(user.getThumbnailRef())
    }

    @CallSuper
    override fun bind(item: T) {
        dateView.text = item.datePosted().toDate().formatReadable()
        userPool.getUser(item.userPostingId()).observeOnce(this, this)
        val lastEditedId = item.lastEditedUserId()
        if (lastEditedId != null && item.datePosted() != item.lastEditedDate()) {
            userPool.getUser(lastEditedId).observeOnce(this, Observer {
                editedView.visibility = View.VISIBLE
                editedView.text = editedView.context
                    .getString(R.string.post_header_edited, it.name)
            })
        }
    }

    interface PostHeaderProvider {
        fun userPostingId(): Identifier
        fun datePosted(): Timestamp
        fun lastEditedUserId(): Identifier?
        fun lastEditedDate(): Timestamp?

        fun userReference() =
            DataCreator.getSingleReference(User::class.java, userPostingId())

    }
}