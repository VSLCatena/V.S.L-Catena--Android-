package nl.vslcatena.vslcatena.modules.bingo


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_bingo.*
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.abstraction.fragment.BaseFragment
import nl.vslcatena.vslcatena.abstraction.fragment.NeedsAuthentication

/**
 * A simple [Fragment] subclass.
 *
 */

@NeedsAuthentication
class BingoFragment : BaseFragment() {
    private val bingoGame = BingoGame(boardSize, Array(50) { "test: $it" }.toList())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_bingo, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bingoboard.apply {
            layoutManager = GridLayoutManager(context, boardSize)
            adapter = BingoAdapter()
        }
    }

    inner class BingoAdapter: RecyclerView.Adapter<BingoAdapter.BingoFieldViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BingoFieldViewHolder =
                BingoFieldViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bingo_field, parent, false))

        override fun getItemCount() = bingoGame.cellText.size


        override fun onBindViewHolder(holder: BingoFieldViewHolder, position: Int) {
            holder.title.text = bingoGame.cellText[position]

            holder.itemView.setOnClickListener {
                bingoGame.marked[position] = !bingoGame.marked[position]

                it.setBackgroundColor(if(bingoGame.marked[position]) Color.GREEN else Color.WHITE)

                if (bingoGame.hasWon())
                    Toast.makeText(context, "Bingo Won", Toast.LENGTH_SHORT).show()
            }
        }

        inner class BingoFieldViewHolder(view: View): RecyclerView.ViewHolder(view){
            val title: TextView = view.findViewById(R.id.bingo_item_title)
        }
    }

    companion object {
        const val boardSize = 5
    }
}
