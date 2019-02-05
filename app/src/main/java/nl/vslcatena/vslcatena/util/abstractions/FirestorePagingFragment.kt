package nl.vslcatena.vslcatena.util.abstractions

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.recyclerview.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Role
import nl.vslcatena.vslcatena.models.viewmodels.UserPool
import nl.vslcatena.vslcatena.modules.news.News
import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.login.UserProvider

abstract class FirestorePagingFragment<T : BaseModel, B> : BaseFirestorePagingFragment<T, B>()
        where B : RecyclerView.ViewHolder, B : BaseFirestorePagingFragment.Binder<T> {
    protected lateinit var userPool: UserPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        userPool = ViewModelProviders.of(this).get(UserPool::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_icon_menu, menu)

        UserProvider.currentUser.observe(this, Observer { user ->
            menu?.findItem(R.id.add)?.isVisible = user.hasClearance(requiredAddRole())
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.add) {
            if (UserProvider.getUser()?.hasClearance(requiredAddRole()) == true) {
                findNavController().navigate(addNavigationId())
            }
            return true
        }
        return false
    }

    override fun createQuery() = DataCreator
        .createQuery(getItemClass())
        .orderBy("date", Query.Direction.DESCENDING)

    abstract fun requiredAddRole(): Role
    abstract fun addNavigationId(): Int
}