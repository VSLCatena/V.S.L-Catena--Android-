package nl.vslcatena.vslcatena.modules.promo

import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataReference
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.util.componentholders.PostHeaderViewHolder
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
    override val id: Identifier,
    val imageRef: String,
    val adventureId: Int,
    val title: String,
    val content: String,
    val user: Identifier,
    val date: Date,
    val userLastEdited: Identifier = user,
    val dateLastEdited: Date = date

) : BaseModel, PostHeaderViewHolder.PostHeaderProvider {
    constructor() : this(Identifier(""), "", -1, "", "", Identifier(""), Date())

    override fun datePosted() = date
    override fun userPostingId() = user
    override fun lastEditedUserId(): Identifier = userLastEdited
    override fun lastEditedDate(): Date = dateLastEdited
}