package nl.vslcatena.vslcatena.models

import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataReference
import nl.vslcatena.vslcatena.util.Paths

@DataReference("users", "users/%s")
data class User(
    override val id: Identifier,
    val name: String,
    private val role: Long
) : BaseModel {

    fun role() = Role.getRoleFromLevel(role)

    fun hasClearance(role: Role) = role().hasClearance(role)

    constructor() : this(Identifier(""), "", 0)

    fun getThumbnailRef() = Paths.getStorageThumbnailPath(id)

}