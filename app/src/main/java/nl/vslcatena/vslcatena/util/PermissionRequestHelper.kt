package nl.vslcatena.vslcatena.util

import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * Helper to call an action when requesting a permission
 * In the onRequestPermissionsResult of the Activity or Fragment call the onRequestHandled method of this object.
 * To request a permission call the requestPermission method.
 * //Todo Add option to show dialog why the permission is needed if the user blocks the permission request.
 */
abstract class PermissionRequestHelper private constructor(
        val fragment: Fragment? = null,
        val activity: FragmentActivity? = null
){
    constructor(fragment: Fragment): this(fragment = fragment, activity = null)
    constructor(activity: FragmentActivity): this(activity = activity, fragment = null)

    //Extra content that can be added to use in the onRequestHandled method.
    val content: MutableMap<String, Any> = HashMap()

    /**
     * Requests the given permissions using the given activity or fragment.
     */
    fun requestPermission(permissions: Array<out String>, requestCode: Int){
        fragment?.requestPermissions(permissions, requestCode)
        activity?.let { ActivityCompat.requestPermissions(it, permissions, requestCode) }
    }

    /**
     * Action that needs to be run when the request is completed. This must be called in the onRequestPermissionsResult method of the activity or fragment.
     */
    abstract fun onRequestHandled(requestCode: Int,  permissions: Array<out String>, grantResults: IntArray)

}