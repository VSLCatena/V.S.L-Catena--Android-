package nl.vslcatena.vslcatena

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sign_in_button.setOnClickListener { login() }
    }

    override fun onResume() {
        super.onResume()

        if (FirebaseAuth.getInstance().currentUser != null) {
            goToNext()
            return
        }

        checkIfLoggingIn()
    }

    private fun login() {
        val oauthProvider = OAuthProvider.newBuilder("microsoft.com")
            .addCustomParameter("tenant", "2ea9aa7a-5a05-49cb-8307-63467188daa2")
            .build()

        FirebaseAuth.getInstance().startActivityForSignInWithProvider(this, oauthProvider)
            .addOnCanceledListener {
                Toast.makeText(this, "Not logged in", Toast.LENGTH_LONG).show()
            }
            .addOnCompleteListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun checkIfLoggingIn() {
        // If there are no pending results, we return immediately
        val pendingResultTask: Task<AuthResult> = FirebaseAuth.getInstance().pendingAuthResult
            ?: return

        // There's something already here! Finish the sign-in for your user.
        pendingResultTask
            .addOnSuccessListener {
                // User is signed in.
                // IdP data available in
                // authResult.getAdditionalUserInfo().getProfile().
                // The OAuth access token can also be retrieved:
                // authResult.getCredential().getAccessToken().
                // The OAuth ID token can also be retrieved:
                // authResult.getCredential().getIdToken().
                goToNext()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    getString(R.string.general_error, it.message),
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun goToNext() {
        startActivity(Intent(this, BaseActivity::class.java))
    }
}