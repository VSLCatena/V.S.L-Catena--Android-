package nl.vslcatena.vslcatena.modules.news

import nl.vslcatena.vslcatena.abstraction.firebase.BaseModel
import nl.vslcatena.vslcatena.abstraction.firebase.DataReference

/**
 * Created by Thomas van den Bulk on 14-5-2018.
 */
@DataReference("list", "list/%s")
data class News(override val id: String,
                val title: String,
                val content: String) : BaseModel {
    constructor(): this("", "", "")
}