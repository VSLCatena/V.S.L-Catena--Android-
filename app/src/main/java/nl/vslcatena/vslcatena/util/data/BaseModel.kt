package nl.vslcatena.vslcatena.util.data

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import nl.vslcatena.vslcatena.models.Identifier
import java.io.Serializable

/**
 * Created by Thomas van den Bulk on 30-4-2018.
 */
@IgnoreExtraProperties
interface BaseModel : Serializable {
    // We are required to have an id for the paging module
    @get:Exclude
    val id: Identifier?
}