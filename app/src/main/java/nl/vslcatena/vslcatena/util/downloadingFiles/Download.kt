package nl.vslcatena.vslcatena.util.downloadingFiles

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import nl.vslcatena.vslcatena.R
import java.io.File

/**
 * Helper method to download a file from a url.
 * @param context Context object needed to get the DownloadManager.
 * @param filename The name the file should have when downloaded.
 * @param url The url the file should be downloaded from.
 */
fun Context.downloadFileFromUrl(filename: String, url: String) {
    val uri = Uri.parse(url)
    val downloadManager: DownloadManager =
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    val request = DownloadManager.Request(uri).apply {
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        setAllowedOverRoaming(false)
        setTitle(filename)
        setDescription("Downloading $filename")
        setVisibleInDownloadsUi(true)
        setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${getString(R.string.app_name)}/$filename"
        )
    }
    val downloadRef = downloadManager.enqueue(request)

    val myDownloadQuery = DownloadManager.Query()
    //set the query filter to our previously Enqueued download
    myDownloadQuery.setFilterById(downloadRef)
}


class DownloadBroadcastReceiver : BroadcastReceiver() {
    /**
     * Broafvast receiver to show when a file is downloaded successfully and link on click.
     * In Manifest
    <receiver android:name="DownloadBroadcastReceiver">
    <intent-filter>
    <action android:name="android.intent.action.DOWNLOAD_COMPLETE"/>
    </intent-filter>
    </receiver>
     */
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
            val reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val downloadManager: DownloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(reference))


            val index = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            if (cursor.moveToFirst()) {
                if (cursor.getInt(index) == DownloadManager.STATUS_SUCCESSFUL) {

                    var downloadFilePath: String? = null
                    val downloadFileLocalUri =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                    if (downloadFileLocalUri != null) {
                        val mFile = File(Uri.parse(downloadFileLocalUri).path)
                        downloadFilePath = mFile.absolutePath
                    }


                    val descriptor = downloadManager.openDownloadedFile(reference)
                    downloadManager.addCompletedDownload(
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)),
                        "description",
                        true,
                        downloadManager.getMimeTypeForDownloadedFile(reference),
                        downloadFilePath,
                        descriptor.statSize,
                        true
                    )
                }
            }
            cursor.close()


        }
    }
}

