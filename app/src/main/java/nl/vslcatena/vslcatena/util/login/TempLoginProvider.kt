package nl.vslcatena.vslcatena.util.login

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.models.User
import nl.vslcatena.vslcatena.util.Result
import nl.vslcatena.vslcatena.util.extensions.await

//TODO remove when actual login server is set up.
class TempLoginProvider : LoginProvider {

    override val currentUser = MutableLiveData<User?>()

    override suspend fun authenticate(username: String, password: String): Result<User> {
        delay(2000)
        if (username != "test" || password != "test")
            return Result.Failure(Exception("Invalid credentials"))

        val userId = "15-215"

        val userInfo = FirebaseFirestore.getInstance()
            .document("users/$userId")
            .get().await()

        if (userInfo?.data == null) {
            return Result.Failure(Exception("Something went wrong"))
        }

        val newUser =
            User(Identifier(userId), userInfo.getString("name")!!, userInfo.getString("role")!!)

        currentUser.value = newUser

        return Result.Success(newUser)
    }

}