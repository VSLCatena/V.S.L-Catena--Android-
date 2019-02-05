package nl.vslcatena.vslcatena

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nl.vslcatena.vslcatena.util.Result
import nl.vslcatena.vslcatena.util.login.UserProvider
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), CoroutineScope {

    lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        setContentView(R.layout.activity_login)

        sign_in_button.setOnClickListener { launch { attemptLogin() } }
        forgot_password_button.setOnClickListener {
            Toast.makeText(
                this,
                "Moet nog geimplementeerd worden",
                Toast.LENGTH_SHORT
            ).show()
        }
        Toast.makeText(this, "Username: test, Password: test", Toast.LENGTH_LONG).show()

        if (FirebaseAuth.getInstance()?.currentUser != null) {
            launch { attemptLoginByFirebase() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private suspend fun attemptLoginByFirebase() {
        showProgress(true)
        if (UserProvider.doLoginFromFirebase().isSuccesful()) {
            showProgress(false)
            goToNext()
        } else {
            showProgress(false)
        }

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private suspend fun attemptLogin() {
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

        val result = UserProvider.authenticate(userNameString, passwordStr)
        if (result is Result.Success) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            goToNext()
        } else {
            Toast.makeText(
                this,
                result.getExceptionOrNull()?.message ?: "Unknown error",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 1 else 0).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (login_progress != null)
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
    }

    private fun goToNext() {
        startActivity(Intent(this, BaseActivity::class.java))
    }
}