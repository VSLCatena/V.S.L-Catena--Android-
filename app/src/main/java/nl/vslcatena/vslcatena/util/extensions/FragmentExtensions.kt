package nl.vslcatena.vslcatena.util.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment


fun Fragment.applyArguments(applyFunction: (bundle: Bundle) -> Unit) {
    if (arguments == null)
        arguments = Bundle()
    applyFunction(arguments!!)
}
