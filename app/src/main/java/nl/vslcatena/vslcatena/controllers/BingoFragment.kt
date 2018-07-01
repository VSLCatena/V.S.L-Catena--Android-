package nl.vslcatena.vslcatena.controllers


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.BingoItem

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


    /**
     * When field is checked:
     *  Check for winconditions
     *      1. Horizontal
     *      2. Vertical
     *      3. Diagonal
     *     Return all winconditions
     */
    class BingoGame(boardSize: Int){
        data class BingoField(val content: String, val checked: Boolean)

        val board = Array<Array<BingoItem>>(boardSize) { y ->
            Array<BingoItem>(boardSize){x ->
                BingoItem("$y-$x", "$y-$x")
            }
        }

    }
}
