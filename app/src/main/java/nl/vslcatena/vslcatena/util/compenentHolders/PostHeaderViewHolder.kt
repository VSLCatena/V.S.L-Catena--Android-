package nl.vslcatena.vslcatena.util.compenentHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import nl.vslcatena.vslcatena.R

/**
 * ViewHolder for post_header.xml that can be used in different viewholders
 */
class PostHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val mUserNameView: TextView = itemView.findViewById(R.id.postHeaderUserName)
    val mDateView: TextView = itemView.findViewById(R.id.postHeaderPostDate)
    val mThumbnailView: ImageView = itemView.findViewById(R.id.postHeaderUserImage)

}