package nl.vslcatena.vslcatena.models

import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataReference
import nl.vslcatena.vslcatena.util.Paths

@DataReference("users", "users/%s")
data class User(
    override val id: Identifier,
    val name: String,
    private val role: String
) : BaseModel {

    fun role() = Role.getRoleFromString(role)

    constructor() : this(Identifier(""), "", "")

    fun getThumbnailRef() = Paths.getStorageThumbnailPath(id)

}