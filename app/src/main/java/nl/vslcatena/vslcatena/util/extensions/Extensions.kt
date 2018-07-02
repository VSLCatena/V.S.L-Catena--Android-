package nl.vslcatena.vslcatena.util.extensions

import android.os.Bundle
import android.support.v4.app.Fragment

//If this gets to big, split in multiple files.

fun Fragment.applyArguments(applyFunction: (bundle: Bundle) -> Unit){
    if(arguments == null)
        arguments = Bundle()
    applyFunction(arguments!!)
}
