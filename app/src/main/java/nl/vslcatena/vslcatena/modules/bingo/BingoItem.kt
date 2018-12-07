package nl.vslcatena.vslcatena.modules.bingo

import nl.vslcatena.vslcatena.abstraction.firebase.BaseModel
import nl.vslcatena.vslcatena.abstraction.firebase.DataReference
/**
 * Model item for the ALV bingo.
 * @param id The id of the item
 * @param content The situation (described with text) that needs to happen to cross the item of a bingo list
 */
@DataReference("")
data class BingoItem(override val id: String,
                     val content: String): BaseModel {
    constructor(): this("", "")
}