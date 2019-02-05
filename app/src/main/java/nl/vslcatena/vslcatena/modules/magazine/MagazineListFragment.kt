package nl.vslcatena.vslcatena.modules.magazine

import android.Manifest
import android.content.DialogInterface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Role
import nl.vslcatena.vslcatena.util.abstractions.BaseFirestorePagingFragment
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.downloadingFiles.downloadFileFromUrl
import nl.vslcatena.vslcatena.util.login.AuthenticationLevel
import java.text.SimpleDateFormat


@AuthenticationLevel(Role.USER)
class MagazineListFragment : BaseFirestorePagingFragment<Magazine, MagazineListFragment.MagazineViewHolder>() {
    override fun getItemClass() = Magazine::class.java
    override fun getItemLayout() = R.layout.magazine_item
    override fun createItemViewHolder(view: View) = MagazineViewHolder(view)

    override fun createQuery() = DataCreator
        .createQuery(Magazine::class.java)
        .orderBy("publish_date", Query.Direction.DESCENDING)


    private fun onMagazineClicked(magazine: Magazine) {
        AlertDialog.Builder(context!!).apply {
            setTitle("Ontekend downloaden?")
            setMessage("Wil je de ontketend '${magazine.title}' downloaden?")
            setPositiveButton("Ja"){ _: DialogInterface, _: Int ->
                requestDownload(magazine)
            }
            setNegativeButton("Nee"){ _: DialogInterface, _: Int ->  }
        }.show()
    }

    private fun requestDownload(magazine: Magazine) {
        requestPermissionsWithResults(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            getString(R.string.permission_download_for_magazine_explanation)
        ) {
            if (it) {
                FirebaseStorage.getInstance()
                    .getReference(magazine.getPdfRef()).downloadUrl
                    .addOnSuccessListener { uri ->
                        context!!.downloadFileFromUrl(magazine.title, uri.toString())
                    }
            } else {
                Toast.makeText(
                    context,
                    R.string.permission_download_for_magazine_denied,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    inner class MagazineViewHolder(val view: View)
        : RecyclerView.ViewHolder(view), BaseFirestorePagingFragment.Binder<Magazine>
    {
        val mCoverImageView: ImageView = itemView.findViewById(R.id.coverView)
        val mTitleView: TextView = itemView.findViewById(R.id.titleView)
        val mDateView: TextView = itemView.findViewById(R.id.dateView)

        override fun bind(item: Magazine) {
            mTitleView.text = item.title
            mDateView.text =  SimpleDateFormat("MMMM yyyy").format(item.publish_date)
            Glide.with(this@MagazineListFragment)
                .load(
                    FirebaseStorage.getInstance()
                        .getReference(item.getCoverPictureRef())
                        .apply { println("path is $path") }
                ).into(mCoverImageView)

            view.setOnClickListener {
                onMagazineClicked(item)
            }
        }
    }


}


