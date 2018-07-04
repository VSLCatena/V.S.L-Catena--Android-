package nl.vslcatena.vslcatena.util

import java.util.Random
import kotlin.collections.HashSet

typealias BingoItem = String

/**
 * This is an ease of use class that handles all the BingoGame mechanics for us.
 *
 * @param size the gridsize of the bingo game you want to create
 * @param textList a list of all possible texts to fill the bingo with
 *
 * Make sure the {@link #textList} is bigger than the size squared! D:
 */
class BingoGame(val size: Int = 5, textList: List<BingoItem>) {
    /**
     * Contains a list of which text is at which position.
     * We keep this separated from {@link #marked} to speed up search time
     */
    val cellText: Array<BingoItem>

    /**
     * Contains a list of if which items are marked and which are not.
     * We keep this separated from {@link #cellText} to speed up search time
     */
    val marked: Array<Boolean>

    init {
        val mutableTextList = textList.toMutableList()
        cellText = Array(size*size){ mutableTextList.removeAt(Random().nextInt(mutableTextList.size)) }
        marked = Array(cellText.size){ false }
    }

    /**
     * Checks if a field is marked
     */
    fun isFieldMarked(x: Int, y: Int) = marked[x*size+y]

    /**
     * A convinience method to get the text of a certain field
     */
    fun getFieldText(x: Int, y: Int) = cellText[x*size+y]

    /**
     * A convinience method to toggle a certain field
     */
    fun toggleField(x: Int, y: Int){
        marked[x*size+y] = !marked[x*size+y]
    }

    /**
     * Receives which cells are part of a row/column/diagonal
     */
    fun getWinningCells() = getColumns() + getRows() + getDiagonals()

    /**
     * A convinience method to check if we won
     */
    fun hasWon() = getWinningCells().isNotEmpty()

    /**
     * Returns all the cells that are part of a diagonal, if any.
     *
     * if the size is an even number, this will always return an empty list
     * as there is no middle.
     */
    fun getDiagonals(): Set<Int> {
        val cellSet = HashSet<Int>()
        // If size is dividable in 2, then there is no middle, dummy!
        if(size.rem(2) == 0) return cellSet

        run next@{
            for(x in 0 until size) if(!marked[x*size+x]) return@next
            // If we did manage to win one we add it to the list
            for(x in 0 until size) cellSet.add(x*size+x)
        }

        run next@{
            for(x in 0 until size) if(!marked[x*size+size-x-1]) return@next
            // If we did manage to win one we add it to the list
            for(x in 0 until size) cellSet.add(x*size+size-x-1)
        }

        // At last we return a complete list with all the cells we found
        return cellSet
    }

    /**
     * Returns all the cells that are part of a column, if any.
     */
    fun getColumns(): Set<Int> {
        val cellSet = HashSet<Int>()
        next@for(x in 0 until size){
            // We go down the rows and if one is not checked we immediately continue to the next
            for(y in 0 until size) if(!marked[x+y*size]) continue@next

            // If we did manage to win one we add it to the list
            for(y in 0 until size) cellSet.add(x+y*size)
        }
        // At last we return a complete list with all the cells we found
        return cellSet
    }

    /**
     * Returns all the cells that are part of a row, if any.
     */
    fun getRows(): Set<Int> {
        val cellSet = HashSet<Int>()

        next@for(x in 0 until size){
            // We go down the column and if one is not checked we immediately continue to the next
            for(y in 0 until size) if(!marked[x*size+y]) continue@next

            // If we did manage to win one we add it to the list
            for(y in 0 until size) cellSet.add(x*size+y)
        }
        // At last we return a complete list with all the cells we found
        return cellSet
    }
}