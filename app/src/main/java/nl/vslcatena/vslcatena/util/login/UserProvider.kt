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
    "token": "eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL2lkZW50aXR5dG9vbGtpdC5nb29nbGVhcGlzLmNvbS9nb29nbGUuaWRlbnRpdHkuaWRlbnRpdHl0b29sa2l0LnYxLklkZW50aXR5VG9vbGtpdCIsImV4cCI6MTU1MDAxMDcxMywiaWF0IjoxNTUwMDA3MTEzLCJpc3MiOiJmaXJlYmFzZS1hZG1pbnNkay1sNTMybkB2c2wtY2F0ZW5hLmlhbS5nc2VydmljZWFjY291bnQuY29tIiwic3ViIjoiZmlyZWJhc2UtYWRtaW5zZGstbDUzMm5AdnNsLWNhdGVuYS5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsInVpZCI6IjUwLTAwMyJ9.mjYJmL6e3c1ErsPUCBVP75jtzwXkv-MCnrTzINmj8NEU273I2DITOOkL0I5v6nNJSQH4ev05_gsqABg1FchQ2V1xH_g-m92nNdASrtiLuuuT91WsM82e50flMCIyJ_WhB29XgKT97f7Nk2OYwc5MQLGM0QMUHEEYRkHGHhtNoCN6rJMtGc0BSIMtIuiJibm1zgh5sypEvia15xuRI-DdoKssD5wxjo9m_89_1c5Xu-HjxMwvZM2Z0O0NipufWyi3tSv2u2f9ngarr8ywzwZOHRB1xRqx7PIWbx6yLEVxLfaU9I7BHvPfcE-i40uTraM98a1jeL3eJCkIqiC0LXVcsw",
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