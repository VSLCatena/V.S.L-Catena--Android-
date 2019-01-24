package nl.vslcatena.vslcatena.util.login

import androidx.lifecycle.LiveData
import nl.vslcatena.vslcatena.models.User
import nl.vslcatena.vslcatena.util.Result

interface LoginProvider {
    suspend fun authenticate(username: String, password: String): Result<User>

    val currentUser: LiveData<User?>

    fun getUser() = currentUser.value

    companion object : LoginProvider {
        val provider: LoginProvider =
            TempLoginProvider()

        override val currentUser: LiveData<User?>
            get() = provider.currentUser

        override suspend fun authenticate(username: String, password: String) =
            provider.authenticate(username, password)
    }
}