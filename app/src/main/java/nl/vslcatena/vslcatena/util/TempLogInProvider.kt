package nl.vslcatena.vslcatena.util

//TODO remove when actual login server is set up.
object TempLogInProvider {

    var isAuthenticated = false

    fun authenticate(username: String, password: String): Boolean {
        if (username =="test" && password == "test")
            isAuthenticated = true

        return isAuthenticated
    }

}