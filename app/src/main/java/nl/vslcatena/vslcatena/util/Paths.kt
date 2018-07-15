package nl.vslcatena.vslcatena.util

object Paths{

    //Users
    fun getStorageThumbnailPath(userId: String) = "thumbnails/$userId.jpg"

    //Magazines
    fun getStorageMagazineCoverPath(magazineId: String) = "magazines/cover/${magazineId}_cover.jpg"
    fun getStorageMagazinePdfPath(magazineId: String) = "magazines/pdf/$magazineId.pdf"
}