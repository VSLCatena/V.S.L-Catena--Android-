package nl.vslcatena.vslcatena.modules.magazine

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.abstraction.fragment.NeedsAuthentication
import nl.vslcatena.vslcatena.abstraction.lists.PagedFirebaseListFragment
import nl.vslcatena.vslcatena.util.PermissionRequestHelper
import nl.vslcatena.vslcatena.util.downloadingFiles.downloadFileFromUrl
import java.text.SimpleDateFormat
import java.util.*

@NeedsAuthentication
class MagazineListFragment : PagedFirebaseListFragment<Magazine, MagazineListFragment.MagazineViewHolder>(
    Magazine::class.java) {
    override val itemView = R.layout.magazine_item
    private var permissionRequestHelper: PermissionRequestHelper? = null

    override fun onListItemClicked(item: Magazine) {

        AlertDialog.Builder(context!!).apply {
            setTitle("Ontekend downloaden?")
            setMessage("Wil je de ontketend '${item.name}' downloaden?")
            setPositiveButton("Ja"){ _: DialogInterface, _: Int ->
                permissionRequestHelper = object: PermissionRequestHelper(this@MagazineListFragment){
                    override fun onRequestHandled(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
                        if(permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == 0)
                            FirebaseStorage.getInstance().getReference(item.getPdfRef()).downloadUrl.addOnSuccessListener {
                                downloadFileFromUrl(context, item.name, it.toString())
                            }
                    }
                }
                permissionRequestHelper!!.requestPermission(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
            setNegativeButton("Nee"){ _: DialogInterface, _: Int ->  }
        }.show()







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
           mDateView.text =  SimpleDateFormat("MMMM yyyy").format(Date(item.publishDate))
            Glide.with(this@MagazineListFragment)
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


