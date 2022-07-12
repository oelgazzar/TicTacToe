package com.example.tictactoe.models

import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GameViewModel: ViewModel() {
    var  matrix = createRandomMatrix()

    private fun createRandomMatrix(): Array<Array<String>> {
        val random = Random(System.currentTimeMillis())
        return Array(3) {
            Array(3
            ) { arrayListOf("o", "x", "")[random.nextInt(3)] }
        }
    }
}