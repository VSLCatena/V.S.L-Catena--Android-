package nl.vslcatena.vslcatena.modules.adventure

import nl.vslcatena.vslcatena.abstraction.firebase.BaseModel
import nl.vslcatena.vslcatena.abstraction.firebase.DataReference
import nl.vslcatena.vslcatena.models.PostMetaData

@DataReference("adventure")
data class Adventure (override val id: String,
                      val metaData: PostMetaData
): BaseModel{
    constructor() : this("", PostMetaData())

}
