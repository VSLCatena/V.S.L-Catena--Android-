package nl.vslcatena.vslcatena.modules.bingo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BingoViewModel : ViewModel() {
    lateinit var grid: Array<Array<MutableLiveData<Boolean>>>


    fun toggleCell(x: Int, y: Int) {
        grid[x][y].value = !(grid[x][y].value ?: false)
    }
}