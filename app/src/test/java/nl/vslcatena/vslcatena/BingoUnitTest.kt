package nl.vslcatena.vslcatena

import nl.vslcatena.vslcatena.util.BingoGame
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BingoUnitTest {

    @Test
    fun `check horizontal win 1`() {
        val game = BingoGame(5).apply {
            markField(0,0)
            markField(1,0)
            markField(2,0)
            markField(3,0)
            markField(4,0)
        }
        val winningTiles = game.checkIfWon()

        assertEquals(5, winningTiles.size)
    }

    @Test
    fun `check horizontal win 2`() {
        val game = BingoGame(5).apply {
            markField(0,0)
            markField(1,0)
            markField(2,0)
            markField(3,0)
//            markField(4,0)
        }
        val winningTiles = game.checkIfWon()

        assertEquals(0, winningTiles.size)
    }

    @Test
    fun `check horizontal win 3`() {
        val game = BingoGame(5).apply {
            markField(0,0)
            markField(1,0)
            markField(2,0)
            markField(3,0)
            markField(4,0)

            markField(0,3)
            markField(1,3)
            markField(2,3)
            markField(3,3)
            markField(4,3)
        }
        val winningTiles = game.checkIfWon()

        assertEquals(10, winningTiles.size)
    }

    @Test
    fun `check vertical win 1`() {
        val game = BingoGame(5).apply {
            markField(0,0)
            markField(0,1)
            markField(0,2)
            markField(0,3)
            markField(0,4)
        }
        val winningTiles = game.checkIfWon()

        assertEquals(5, winningTiles.size)
    }
    @Test
    fun `check vertical win 2`() {
        val game = BingoGame(5).apply {
            markField(0,0)
            markField(0,1)
            markField(0,2)
            markField(1,3)
            markField(0,4)
        }
        val winningTiles = game.checkIfWon()

        assertEquals(0, winningTiles.size)
    }

    @Test
    fun `check vertical win 3`() {
        val game = BingoGame(5).apply {
            markField(0,0)
            markField(0,1)
            markField(0,2)
            markField(0,3)
            markField(0,4)

            markField(4,0)
            markField(4,1)
            markField(4,2)
            markField(4,3)
            markField(4,4)
        }
        val winningTiles = game.checkIfWon()

        assertEquals(10, winningTiles.size)
    }

    @Test
    fun `check diagonal win 1`() {
        val game = BingoGame(5).apply {
            markField(0,0)
            markField(1,1)
            markField(2,2)
            markField(3,3)
            markField(4,4)

        }
        val winningTiles = game.checkIfWon()

        assertEquals(5, winningTiles.size)
    }

    @Test
    fun `check diagonal win 2`() {
        val game = BingoGame(5).apply {
            markField(0,4)
            markField(1,3)
            markField(2,2)
            markField(3,1)
            markField(4,0)

        }
        val winningTiles = game.checkIfWon()

        assertEquals(5, winningTiles.size)
    }
    @Test
    fun `check diagonal win 3`() {
        val game = BingoGame(5).apply {
            markField(0,0)
            markField(1,1)
//            markField(2,2)
            markField(3,3)
            markField(4,4)


            markField(0,4)
            markField(1,3)
//            markField(2,2)
            markField(3,1)
            markField(4,0)

        }
        val winningTiles = game.checkIfWon()

        assertEquals(0, winningTiles.size)
    }


    @Test
    fun `check horizontal and vertical win 1`() {
        val game = BingoGame(5).apply {
            markField(0,0)
            markField(0,1)
            markField(0,2)
            markField(0,3)
            markField(0,4)

            markField(0,0)
            markField(1,0)
            markField(2,0)
            markField(3,0)
            markField(4,0)
        }
        val winningTiles = game.checkIfWon()

        assertEquals(9, winningTiles.size)
    }

    @Test
    fun `check horizontal, vertical and diagonal win 1`() {
        val game = BingoGame(5).apply {
            markField(0,0)
            markField(0,1)
            markField(0,2)
            markField(0,3)
            markField(0,4)

            markField(0,0)
            markField(1,0)
            markField(2,0)
            markField(3,0)
            markField(4,0)

            markField(0,4)
            markField(1,3)
            markField(2,2)
            markField(3,1)
            markField(4,0)
        }
        val winningTiles = game.checkIfWon()

        assertEquals(12, winningTiles.size)
    }
}
