package nl.vslcatena.vslcatena.controllers

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import kotlinx.android.synthetic.main.activity_home.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.firebase.LiveViewModel
import nl.vslcatena.vslcatena.models.News

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        LiveViewModel.of(this).observeList(News::class.java) {
            it.forEach {
                Log.d("Test", "#" + it.id + " " + it.title + " " + it.content)
            }
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
