package nl.vslcatena.vslcatena

import nl.vslcatena.vslcatena.modules.bingo.BingoViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.rules.TestRule
import org.junit.Rule


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BingoUnitTest {

    lateinit var bingoViewModel: BingoViewModel

    @Rule
    @JvmField
    public var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun initTest() {
        bingoViewModel = BingoViewModel()
        bingoViewModel.initialize(Array(50) { "test $it" }.toList(), 5)
    }

    fun clear() {
        bingoViewModel.grid.forEach {
            it.forEach { cell ->
                cell.setChecked(false)
            }
        }
    }

    fun setCell(x: Int, y: Int, checked: Boolean = true) {
        bingoViewModel.grid[x][y].setChecked(checked)
    }

    fun hasWon() = bingoViewModel.won.value ?: false


    @Test
    fun `check horizontal win 1`() {
        bingoViewModel.grid.apply {
            clear()

            setCell(0, 0)
            setCell(1, 0)
            setCell(2, 0)
            setCell(3, 0)
            setCell(4, 0)

            assertEquals("Check horizontal win 1", hasWon(), true)
        }
    }

    @Test
    fun `check horizontal win 2`() {
        bingoViewModel.grid.apply {
            clear()

            setCell(0, 3)
            setCell(1, 3)
            setCell(2, 3)
            setCell(3, 3)
            setCell(4, 3)

            assertEquals("Check horizontal win 2", hasWon(), true)
        }
    }

    @Test
    fun `check vertical win 1`() {
        bingoViewModel.grid.apply {
            clear()

            setCell(0, 0)
            setCell(0, 1)
            setCell(0, 2)
            setCell(0, 3)
            setCell(0, 4)

            assertEquals("Check vertical win 1", hasWon(), true)
        }
    }

    @Test
    fun `check vertical win 2`() {
        bingoViewModel.grid.apply {
            clear()

            setCell(3, 0)
            setCell(3, 1)
            setCell(3, 2)
            setCell(3, 3)
            setCell(3, 4)

            assertEquals("Check vertical win 2", hasWon(), true)
        }
    }

    @Test
    fun `check diagonal win 1`() {
        bingoViewModel.grid.apply {
            clear()

            setCell(0, 0)
            setCell(1, 1)
            setCell(2, 2)
            setCell(3, 3)
            setCell(4, 4)

            assertEquals("Check diagonal win 1", hasWon(), true)
        }
    }

    @Test
    fun `check diagonal win 2`() {
        bingoViewModel.grid.apply {
            clear()

            setCell(0, 4)
            setCell(1, 3)
            setCell(2, 2)
            setCell(3, 1)
            setCell(4, 0)

            assertEquals("Check diagonal win 2", hasWon(), true)
        }
    }

    @Test
    fun `check not a win 1`() {
        bingoViewModel.grid.apply {
            clear()

            setCell(0, 0)
            setCell(1, 0)
            setCell(2, 1)
            setCell(3, 0)
            setCell(4, 0)

            assertEquals("Check not a win 1", hasWon(), false)
        }
    }

    @Test
    fun `check not a win 2`() {
        bingoViewModel.grid.apply {
            clear()

            setCell(0, 0)
            setCell(0, 1)
            setCell(0, 2)
            setCell(1, 3)
            setCell(0, 4)

            assertEquals("Check not a win 2", hasWon(), false)
        }
    }

    @Test
    fun `check not a win 3`() {
        bingoViewModel.grid.apply {
            clear()

            setCell(0, 0)
            setCell(1, 1)
            setCell(2, 3)
            setCell(3, 3)
            setCell(4, 4)

            assertEquals("Check not a win 3", hasWon(), false)
        }
    }

    @Test
    fun `check not a win 4`() {
        bingoViewModel.grid.apply {
            clear()

            setCell(0, 4)
            setCell(2, 3)
            setCell(2, 2)
            setCell(3, 1)
            setCell(4, 0)

            assertEquals("Check not a win 3", hasWon(), false)
        }
    }

    @Test
    fun `check cluster win 1`() {
        bingoViewModel.grid.apply {
            clear()

            setCell(0, 0)
            setCell(1, 0)
            setCell(2, 0)
            setCell(3, 0)
            setCell(4, 0)

            setCell(0, 1)
            setCell(0, 2)
            setCell(0, 3)
            setCell(0, 4)

            assertEquals("Check cluster win 1", hasWon(), true)
        }
    }

    @Test
    fun `check if unchecking works`() {
        bingoViewModel.grid.apply {
            clear()

            setCell(0, 0)
            setCell(1, 0)
            setCell(2, 0)
            setCell(3, 0)
            setCell(4, 0)

            setCell(4, 0, false)

            assertEquals("Check unchecking", hasWon(), false)
        }
    }
}
