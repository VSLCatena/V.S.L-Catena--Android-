package nl.vslcatena.vslcatena

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import kotlinx.android.synthetic.main.base.*
import nl.vslcatena.vslcatena.controllers.MagazineListFragment

class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.base)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        navController = findNavController(this, R.id.nav_host)

    }

    override fun onSupportNavigateUp()
            = navController.navigateUp()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.base, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_news -> {
                navController.navigate(R.id.newsFragment)
            }
            R.id.nav_promo -> {
                navController.navigate(R.id.promoFragment)
            }
            R.id.nav_magazines -> {
                navController.navigate(Nav_graphDirections.actionGlobalMagazineFragment(resources.getInteger(R.integer.magazineColumnCount)))
            }
            R.id.nav_rules -> {

            }
            R.id.nav_faq -> {

            }
            R.id.nav_bingo -> {
                navController.navigate(R.id.bingoFragment)
            }
            R.id.nav_crying_wall -> {

            }
            R.id.nav_user_settings -> {
                navController.navigate(R.id.userSettingsFragment)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
