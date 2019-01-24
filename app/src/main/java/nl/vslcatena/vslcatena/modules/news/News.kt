package nl.vslcatena.vslcatena.modules.news

import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataReference
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.util.componentholders.PostHeaderViewHolder
import java.util.*

/**
 * Created by Thomas van den Bulk on 14-5-2018.
 */
@DataReference("news", "news/%s")
data class News(
    override val id: Identifier,
    val title: String,
    val content: String,
    val user: Identifier,
    val date: Date
) : BaseModel, PostHeaderViewHolder.PostHeaderProvider {
    constructor() : this(Identifier(""), "", "", Identifier(""), Date())

    override fun getUserPostingId() = user
    override fun getDatePosted() = date
}