package nl.vslcatena.vslcatena.modules.news

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataReference
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.util.componentholders.PostHeaderViewHolder
import nl.vslcatena.vslcatena.util.login.UserProvider
import java.util.*

/**
 * Created by Thomas van den Bulk on 14-5-2018.
 */
@DataReference("news", "news/%s")
data class News(
    @get:Exclude
    @field:Exclude
    override val id: Identifier?,
    var title: String,
    var content: String,
    val user: Identifier = UserProvider.getUser()!!.id,
    val date: Timestamp = Timestamp(Date()),
    override var userLastEdited: Identifier? = user,
    override var dateLastEdited: Timestamp? = date
) : BaseModel, PostHeaderViewHolder.PostHeaderProvider {
    constructor() : this(null, "", "", Identifier(""), Timestamp(Date()))

    override fun userPostingId() = user
    override fun datePosted() = date
    override fun lastEditedUserId() = userLastEdited
    override fun lastEditedDate() = dateLastEdited
}