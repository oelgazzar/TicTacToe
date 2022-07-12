package com.example.tictactoe.models

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GameViewModel: ViewModel() {
    lateinit var matrix:Array<Array<String>>
    private lateinit var nextTurn: String

    init {
        reset()
    }

    fun reset() {
        matrix = createEmptyMatrix()
        nextTurn = "p1"
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

    fun updateMatrix(row: Int, col: Int) {
        if (matrix[row][col].isNotEmpty()) return
        matrix[row][col] = if (nextTurn == "p1") "o" else "x"
        checkGameEnd()
        switchTurn()
    }

    private fun checkGameEnd(): Boolean {
        matrix.forEach { it.forEach { if (it.isEmpty()) return false } }
        Log.d("tictactoe", "game ended")
        return true
    }

    private fun switchTurn() {
        nextTurn = if (nextTurn == "p1") "p2" else "p1"
    }

}