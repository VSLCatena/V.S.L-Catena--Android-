package nl.vslcatena.vslcatena.util.login

import com.google.firebase.auth.FirebaseAuth
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.models.User
import nl.vslcatena.vslcatena.util.LiveDataWrapper
import nl.vslcatena.vslcatena.util.Result
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.extensions.await
import nl.vslcatena.vslcatena.util.extensions.awaitFirstObservation
import org.json.JSONObject

object UserProvider {

    val currentUser = LiveDataWrapper<User>()

    fun getUser() = currentUser.value

    suspend fun authenticate(username: String, password: String): Result<User> {

        // Do request here
        val requestResult = """
{
    "token": "eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL2lkZW50aXR5dG9vbGtpdC5nb29nbGVhcGlzLmNvbS9nb29nbGUuaWRlbnRpdHkuaWRlbnRpdHl0b29sa2l0LnYxLklkZW50aXR5VG9vbGtpdCIsImV4cCI6MTU0ODgwOTc0NCwiaWF0IjoxNTQ4ODA2MTQ0LCJpc3MiOiJmaXJlYmFzZS1hZG1pbnNkay1sNTMybkB2c2wtY2F0ZW5hLmlhbS5nc2VydmljZWFjY291bnQuY29tIiwic3ViIjoiZmlyZWJhc2UtYWRtaW5zZGstbDUzMm5AdnNsLWNhdGVuYS5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsInVpZCI6IjUwLTAwMyJ9.f7YP4nEqpjuiyVgb3e2pyUCFcIXw_1GGPFsVU84lMC6ZqobFYcDMXgxj8PFtSrK-EUtS3j3t6KqeCFNhtuUfKyYrTaJlZs2QcVH9Ly0bgjyxOps2BBPHCHfx5lZpshjT5Uu-YBbpeUtJ9GR1Rrhgg5qCswapqskylWGJN9azWm-3pRMKBuJOxULJ3pnFWbIdebaUO0NpZCVgb_Mui70h7RY9j5HQmHwCrQF644WgyU-Irkg37d4CBeH909ijx_7iSHIOC8P1LwpQ3GlaWPUD4DUcx7qA4Zv5t-lrC--zhnHZMFOOYb5-slcJkcSticuOEsaBFeG2ESVj-6CmS1Q2DA",
    "user": {
        "id": "50-003",
        "name": "test_Aafke Ruiter",
        "phoneNumber": "0683967742",
        "email": "aruiter@test.vslcatena.lan",
        "committees": [
            {
                "id": "flitscie",
                "name": "flitscie",
                "dn": "CN=flitscie,OU=User Groups,OU=Groups,OU=vslcatena.lan,DC=vslcatena,DC=lan"
            }
        ]
    }
}
        """
        val jsonRequest = JSONObject(requestResult)
        val firebaseToken = jsonRequest.getString("token")
        FirebaseAuth.getInstance().signInWithCustomToken(firebaseToken).await()

        return doLoginFromFirebase()
    }

    suspend fun doLoginFromFirebase(): Result<User> {
        val userId = FirebaseAuth.getInstance().uid
            ?: return Result.Failure(Exception("Something went wrong"))

        currentUser.wrap(DataCreator.getSingleReference(User::class.java, Identifier(userId)))

        return currentUser.awaitFirstObservation()?.let {
            Result.Success(it)
        } ?: return Result.Failure(Exception("Something went wrong"))
    }
}