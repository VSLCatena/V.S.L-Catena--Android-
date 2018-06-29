package nl.vslcatena.vslcatena.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.promo_item.view.*

class PromoViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    val mTitleView: TextView = mView.title
    val mContentView: TextView = mView.content


    override fun toString(): String {
        return super.toString() + " '" + mContentView.text + "'"
    }
}