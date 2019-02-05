package nl.vslcatena.vslcatena.modules.magazine

import com.google.firebase.Timestamp
import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataReference
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.util.Paths
import nl.vslcatena.vslcatena.util.login.UserProvider
import java.util.*

@DataReference("magazines")
data class Magazine(
    override val id: Identifier,
    val title: String,
    val publish_date: Timestamp,
    override var userLastEdited: Identifier? = UserProvider.getUser()?.id,
    override var dateLastEdited: Timestamp? = Timestamp(Date())
) : BaseModel {
    constructor() : this(Identifier(""), "", Timestamp(Date()))

    fun getCoverPictureRef() = Paths.getStorageMagazineCoverPath(id)
    fun getPdfRef() = Paths.getStorageMagazinePdfPath(id)
}