package nl.vslcatena.vslcatena.models

import nl.vslcatena.vslcatena.firebase.BaseModel
import nl.vslcatena.vslcatena.firebase.FirebaseReference
/**
 * Model item for the ALV bingo.
 * @param id The id of the item
 * @param content The situation (described with text) that needs to happen to cross the item of a bingo list
 */
@FirebaseReference("")
data class BingoItem(val id: String,
                     val content: String): BaseModel {
    constructor(): this("", "")
}