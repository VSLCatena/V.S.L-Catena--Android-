package nl.vslcatena.vslcatena.controllers


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.abstraction.fragment.NeedsAuthentication
import nl.vslcatena.vslcatena.abstraction.fragment.SingleItemFragment
import nl.vslcatena.vslcatena.models.News

/**
 * Fragment for showing a single newsItem.
 *
 */
@NeedsAuthentication
class NewsItemFragment : SingleItemFragment<News>(News::class.java) {
    override fun onItemRetrieved(item: News) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_item, container, false)
    }


}
