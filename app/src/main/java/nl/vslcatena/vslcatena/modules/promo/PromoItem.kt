package nl.vslcatena.vslcatena.modules.promo

import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataReference
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.util.componentholders.PostHeaderViewHolder
import nl.vslcatena.vslcatena.util.login.UserProvider
import java.util.*

/**
 * Model item for promo
 * @param id The id of the Item
 * @param title Title of the promo
 * @param content The promo text
 * @param imageRef Reference to the storage location of the image that needs to be shown
 * @param tags The tags that are connected to the promo item
 * @param adventureId The id referring to the adventure (activity) that the promo is for.
 */
@DataReference("promo", "promo/%s")
data class PromoItem(
    override val id: Identifier?,
    var title: String,
    var content: String,
    val user: Identifier = UserProvider.getUser()!!.id,
    val date: Date = Date(),
    var imageRef: String? = null,
    val adventureId: Int? = null,
    override var userLastEdited: Identifier? = user,
    override var dateLastEdited: Date? = date

) : BaseModel, PostHeaderViewHolder.PostHeaderProvider {
    constructor() : this(Identifier(""), "", "")

    override fun datePosted() = date
    override fun userPostingId() = user
    override fun lastEditedUserId(): Identifier? = userLastEdited
    override fun lastEditedDate(): Date? = dateLastEdited
}