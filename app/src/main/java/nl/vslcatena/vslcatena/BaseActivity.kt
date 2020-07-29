package nl.vslcatena.vslcatena

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_base.*
import nl.vslcatena.vslcatena.util.login.UserProvider


class BaseActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        // We grab a list of menu items which we can then set as top-level items
        val menuItems = menuReader(nav_view.menu).map { it.itemId }

        // The AppBarConfiguration that will manage the up and drawer button
        val appBarConfiguration = AppBarConfiguration(menuItems.toSet(), drawer_layout)

        // Link the menus to the navController so that the navController handles the menu clicks
        toolbar.setupWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)


        UserProvider.currentUser.observe(this, Observer { user ->
            findViewById<TextView>(R.id.drawer_name)?.let {
                it.visibility = View.VISIBLE
                it.text = user?.name
            }
        })
    }

    /**
     * A simple function that reads through a menu and will recursively read out all the menu items
     */
    private fun menuReader(menu: Menu): List<MenuItem> {
        val menuItems = ArrayList<MenuItem>()

        // Go through every item in the menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            // Add it to the list
            menuItems.add(item)
            // If it has a subMenu recursively add all those items as well
            item.subMenu?.let {
                menuItems.addAll(menuReader(it))
            }
        }

        return menuItems
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    // We want to close the drawer if it's open, otherwise we want our parent to handle it
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(nav_view))
            drawer_layout.closeDrawer(nav_view)
        else super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        progress.visibility = if (show) View.VISIBLE else View.GONE
        progress.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 1 else 0).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (progress != null)
                        progress.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
    }

    fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        val view = currentFocus ?: View(this)

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
