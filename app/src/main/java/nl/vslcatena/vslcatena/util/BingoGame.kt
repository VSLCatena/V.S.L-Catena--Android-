package nl.vslcatena.vslcatena.util

/**
 * Bingo game instance used for the Bingo page. This is the backend of the bingo game, where the logics are defined.
 *
 * @param boardSize The length and width of the board
 * @param onGameWon Action that should be run when the game is won.
 *
 * When field is marked:
 *  Check for winconditions
 *      1. Horizontal
 *      2. Vertical
 *      3. Diagonal
 *     Return all winconditions
 */
class BingoGame(private val boardSize: Int, private val onGameWon: (winningFields: Set<BingoField>) -> Unit ={}){

    /**
     * A field of the bingo board
     * @param content String that identify when the player should mark this field.
     * @param marked True: field is marked, False: field is not marked.
     */
    data class BingoField(val content: String, var marked: Boolean)

    //The board. Board is a 2d array of BingoField objects.
    private val board = Array(boardSize) { y ->
        Array(boardSize){ x ->
            BingoField("$y-$x", false)
        }
    }

    //Marks (or unmarks) the field at the given x and y values. If the field gets marked, it checks if the game is won. If so the onGameWon action gets performed.
    fun markField(x: Int, y: Int, mark: Boolean = true){
        getField(x,y).marked = mark
        if (mark){
            val set = checkIfWon()
            if (set.isNotEmpty())
                onGameWon(set)
        }
    }

    //Toggles the mark of a field.
    fun toggleMark(x: Int, y: Int): Boolean {
        val newValue = !getField(x, y).marked
        markField(x, y, newValue)
        return newValue
    }

    //Gets a field for a given x and y.
    fun getField(x: Int, y: Int) = board[y][x]

    //Checks if the game is won. If so, all the tiles that result in the win get returned, so that something fancy can be done in the front end.
    fun checkIfWon(): Set<BingoField> {
        fun checkHorizontalWins(): Set<BingoField> {
            val winingFields = HashSet<BingoField>()
            board.forEach{ row ->
                if (row.all { it.marked })
                    winingFields.addAll(row)
            }
            return winingFields
        }
        fun checkVerticalWins(): Set<BingoField> {
            val winingFields = HashSet<BingoField>()
            for (x in 0 until boardSize){
                val column = ArrayList<BingoField>()
                for (y in 0 until boardSize){
                    column.add(board[y][x])
                }
                if(column.all { it.marked })
                    winingFields.addAll(column)
            }
            return winingFields
        }
        fun checkDiagonalWins(): Set<BingoField> {
            val winingFields = HashSet<BingoField>()
            val diag1 = ArrayList<BingoField>()
            val diag2 = ArrayList<BingoField>()

            for(i in 0 until boardSize){
                diag1.add(board[i][i])
                diag2.add(board[i][boardSize-1-i])
            }

            if(diag1.all { it.marked })
                winingFields.addAll(diag1)
            if(diag2.all { it.marked })
                winingFields.addAll(diag2)


            return winingFields
        }

        return checkHorizontalWins() + checkVerticalWins() + checkDiagonalWins()
    }

}