package nl.vslcatena.vslcatena


import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.vslcatena.vslcatena.util.login.LoginProvider
import nl.vslcatena.vslcatena.util.login.NeedsAuthentication
import java.util.*

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleUserAuth()
    }

    override fun onResume() {
        super.onResume()
        handleUserAuth()
    }

    private fun handleUserAuth() {
        val authentication = this::class.java.getAnnotation(NeedsAuthentication::class.java)

        // If we don't need authentication, we skip checking
        if (authentication == null) {
            if (BuildConfig.DEBUG)
                Log.d(AUTH_LOG_TAG, "This fragment (${this::class.java}) doesn't need auth")
            return
        }

        // Now we know we need authentication

        if (BuildConfig.DEBUG)
            Log.d(AUTH_LOG_TAG, "This fragment (${this::class.java}) needs auth")

        // We grab the logged in user
        val user = LoginProvider.provider.getUser()

        // If we aren't logged in, we want to redirect the user
        if (user == null) {
            if (BuildConfig.DEBUG)
                Log.d(AUTH_LOG_TAG, "User is not logged in, redirecting to login page")
            findNavController().navigate(NavGraphDirections.actionGlobalLoginFragment())
            return
        }

        // Then we check if the user has enough clearance to view this page
        if (!user.role().hasClearance(authentication.role)) {
            if (BuildConfig.DEBUG)
                Log.d(AUTH_LOG_TAG, "User is logged in but doesn't have enough clearance")
            Toast.makeText(context!!, R.string.auth_no_clearance, Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
            return
        }

        if (BuildConfig.DEBUG)
            Log.d(AUTH_LOG_TAG, "All good! :)")
        // All good! :)
    }

    private fun hasOrRequestPermissions(
        permissions: Array<out String>,
        reason: String,
        requestCode: Int
    ): Boolean {
        if (
            permissions.all {
                ContextCompat.checkSelfPermission(context!!, it) ==
                        PackageManager.PERMISSION_GRANTED
            }
        ) return true

        if (permissions.any { shouldShowRequestPermissionRationale(it) }) {
            AlertDialog.Builder(context)
                .setTitle(R.string.permission_request_title)
                .setMessage(reason)
                .setNeutralButton(R.string.general_ok) { _, _ ->
                    requestPermissions(permissions, requestCode)
                }
                .show()
        } else {
            requestPermissions(permissions, requestCode)
        }

        return false
    }


    private val permissionRequestQueue = HashMap<Int, (wasSuccessful: Boolean) -> Unit>()
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val wasSuccessful = permissions.all {
            ContextCompat.checkSelfPermission(context!!, it) == PackageManager.PERMISSION_GRANTED
        }
        permissionRequestQueue[requestCode]?.invoke(wasSuccessful)
    }

    fun requestPermissionsWithResults(
        permissions: Array<out String>,
        reason: String,
        block: (wasSuccessful: Boolean) -> Unit
    ) {
        // Otherwise we have a chance of getting "Can only use lower 16 bits for requestCode"
        // as error
        val requestCode = Random().nextInt(65535)
        if (hasOrRequestPermissions(permissions, reason, requestCode)) {
            block.invoke(true)
        }
        permissionRequestQueue[requestCode] = block
    }

    companion object {
        private const val AUTH_LOG_TAG = "Base_AUTH"
    }
}
