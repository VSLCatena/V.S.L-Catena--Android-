package nl.vslcatena.vslcatena.models

import com.google.firebase.Timestamp
import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataReference

@DataReference("users", "users/%s")
data class User(
    override val id: Identifier,
    val name: String,
    private val role: Long,
    override var userLastEdited: Identifier?,
    override var dateLastEdited: Timestamp?
) : BaseModel {

    fun role() = Role.getRoleFromLevel(role)

    fun hasClearance(role: Role) = role().hasClearance(role)

    constructor() : this(Identifier(""), "", 0, null, null)

    fun getProfileImage() = "users/${id.value}.jpg"

}