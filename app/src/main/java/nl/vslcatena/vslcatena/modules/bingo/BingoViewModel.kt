package nl.vslcatena.vslcatena.modules.bingo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class BingoViewModel : ViewModel() {
    var gridSize = 0
    lateinit var grid: Array<Array<BingoCell>>
    val won = MutableLiveData<Boolean>()

    private val gridObserver = Observer<Boolean> {
        won.value = checkIfWon()
    }

    fun initialize(textList: List<String>, newGridSize: Int) {
        // Check if we have enough texts to fill the grid
        if ((newGridSize * newGridSize) > textList.size)
            throw IndexOutOfBoundsException("textList is smaller than the grid that needs to be filled")

        gridSize = newGridSize

        // Randomize the textPool
        val textPool = ArrayList(textList)
        textPool.shuffle()

        // Create a new list
        grid = Array(gridSize) { x ->
            Array(gridSize) { y ->
                BingoCell(textPool[x * gridSize + y]).apply {
                    isChecked.observeForever(gridObserver)
                }
            }
        }
    }

    override fun onCleared() {
        grid.forEach {
            it.forEach {
                it.isChecked.removeObserver(gridObserver)
            }
        }
    }
    fun isCellChecked(x: Int, y: Int) = grid[x][y].isChecked()

    private fun checkIfWon(): Boolean {
        if (gridSize % 2 != 0) {
            run diagonal1@{
                for (x in 0 until gridSize)
                    if (!isCellChecked(x, x)) return@diagonal1
                return true
            }

            run diagonal2@{
                for (x in 0 until gridSize)
                    if (!isCellChecked(x, gridSize - x - 1)) return@diagonal2
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

    data class BingoCell(val text: String, val isChecked: MutableLiveData<Boolean> = MutableLiveData()) {

        fun toggle() {
            isChecked.value = !isChecked()
        }

        fun setChecked(checked: Boolean) {
            isChecked.value = checked
        }

        fun isChecked() = isChecked.value ?: false
    }
}