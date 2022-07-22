package com.example.tictactoe.util

import android.animation.Animator
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.tictactoe.ui.BoardView

@BindingAdapter("animator", "delay", requireAll = false)
fun setAnimator(view: View, animator: Animator, delay: Long?) {
    val context = view.context
    animator.apply {
        setTarget(view)
        startDelay = delay?:0
        start()
    }
}

@BindingAdapter("matrix")
fun updateMatrix(view: BoardView, matrix: Array<Array<String>>) {
    view.matrix = matrix
}