package nl.vslcatena.vslcatena.modules.adventure


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.abstraction.fragment.SingleItemFragment

/**
 * Fragment for showing a single adventure item.
 */
class AdventureItemFragment : SingleItemFragment<Adventure>(
    Adventure::class.java) {
    override fun onItemRetrieved(item: Adventure) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adventure_item, container, false)
    }


}
