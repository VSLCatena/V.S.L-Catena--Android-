package nl.vslcatena.vslcatena.controllers

import android.view.View
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.controllers.dummy.DummyContent
import nl.vslcatena.vslcatena.models.PromoItem
import nl.vslcatena.vslcatena.recyclerview.ListFragmentRecyclerViewAdapter
import nl.vslcatena.vslcatena.recyclerview.PromoViewHolder
//todo this must be a firebase list fragment.
class PromoFragment : ListFragment<PromoItem, PromoViewHolder>() {

    override var mItems: List<PromoItem> = DummyContent.ITEMS
    override fun createAdapter(): ListFragmentRecyclerViewAdapter<PromoItem, PromoViewHolder> {

        return object : ListFragmentRecyclerViewAdapter<PromoItem, PromoViewHolder>(mItems, null, R.layout.promo_item){

            override fun createViewHolder(view: View): PromoViewHolder {
                return PromoViewHolder(view)
            }

            override fun onBindViewHolder(holder: PromoViewHolder, item: PromoItem, position: Int) {
                super.onBindViewHolder(holder, item, position)
                holder.mContentView.text = item.content
                holder.mTitleView.text = item.title
            }

        }
    }
}