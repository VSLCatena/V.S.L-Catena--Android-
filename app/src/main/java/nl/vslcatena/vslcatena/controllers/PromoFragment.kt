package nl.vslcatena.vslcatena.controllers

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.abstraction.firebase.ItemLoader
import nl.vslcatena.vslcatena.abstraction.lists.PagedFirebaseListFragment
import nl.vslcatena.vslcatena.models.PromoItem
import nl.vslcatena.vslcatena.models.User
import nl.vslcatena.vslcatena.util.GlideApp
import nl.vslcatena.vslcatena.util.compenentHolders.PostHeaderViewHolder
import java.text.DateFormat.getDateInstance
import java.util.*
import nl.vslcatena.vslcatena.util.extensions.observeOnce

class PromoFragment : PagedFirebaseListFragment<PromoItem, PromoFragment.PromoViewHolder>(PromoItem::class.java) {
    override val itemView = R.layout.promo_item
    private lateinit var userLoader : ItemLoader<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userLoader = ItemLoader.of(this, User::class.java)
    }


    override fun onListItemClicked(item: PromoItem) {
        Toast.makeText(context, "Clicked on ${item.metaData.title} with id: ${item.id}", Toast.LENGTH_SHORT).show()
    }

    override fun createViewHolder(view: View) = PromoViewHolder(view)

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int, item: PromoItem) {
        with(holder.headerViewHolder){
            userLoader.getItem(item.metaData.postedBy)?.let {
                it.observeOnce(Observer {
                    it?.let{
                        mUserNameView.text = it.name
                        GlideApp.with(this@PromoFragment)
                                .load(FirebaseStorage.getInstance().getReference(it.getThumbnailRef()).apply { println("path is $path") })
                                .into(mThumbnailView)
                    }
                })
            }
            mDateView.text = getDateInstance().format(Date(item.metaData.postedTime))
        }


        holder.mContentView.text = item.metaData.content
        holder.mTitleView.text = item.metaData.title
    }


    inner class PromoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerViewHolder = PostHeaderViewHolder(view)
        val mTitleView: TextView = itemView.findViewById(R.id.title)
        val mContentView: TextView = itemView.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }

}


