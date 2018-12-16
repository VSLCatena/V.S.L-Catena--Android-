package nl.vslcatena.vslcatena.modules.login

import nl.vslcatena.vslcatena.abstraction.firebase.BaseModel
import nl.vslcatena.vslcatena.abstraction.firebase.DataReference
import nl.vslcatena.vslcatena.util.Paths

@DataReference("users")
data class User(
        override val id: String,
        val name: String,
        private val role: String
) : BaseModel {

    fun role() = Role.getRoleFromString(role)

    constructor(): this("", "", "")

    fun getThumbnailRef() = Paths.getStorageThumbnailPath(id)

}