package nl.vslcatena.vslcatena.controllers


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import nl.vslcatena.vslcatena.R

/**
 * A simple [Fragment] subclass.
 *
 */
class BingoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bingo, container, false)
    }

    fun createBingoGameView(){}



}
