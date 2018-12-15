package nl.vslcatena.vslcatena

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.base.*

class BaseActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.base)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        // We grab a list of menu items which we can then set as top-level items
        val menuItems = menuReader(nav_view.menu).map { it.itemId }

        // The AppBarConfiguration that will manage the up and drawer button
        val appBarConfiguration = AppBarConfiguration(menuItems.toSet(), drawer_layout)

        // Link the menus to the navController so that the navController handles the menu clicks
        toolbar.setupWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

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

    override fun onSupportNavigateUp()
            = navController.navigateUp()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.base, menu)
        return true
    }

    // We want to close the drawer if it's open, otherwise we want our parent to handle it
    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(nav_view))
            drawer_layout.closeDrawer(nav_view)
        else super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}
