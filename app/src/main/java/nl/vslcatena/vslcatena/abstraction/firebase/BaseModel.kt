package nl.vslcatena.vslcatena.abstraction.firebase

import java.io.Serializable

/**
 * Created by Thomas van den Bulk on 30-4-2018.
 */
interface BaseModel : Serializable {
    // We are required to have an id for the paging module
    val id: String
}