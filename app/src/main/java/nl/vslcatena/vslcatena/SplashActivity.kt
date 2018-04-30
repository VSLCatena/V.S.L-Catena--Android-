package nl.vslcatena.vslcatena

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import nl.vslcatena.vslcatena.controllers.HomeActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, HomeActivity::class.java))
    }
}
