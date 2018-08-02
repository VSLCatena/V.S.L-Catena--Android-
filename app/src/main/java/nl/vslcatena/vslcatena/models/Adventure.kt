package nl.vslcatena.vslcatena.models

import nl.vslcatena.vslcatena.abstraction.firebase.BaseModel
import nl.vslcatena.vslcatena.abstraction.firebase.FirebaseReference

@FirebaseReference("adventure")
data class Adventure (override val id: String,
                      val metaData: PostMetaData): BaseModel{
    constructor() : this("", PostMetaData())

}
