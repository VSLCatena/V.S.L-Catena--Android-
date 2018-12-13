package nl.vslcatena.vslcatena.modules.login.provider

import androidx.lifecycle.LiveData
import nl.vslcatena.vslcatena.modules.login.User
import nl.vslcatena.vslcatena.util.Result

interface LoginProvider {
    suspend fun authenticate(username: String, password: String): Result<User>

    val currentUser: LiveData<User?>

    fun getUser() = currentUser.value

    companion object {
        val provider: LoginProvider = TempLoginProvider()
    }
}