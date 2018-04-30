package nl.vslcatena.vslcatena.controllers

import android.os.Bundle
import android.support.design.widget.Snackbar

import kotlinx.android.synthetic.main.activity_home.*
import nl.vslcatena.vslcatena.R

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
