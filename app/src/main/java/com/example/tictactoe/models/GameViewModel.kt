package com.example.tictactoe.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

private const val TAG = "Tic Tac Toe [GameViewModel]"

class GameViewModel: ViewModel() {
    private var _p1Score = MutableLiveData(0)
    val p1Score: LiveData<Int> get() = _p1Score
    private var _p2Score = MutableLiveData(0)
    val p2Score:LiveData<Int> get() = _p2Score
    private var _winner: Int = 0
    val winner: Int get() = _winner
    private var _isGameRunning = MutableLiveData(false)
    val isGameRunning: LiveData<Boolean> get() = _isGameRunning

    // AI mode
    private var _mode = Mode.AI
    var mode: String get() = if (_mode == Mode.AI) "AI" else "Player2"
    set(value) {
        _mode = if (value == "AI") Mode.AI else Mode.PLAYER
    }
    private var firstMove = true

    private var _matrix = MutableLiveData<Array<Array<String>>>()
    val matrix: LiveData<Array<Array<String>>> get() = _matrix
    private lateinit var nextTurn: String
    private val checkMatrix = arrayOf(
        arrayOf(arrayOf(0, 0), arrayOf(0, 1), arrayOf(0, 2)),
        arrayOf(arrayOf(1, 0), arrayOf(1, 1), arrayOf(1, 2)),
        arrayOf(arrayOf(2, 0), arrayOf(2, 1), arrayOf(2, 2)),
        arrayOf(arrayOf(0, 0), arrayOf(1, 0), arrayOf(2, 0)),
        arrayOf(arrayOf(0, 1), arrayOf(1, 1), arrayOf(2, 1)),
        arrayOf(arrayOf(0, 2), arrayOf(1, 2), arrayOf(2, 2)),
        arrayOf(arrayOf(0, 0), arrayOf(1, 1), arrayOf(2, 2)),
        arrayOf(arrayOf(0, 2), arrayOf(1, 1), arrayOf(2, 0)),
    )


    init {
        reset()
    }

    fun reset() {
        _matrix.value = createEmptyMatrix()
        nextTurn = "p1"
        _winner = 0
        firstMove = true
        _isGameRunning.value = true
    }

    private fun createEmptyMatrix(): Array<Array<String>> {
        Random(System.currentTimeMillis())
        return Array(3) {
            Array(3
            ) { "" }
        }
    }

    fun updateMatrix(row: Int, col: Int) {
        // Cell not empty -> return
        if (_matrix.value!![row][col].isNotEmpty()) return

        // Cell empty -> fill
        _matrix.value!![row][col] = if (nextTurn == "p1") "o" else "x"

        // Game not ended yet -> switch player and continue
        if (!checkGameEnd()) {
            switchTurn()
            return
        }
    }

    private fun checkGameEnd(): Boolean {
        for (series in checkMatrix) {
            val set = series.map {
                val row = it[0]
                val col = it[1]
                _matrix.value!![row][col]
            }.toSet()
            if (set.size == 1 && set.first() != "") {
                if (set.first() == "o") {
                    _winner = 1
                    _p1Score.value = _p1Score.value?.inc()
                } else {
                    _winner = 2
                    _p2Score.value = _p2Score.value?.inc()
                }
                _isGameRunning.value = false
//                reset()
                return true
            }
        }

        for (series in _matrix.value!!) {
            for (cell in series) {
                if (cell.isEmpty()) return false
            }
        }
        _isGameRunning.value = false
//        reset()
       return true
    }

    private fun switchTurn() {
        nextTurn = if (nextTurn == "p1") "p2" else "p1"
        if (nextTurn == "p2" && _mode == Mode.AI) {
            autoMove()
        }
    }

    private fun autoMove() {
        val row: Int
        val col: Int
        // First move: either check central or any corner cell according to player first move
        if (firstMove) {
            if (_matrix.value!![1][1].isEmpty()) {
                row = 1
                col = 1
            }
            else {
                row = arrayOf(0, 2).random()
                col = arrayOf(0, 2).random()
                Log.d(TAG, "First move: ($row, $col)")
            }
            firstMove = false
            updateMatrix(row, col)
            return
        }


            // A win
        for (series in checkMatrix) {
            val signs = series.map { _matrix.value!![it[0]][it[1]] }
            if (signs.filter { it == "x" }.size == 2 && signs.contains("")) {
                val (r, c) = series[signs.indexOf("")]
                updateMatrix(r, c)
                return
            }
        }

        // An impending lose
        for (series in checkMatrix) {
            val signs = series.map { _matrix.value!![it[0]][it[1]] }
            if (signs.filter { it == "o" }.size == 2 && signs.contains("")) {
                val (r, c) = series[signs.indexOf("")]
                updateMatrix(r, c)
                return
            }
        }

        // Create a win
        for (series in checkMatrix.toList().shuffled()) {
            val signs = series.map { _matrix.value!![it[0]][it[1]] }
            if (signs.filter { it == "" }.size == 2 && signs.contains("x")) {
                val (r, c) = series[signs.indexOf("")]
                updateMatrix(r, c)
                return
            }
        }

        for (series in checkMatrix.toList().shuffled()) {
            val signs = series.map { _matrix.value!![it[0]][it[1]] }
            if (signs.contains("")) {
                val (r, c) = series[signs.indexOf("")]
                updateMatrix(r, c)
                return
            }
        }
    }

    enum class Mode {
        AI, PLAYER
    }

}