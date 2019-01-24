package nl.vslcatena.vslcatena.util

import nl.vslcatena.vslcatena.models.Identifier

object Paths {

    //Users
    fun getStorageThumbnailPath(userId: Identifier) = "thumbnails/${userId.value}.jpg"

    //Magazines
    fun getStorageMagazineCoverPath(magazineId: Identifier) =
        "magazines/cover/${magazineId}_cover.jpg"

    fun getStorageMagazinePdfPath(magazineId: Identifier) = "magazines/pdf/$magazineId.pdf"
}