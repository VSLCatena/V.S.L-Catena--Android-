package nl.vslcatena.vslcatena.modules.magazine

import nl.vslcatena.vslcatena.abstraction.firebase.BaseModel
import nl.vslcatena.vslcatena.abstraction.firebase.DataReference
import nl.vslcatena.vslcatena.util.Paths

@DataReference("magazines")
data class Magazine(
        override val id: String,
        val name: String,
        val publishDate: Long
) : BaseModel {
    constructor(): this("", "", -1L)
    fun getCoverPictureRef() = Paths.getStorageMagazineCoverPath(id)
    fun getPdfRef() = Paths.getStorageMagazinePdfPath(id)
}