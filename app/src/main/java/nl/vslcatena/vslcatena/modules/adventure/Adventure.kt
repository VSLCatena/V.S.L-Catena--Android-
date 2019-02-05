package nl.vslcatena.vslcatena.modules.adventure

import com.google.firebase.Timestamp
import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataReference
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.util.login.UserProvider
import java.util.*

@DataReference("adventure")
data class Adventure(
    override val id: Identifier,
    override var userLastEdited: Identifier? = UserProvider.getUser()?.id,
    override var dateLastEdited: Timestamp? = Timestamp(Date())
) : BaseModel {
    constructor() : this(Identifier(""))

}
