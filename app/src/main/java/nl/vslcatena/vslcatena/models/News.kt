package nl.vslcatena.vslcatena.models

import nl.vslcatena.vslcatena.abstraction.firebase.BaseModel
import nl.vslcatena.vslcatena.abstraction.firebase.FirebaseReference

/**
 * Created by Thomas van den Bulk on 14-5-2018.
 */
@FirebaseReference("list", "list/%s")
data class News(override val id: String,
                val title: String,
                val content: String) : BaseModel {
    constructor(): this("", "", "")
}