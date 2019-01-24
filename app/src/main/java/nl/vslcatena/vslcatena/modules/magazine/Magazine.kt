package nl.vslcatena.vslcatena.modules.magazine

import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataReference
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.util.Paths
import java.util.*

@DataReference("magazines")
data class Magazine(
    override val id: Identifier,
    val title: String,
    val publish_date: Date
) : BaseModel {
    constructor() : this(Identifier(""), "", Date())

    fun getCoverPictureRef() = Paths.getStorageMagazineCoverPath(id)
    fun getPdfRef() = Paths.getStorageMagazinePdfPath(id)
}