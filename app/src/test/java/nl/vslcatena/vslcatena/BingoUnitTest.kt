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
        val game = BingoGame(5, Array(50){ "test: $it" }.toList()).apply {
            toggleField(0,0)
            toggleField(1,0)
            toggleField(2,0)
            toggleField(3,0)
            toggleField(4,0)
        }
        val winningTiles = game.getWinningCells()

        assertEquals(5, winningTiles.size)
    }

    @Test
    fun `check horizontal win 2`() {
        val game = BingoGame(5, Array(50){ "test: $it" }.toList()).apply {
            toggleField(0,0)
            toggleField(1,0)
            toggleField(2,0)
            toggleField(3,0)
//            toggleField(4,0)
        }
        val winningTiles = game.getWinningCells()

        assertEquals(0, winningTiles.size)
    }

    @Test
    fun `check horizontal win 3`() {
        val game = BingoGame(5, Array(50){ "test: $it" }.toList()).apply {
            toggleField(0,0)
            toggleField(1,0)
            toggleField(2,0)
            toggleField(3,0)
            toggleField(4,0)

            toggleField(0,3)
            toggleField(1,3)
            toggleField(2,3)
            toggleField(3,3)
            toggleField(4,3)
        }
        val winningTiles = game.getWinningCells()

        assertEquals(10, winningTiles.size)
    }

    @Test
    fun `check vertical win 1`() {
        val game = BingoGame(5, Array(50){ "test: $it" }.toList()).apply {
            toggleField(0,0)
            toggleField(0,1)
            toggleField(0,2)
            toggleField(0,3)
            toggleField(0,4)
        }
        val winningTiles = game.getWinningCells()

        assertEquals(5, winningTiles.size)
    }
    @Test
    fun `check vertical win 2`() {
        val game = BingoGame(5, Array(50){ "test: $it" }.toList()).apply {
            toggleField(0,0)
            toggleField(0,1)
            toggleField(0,2)
            toggleField(1,3)
            toggleField(0,4)
        }
        val winningTiles = game.getWinningCells()

        assertEquals(0, winningTiles.size)
    }

    @Test
    fun `check vertical win 3`() {
        val game = BingoGame(5, Array(50){ "test: $it" }.toList()).apply {
            toggleField(0,0)
            toggleField(0,1)
            toggleField(0,2)
            toggleField(0,3)
            toggleField(0,4)

            toggleField(4,0)
            toggleField(4,1)
            toggleField(4,2)
            toggleField(4,3)
            toggleField(4,4)
        }
        val winningTiles = game.getWinningCells()

        assertEquals(10, winningTiles.size)
    }

    @Test
    fun `check diagonal win 1`() {
        val game = BingoGame(5, Array(50){ "test: $it" }.toList()).apply {
            toggleField(0,0)
            toggleField(1,1)
            toggleField(2,2)
            toggleField(3,3)
            toggleField(4,4)

        }
        val winningTiles = game.getWinningCells()

        assertEquals(5, winningTiles.size)
    }

    @Test
    fun `check diagonal win 2`() {
        val game = BingoGame(5, Array(50){ "test: $it" }.toList()).apply {
            toggleField(0,4)
            toggleField(1,3)
            toggleField(2,2)
            toggleField(3,1)
            toggleField(4,0)

        }
        val winningTiles = game.getWinningCells()

        assertEquals(5, winningTiles.size)
    }
    @Test
    fun `check diagonal win 3`() {
        val game = BingoGame(5, Array(50){ "test: $it" }.toList()).apply {
            toggleField(0,0)
            toggleField(1,1)
//            toggleField(2,2)
            toggleField(3,3)
            toggleField(4,4)


            toggleField(0,4)
            toggleField(1,3)
//            toggleField(2,2)
            toggleField(3,1)
            toggleField(4,0)

        }
        val winningTiles = game.getWinningCells()

        assertEquals(0, winningTiles.size)
    }


    @Test
    fun `check horizontal and vertical win 1`() {
        val game = BingoGame(5, Array(50){ "test: $it" }.toList()).apply {
            toggleField(0,0)
            toggleField(0,1)
            toggleField(0,2)
            toggleField(0,3)
            toggleField(0,4)

//            toggleField(0,0) // This has already been toggled
            toggleField(1,0)
            toggleField(2,0)
            toggleField(3,0)
            toggleField(4,0)
        }
        val winningTiles = game.getWinningCells()

        assertEquals(9, winningTiles.size)
    }

    @Test
    fun `check horizontal, vertical and diagonal win 1`() {
        val game = BingoGame(5, Array(50){ "test: $it" }.toList()).apply {
            toggleField(0,0)
            toggleField(0,1)
            toggleField(0,2)
            toggleField(0,3)
            toggleField(0,4)

            toggleField(1,0)
            toggleField(2,0)
            toggleField(3,0)
            toggleField(4,0)

            toggleField(1,3)
            toggleField(2,2)
            toggleField(3,1)
        }
        val winningTiles = game.getWinningCells()

        assertEquals(12, winningTiles.size)
    }
}
