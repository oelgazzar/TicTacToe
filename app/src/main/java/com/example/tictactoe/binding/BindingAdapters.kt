package com.example.tictactoe.binding

import androidx.databinding.BindingAdapter
import com.example.tictactoe.ui.BoardView

@BindingAdapter("matrix")
fun updateMatrix(view: BoardView, matrix: Array<Array<String>>) {
    view.matrix = matrix
}