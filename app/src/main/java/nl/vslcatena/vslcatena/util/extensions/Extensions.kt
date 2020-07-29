package nl.vslcatena.vslcatena.util.extensions


import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

//If this gets to big, split in multiple files.


fun Timestamp.formatReadable(): String =
    this.toDate().formatReadable()

fun Date.formatReadable(): String =
    SimpleDateFormat("EEEE d MMMM yyyy HH:mm", Locale.getDefault()).format(this)