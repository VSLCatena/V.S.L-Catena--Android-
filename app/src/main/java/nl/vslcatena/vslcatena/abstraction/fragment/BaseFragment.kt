package nl.vslcatena.vslcatena.abstraction.fragment


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.vslcatena.vslcatena.BuildConfig
import nl.vslcatena.vslcatena.NavGraphDirections
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.modules.login.provider.LoginProvider

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleUserAuth()
    }

    override fun onResume() {
        super.onResume()
        handleUserAuth()
    }

    private fun handleUserAuth(){
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
        if (user.role()?.hasClearance(authentication.role) != true) {
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

    companion object {
        private const val AUTH_LOG_TAG = "Base_AUTH"
    }
}
