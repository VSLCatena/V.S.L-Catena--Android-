package nl.vslcatena.vslcatena.util

import nl.vslcatena.vslcatena.models.Identifier

object Paths {

    //Users
    fun getStorageThumbnailPath(userId: Identifier) = "profile/${userId.value}/thumbnail.jpg"

    //Magazines
    fun getStorageMagazineCoverPath(magazineId: Identifier) =
        "magazines/cover/${magazineId.value}_cover.jpg"

    fun getStorageMagazinePdfPath(magazineId: Identifier) = "magazines/pdf/${magazineId.value}.pdf"
    fun getStoragePromoImage(userId: Identifier, id: Identifier, fileExtension: String) = "promo/${userId.value}/${id.value}.$fileExtension"


}