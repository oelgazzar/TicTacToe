package com.example.tictactoe.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.sign
import kotlin.random.Random

private const val TAG = "Tic Tac Toe [GameViewModel]"

class GameViewModel: ViewModel() {
    lateinit var matrix:Array<Array<String>>
    private lateinit var nextTurn: String
    private var _p1Score = MutableLiveData(0)
    val p1Score: LiveData<Int> get() = _p1Score
    private var _p2Score = MutableLiveData(0)
    val p2Score:LiveData<Int> get() = _p2Score
    private var _winner: Int = 0
    val winner: Int get() = _winner
    private var _gameEnded = MutableLiveData(false)
    val gameEnded: LiveData<Boolean> get() = _gameEnded

    // AI mode
    var mode = "AI"
    var firstMove = true

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
        matrix = createEmptyMatrix()
        nextTurn = "p1"
//        _p1Score.value = 0
//        _p2Score.value = 0
        _winner = 0
        firstMove = true
        _gameEnded.value = false
    }

    private fun createRandomMatrix(): Array<Array<String>> {
        val random = Random(System.currentTimeMillis())
        return Array(3) {
            Array(3
            ) { arrayListOf("o", "x", "")[random.nextInt(3)] }
        }
    }

    private fun createEmptyMatrix(): Array<Array<String>> {
        val random = Random(System.currentTimeMillis())
        return Array(3) {
            Array(3
            ) { "" }
        }
    }

    fun updateMatrixAndCheckGameEnd(row: Int, col: Int): Boolean {
        // Cell not empty -> return
        if (matrix[row][col].isNotEmpty()) return false

        // Cell empty -> fill
        matrix[row][col] = if (nextTurn == "p1") "o" else "x"

        // Game not ended yet -> switch player and continue
        if (!checkGameEnd()) {
            switchTurn()
            return false
        }

        // Game ended
        return true
    }

    private fun checkGameEnd(): Boolean {
        for (series in checkMatrix) {
            val set = series.map {
                val row = it[0]
                val col = it[1]
                matrix[row][col]
            }.toSet()
            if (set.size == 1 && set.first() != "") {
                if (set.first() == "o") {
                    _winner = 1
                    _p1Score.value = _p1Score.value?.inc()
                } else {
                    _winner = 2
                    _p2Score.value = _p2Score.value?.inc()
                }
                _gameEnded.value = true
                reset()
                return true
            }
        }

        for (series in matrix) {
            for (cell in series) {
                if (cell.isEmpty()) return false
            }
        }
        _gameEnded.value = true
        reset()
       return true
    }

    private fun switchTurn() {
        nextTurn = if (nextTurn == "p1") "p2" else "p1"
        if (nextTurn == "p2" && mode == "AI") {
            autoMove()
        }
    }

    private fun autoMove() {
        var row = 0
        var col = 0
        // First move: either check central or any corner cell according to player first move
        if (firstMove) {
            if (matrix[1][1].isEmpty()) {
                row = 1
                col = 1
            }
            else {
                val row = arrayOf(0, 2).random()
                val col = arrayOf(0, 2).random()
                Log.d(TAG, "First move: ($row, $col)")
            }
            firstMove = false
            updateMatrixAndCheckGameEnd(row, col)
            return
        }


            // A win
        for (series in checkMatrix) {
            val signs = series.map { matrix[it[0]][it[1]] }
            if (signs.filter { it == "x" }.size == 2 && signs.contains("")) {
                val (r, c) = series[signs.indexOf("")]
                updateMatrixAndCheckGameEnd(r, c)
                return
            }
        }

        // An impending lose
        for (series in checkMatrix) {
            val signs = series.map { matrix[it[0]][it[1]] }
            if (signs.filter { it == "o" }.size == 2 && signs.contains("")) {
                val (r, c) = series[signs.indexOf("")]
                updateMatrixAndCheckGameEnd(r, c)
                return
            }
        }

        // Create a win
        for (series in checkMatrix.toList().shuffled()) {
            val signs = series.map { matrix[it[0]][it[1]] }
            if (signs.filter { it == "" }.size == 2 && signs.contains("x")) {
                val (r, c) = series[signs.indexOf("")]
                updateMatrixAndCheckGameEnd(r, c)
                return
            }
        }

        for (series in checkMatrix.toList().shuffled()) {
            val signs = series.map { matrix[it[0]][it[1]] }
            if (signs.contains("")) {
                val (r, c) = series[signs.indexOf("")]
                updateMatrixAndCheckGameEnd(r, c)
                return
            }
        }
    }

}