package nl.vslcatena.vslcatena.models.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.models.User
import java.util.concurrent.ConcurrentHashMap

class UserPool : ViewModel() {
    private val users = ConcurrentHashMap<Identifier, LiveData<User>>()

    fun getUser(id: Identifier): LiveData<User> {
        var user = users[id]
        if (user == null) {
            user = DataCreator.getSingleReference(User::class.java, id)
            users[id] = user
        }

        return user
    }
}