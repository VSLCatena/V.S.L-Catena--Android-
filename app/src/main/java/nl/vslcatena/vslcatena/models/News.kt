package nl.vslcatena.vslcatena.models

import nl.vslcatena.vslcatena.firebase.BaseModel
import nl.vslcatena.vslcatena.firebase.FirebaseReference

/**
 * Created by Thomas van den Bulk on 14-5-2018.
 */
@FirebaseReference("list")
data class News(override val id: String,
                val title: String,
                val content: String) : BaseModel {
    constructor(): this("", "", "")
}