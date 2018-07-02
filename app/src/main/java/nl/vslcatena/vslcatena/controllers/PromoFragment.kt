package nl.vslcatena.vslcatena.controllers

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.promo_item.view.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.lists.normal.FirebaseListFragment
import nl.vslcatena.vslcatena.models.PromoItem
import nl.vslcatena.vslcatena.lists.normal.ListFragmentRecyclerViewAdapter

class PromoFragment : FirebaseListFragment<PromoItem, PromoFragment.PromoViewHolder>(PromoItem::class.java) {

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

    inner class PromoViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.title
        val mContentView: TextView = mView.content


        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }

}