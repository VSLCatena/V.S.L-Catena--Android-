package nl.vslcatena.vslcatena.modules.bingo

import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataReference
import nl.vslcatena.vslcatena.models.Identifier

/**
 * Model item for the ALV bingo.
 * @param id The id of the item
 * @param content The situation (described with text) that needs to happen to cross the item of a bingo list
 */
@DataReference("")
data class BingoItem(
    override val id: Identifier,
    val content: String
) : BaseModel {
    constructor() : this(Identifier(""), "")
}