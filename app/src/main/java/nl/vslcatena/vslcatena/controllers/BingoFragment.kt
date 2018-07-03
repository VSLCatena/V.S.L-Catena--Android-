package nl.vslcatena.vslcatena.controllers


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.lists.OnListItemClickedListener
import nl.vslcatena.vslcatena.lists.normal.ListFragmentRecyclerViewAdapter
import nl.vslcatena.vslcatena.models.BingoItem
import nl.vslcatena.vslcatena.util.BingoGame
import nl.vslcatena.vslcatena.util.BingoGame.BingoField
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class BingoFragment : Fragment() {


    private val boardSize = 5
    private val bingoGame = BingoGame(boardSize)
    private val mItems = bingoGame.getFieldsAsList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bingo, container, false);
        val recyclerView = view.findViewById<RecyclerView>(R.id.bingoBoardRecyclerView)

        applyBingoItems(tempList)

        recyclerView.layoutManager = GridLayoutManager(context, boardSize)

        recyclerView.adapter = object: RecyclerView.Adapter<BingoFieldViewHolder>(){
            val mItems = bingoGame.getFieldsAsList()

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BingoFieldViewHolder =
                    BingoFieldViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bingo_field, parent, false))

            override fun getItemCount() = mItems.size


            override fun onBindViewHolder(holder: BingoFieldViewHolder, position: Int) {
                holder.itemView.setOnClickListener {
                    val item = mItems[position]
                    item.marked = !item.marked

                    if (item.marked)
                        it.background = ColorDrawable(Color.GREEN)
                    else
                        it.background = null

                    if (bingoGame.checkIfWon().isNotEmpty())
                        Toast.makeText(context, "Bingo Won", Toast.LENGTH_SHORT).show()
                }
            }

        }
        return view
    }

    private fun applyBingoItems(list: List<BingoItem>){
        val mutableList = list.toMutableList()
        mItems.forEach {
            if(mutableList.size>0)
                it.content = mutableList.removeAt(Random().nextInt(mutableList.size)).content
            else
                Log.d("BingoFragment", "Not enough bingoitems given")
        }
    }


    class BingoFieldViewHolder(view: View): RecyclerView.ViewHolder(view){

    }



    private val tempList = arrayListOf(
            BingoItem("1", "Thomas die kut doet"),
            BingoItem("2", "Martie die oud doet"),
            BingoItem("3", "HARDER"),
            BingoItem("4", "SNELLER"),
            BingoItem("5", "DIEPER"),
            BingoItem("6", "DIEPER"),
            BingoItem("7", "\"Nooit meer alcohol\"")

    )
}
