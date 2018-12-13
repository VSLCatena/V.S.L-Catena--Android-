package nl.vslcatena.vslcatena.modules.login


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.modules.login.provider.LoginProvider
import nl.vslcatena.vslcatena.util.Result
import kotlin.coroutines.CoroutineContext


/**
 * Fragment that handles the Login.
 *
 */
class LoginFragment : Fragment(), CoroutineScope {

    lateinit var job: Job
    override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_in_button.setOnClickListener { attemptLogin() }
        forgot_password_button.setOnClickListener{ Toast.makeText(context, "Moet nog geimplementeerd worden", Toast.LENGTH_SHORT).show()}
        Toast.makeText(context, "Username: test, Password: test", Toast.LENGTH_LONG).show()
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        // Reset errors.
        username.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val userNameString = username.text.toString()
        val passwordStr = password.text.toString()


        // Check if username is filled in.
        if (TextUtils.isEmpty(userNameString)) {
            username.error = getString(R.string.error_field_required)
            username.requestFocus()
            return
        }

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true)
        launch {
            val result = LoginProvider.provider.authenticate(userNameString, passwordStr)
            if (result is Result.Success) {
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    context!!,
                    result.getExceptionOrNull()?.message ?: "Unknown error",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        if(login_progress!=null)
                            login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

}
