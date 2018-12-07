package nl.vslcatena.vslcatena.models

import nl.vslcatena.vslcatena.abstraction.firebase.BaseModel
import nl.vslcatena.vslcatena.abstraction.firebase.DataReference
import nl.vslcatena.vslcatena.util.Paths

@DataReference("users")
data class User(
        override var id: String,
        var name: String
) : BaseModel {


    constructor(id: String): this(id, "")
    constructor(): this("")

    fun getThumbnailRef() = Paths.getStorageThumbnailPath(id)

}