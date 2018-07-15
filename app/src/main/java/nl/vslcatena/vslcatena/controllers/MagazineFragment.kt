package nl.vslcatena.vslcatena.controllers

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.lists.PagedFirebaseListFragment
import nl.vslcatena.vslcatena.models.Magazine
import nl.vslcatena.vslcatena.util.GlideApp
import nl.vslcatena.vslcatena.util.PermissionRequestHelper
import nl.vslcatena.vslcatena.util.downloadingFiles.downloadFileFromUrl
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MagazineFragment : PagedFirebaseListFragment<Magazine, MagazineFragment.MagazineViewHolder>(Magazine::class.java) {
    override val itemView = R.layout.magazine_item
    private var permissionRequestHelper: PermissionRequestHelper? = null

    override fun onListItemClicked(item: Magazine) {
        Toast.makeText(context, "Clicked on ${item.name} with id: ${item.id}", Toast.LENGTH_SHORT).show()
        permissionRequestHelper = object: PermissionRequestHelper(this){
            //todo maybe show a popup first asking the user if they want to download the mag.
            override fun onRequestHandled(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
                if(permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == 0)
                FirebaseStorage.getInstance().getReference(item.getPdfRef()).downloadUrl.addOnSuccessListener {
                    downloadFileFromUrl(context!!, item.name, it.toString())
                }
            }
        }

        permissionRequestHelper!!.requestPermission(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)




    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionRequestHelper?.onRequestHandled(requestCode, permissions, grantResults)
    }

    override fun createViewHolder(view: View) = MagazineViewHolder(view)

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MagazineViewHolder, position: Int, item: Magazine) {
        with(holder){
            mTitleView.text = item.name
            mDateView.text = item.publishDate.toString() //todo convert
            mDateView.text =  SimpleDateFormat("MMMM YYYY").format(Date(item.publishDate))
            GlideApp.with(this@MagazineFragment)
                                .load(FirebaseStorage.getInstance().getReference(item.getCoverPictureRef()).apply { println("path is $path") })
                                .into(mCoverImageView)
        }
    }


    inner class MagazineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mCoverImageView: ImageView = itemView.findViewById(R.id.coverView)
        val mTitleView: TextView = itemView.findViewById(R.id.titleView)
        val mDateView: TextView = itemView.findViewById(R.id.dateView)

    }

}


