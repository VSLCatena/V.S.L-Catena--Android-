package nl.vslcatena.vslcatena.models

import nl.vslcatena.vslcatena.firebase.BaseModel
import nl.vslcatena.vslcatena.firebase.FirebaseReference
import nl.vslcatena.vslcatena.util.Paths

@FirebaseReference("magazines")
data class Magazine(
        override val id: String,
        val name: String,
        val publishDate: Long
) : BaseModel {
    constructor(): this("", "", -1L)
    fun getCoverPictureRef() = Paths.getStorageMagazineCoverPath(id)
    fun getPdfRef() = Paths.getStorageMagazinePdfPath(id)
}