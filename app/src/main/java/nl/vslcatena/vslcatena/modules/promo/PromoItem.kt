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
 * //TODO maybe change content from string to a new Content class (or an array) that can contain text and references to images. If this happens imageRef can be removed.
 */
@DataReference("promo", "promo/%s")
data class PromoItem(
    override val id: Identifier,
    val imageRef: String,
    val adventureId: Int,
    val title: String,
    val content: String,
    val userId: Identifier,
    val date: Date

) : BaseModel, PostHeaderViewHolder.PostHeaderProvider {
    constructor() : this(Identifier(""), "", -1, "", "", Identifier(""), Date())

    override fun getDatePosted() = date
    override fun getUserPostingId() = userId
}