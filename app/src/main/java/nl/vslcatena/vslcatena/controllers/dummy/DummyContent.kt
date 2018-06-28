package nl.vslcatena.vslcatena.controllers.dummy

import nl.vslcatena.vslcatena.models.PromoItem
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<PromoItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, PromoItem> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createPromoItem(i))
        }
    }

    private fun addItem(item: PromoItem) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    private fun createPromoItem(position: Int): PromoItem {
        return PromoItem(position.toString(), "Item $position", makeDetails(position), "" ,  arrayOf(), "")
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

}
