package nl.vslcatena.vslcatena.controllers

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.promo_item.view.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.lists.PagedFirebaseListFragment
import nl.vslcatena.vslcatena.lists.normal.FirebaseListFragment
import nl.vslcatena.vslcatena.models.PromoItem
import nl.vslcatena.vslcatena.lists.normal.ListFragmentRecyclerViewAdapter

class PromoFragment : PagedFirebaseListFragment<PromoItem, PromoFragment.PromoViewHolder>(PromoItem::class.java) {
    override val itemView = R.layout.promo_item

    override fun onListItemClicked(item: PromoItem) {
        Toast.makeText(context, "Clicked on ${item.title} with id: ${item.id}", Toast.LENGTH_SHORT).show()
    }

    override fun createViewHolder(view: View) = PromoViewHolder(view)

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int, item: PromoItem) {
        holder.mContentView.text = item.content
        holder.mTitleView.text = item.title
    }

    inner class PromoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTitleView: TextView = itemView.findViewById(R.id.title)
        val mContentView: TextView = itemView.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }

}