package nl.vslcatena.vslcatena.modules.bingo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import java.lang.IndexOutOfBoundsException

class BingoViewModel : ViewModel() {
    private val _grid = MutableLiveData<Array<Array<Boolean>>>()
    val textGrid = MutableLiveData<Array<Array<String>>>()
    val won = MutableLiveData<Boolean>()

    var gridSize: Int = 0
        private set

    val grid: LiveData<Array<Array<Boolean>>>
        get() = _grid

    private val gridObserver = Observer<Array<Array<Boolean>>> {
        won.value = checkIfWon()
    }

    fun initialize(textList: List<String>, newGridSize: Int) {
        // Check if we have enough texts to fill the grid
        if (newGridSize * newGridSize < textList.size)
            throw IndexOutOfBoundsException("textList is smaller than the grid that needs to be filled")

        gridSize = newGridSize

        // Randomize the textPool
        val textPool = ArrayList(textList)
        textPool.shuffle()

        // Create a new list
        _grid.value = Array(gridSize) {
            Array(gridSize) { false }
        }
        textGrid.value = Array(gridSize) { x ->
            // For every item in the textGrid we want to grab it from the textPool
            Array(gridSize) { y -> textPool[x * gridSize + y] }
        }

        grid.observeForever(gridObserver)
    }

    override fun onCleared() {
        grid.removeObserver(gridObserver)
    }

    fun toggleCell(x: Int, y: Int) {
        setCellChecked(x, y, !isCellChecked(x, y))
    }

    fun isCellChecked(x: Int, y: Int) = grid.value!![x][y]

    fun setCellChecked(x: Int, y: Int, checked: Boolean) {
        grid.value!![x][y] = checked
        _grid.value = _grid.value // We do this just to trigger the observers
    }

    fun checkIfWon(): Boolean {
        if (gridSize % 2 == 0) {
            run diagonal1@{
                for (x in 0 until gridSize)
                    if (!isCellChecked(x, x)) return@diagonal1
                return true
            }

            run diagonal2@{
                for (x in 0 until gridSize)
                    if (!isCellChecked(x, gridSize - x)) return@diagonal2
                return true
            }
        }

        horizontal@for (x in 0 until gridSize) {
            for (y in 0 until gridSize)
                if (!isCellChecked(x, y)) continue@horizontal
            return true
        }

        vertical@for (y in 0 until gridSize) {
            for (x in 0 until gridSize)
                if (!isCellChecked(x, y)) continue@vertical
            return true
        }

        return false
    }
}