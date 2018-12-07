package nl.vslcatena.vslcatena.abstraction.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import androidx.navigation.fragment.findNavController
import nl.vslcatena.vslcatena.NavGraphDirections
import nl.vslcatena.vslcatena.util.TempLogInProvider

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleUserAuth()
    }

    override fun onResume() {
        super.onResume()
        handleUserAuth()
    }

    private fun userIsAuthenticated(): Boolean {
        //TODO this should be done via dependency inject, to make it more flexible and testable. Consider adding Dagger2.
//        return FirebaseAuth.getInstance().currentUser != null
        return TempLogInProvider.isAuthenticated
    }

    private fun handleUserAuth(){
        val needsAuth = this::class.java.getAnnotation(NeedsAuthentication::class.java) != null

        if(needsAuth) {
            Log.d("Auth", "This fragment (${this::class.java}) needs auth")
            if (!userIsAuthenticated()) {
                findNavController().navigate(NavGraphDirections.actionGlobalLoginFragment())
            }
        }
        else
            Log.d("Auth", "This fragment (${this::class.java}) doesn't need auth")

    }



}
