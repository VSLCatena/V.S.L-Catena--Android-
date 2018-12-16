package nl.vslcatena.vslcatena.modules.login.provider

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import nl.vslcatena.vslcatena.modules.login.Role
import nl.vslcatena.vslcatena.modules.login.User
import nl.vslcatena.vslcatena.util.Result

//TODO remove when actual login server is set up.
class TempLoginProvider: LoginProvider {

    override val currentUser = MutableLiveData<User?>()

    override suspend fun authenticate(username: String, password: String): Result<User> {
        delay(2000)
        if (username != "test" || password != "test")
            return Result.Failure(Exception("Invalid credentials"))

        val newUser = User("00-000", "Test user", Role.USER.value)

        currentUser.value = newUser

        return Result.Success(newUser)
    }

}