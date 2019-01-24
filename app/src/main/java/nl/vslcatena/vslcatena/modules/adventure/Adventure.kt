package nl.vslcatena.vslcatena.modules.adventure

import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataReference
import nl.vslcatena.vslcatena.models.Identifier

@DataReference("adventure")
data class Adventure(
    override val id: Identifier
) : BaseModel {
    constructor() : this(Identifier(""))

}
