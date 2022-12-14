package com.example.tictactoe.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.tictactoe.R

class BoardView(context: Context, attrSet: AttributeSet):
    AppCompatImageView(context, attrSet), View.OnTouchListener {
    companion object {
        private const val SIGN_WIDTH = 100
        private const val SIGN_HEIGHT = SIGN_WIDTH
        private const val CELL_PADDING = SIGN_WIDTH / 2
        private const val CELL_WIDTH = SIGN_WIDTH + CELL_PADDING * 2
        private const val CELL_HEIGHT = SIGN_HEIGHT + CELL_PADDING * 2
        private const val LINE_WIDTH = 2f
        private const val BOARD_WIDTH = CELL_WIDTH * 3 + LINE_WIDTH * 2
        private const val BOARD_HEIGHT = CELL_HEIGHT * 3 + LINE_WIDTH * 2
    }

    private val paint = Paint()
    lateinit var matrix: Array<Array<String>>

    var onMoveListener: OnMoveListener? = null

    init {
        setOnTouchListener(this)

    }



    override fun onDraw(canvas: Canvas) {
        paint.strokeWidth = LINE_WIDTH
        paint.color = ContextCompat.getColor(context, R.color.gray)
        drawGrid(canvas)
        drawMatrix(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        val offsetX = (width - BOARD_WIDTH) / 2
        val offsetY = (height - BOARD_HEIGHT) / 2

        // Draw left vertical line of the grid
        canvas.drawLine(offsetX + CELL_WIDTH,
            offsetY,
            offsetX + CELL_WIDTH,
            offsetY + BOARD_HEIGHT,
            paint)

        // Draw the right vertical line of the grid
        canvas.drawLine(offsetX + 2 * CELL_WIDTH + LINE_WIDTH,
            offsetY,
            offsetX + 2 * CELL_WIDTH + LINE_WIDTH,
            offsetY + BOARD_HEIGHT,
            paint)

        // Draw the upper horizontal line of the grid
        canvas.drawLine(offsetX,
            offsetY + CELL_HEIGHT,
            offsetX + BOARD_WIDTH,
            offsetY + CELL_HEIGHT,
            paint)

        // Draw the lower horizontal line of the grid
        canvas.drawLine(offsetX,
            offsetY + 2 * CELL_HEIGHT + LINE_WIDTH,
            offsetX + BOARD_WIDTH,
            offsetY + 2 * CELL_HEIGHT + LINE_WIDTH,
            paint)
    }

    private fun drawMatrix(canvas: Canvas) {
        val offsetX = (width - BOARD_WIDTH) / 2
        val offsetY = (height - BOARD_HEIGHT) / 2

        for (row in matrix.indices) {
            for (col in matrix.indices) {
                val cell = matrix[row][col]
                if (cell.isEmpty()) continue

                val x = offsetX + col * (CELL_WIDTH + LINE_WIDTH) + CELL_PADDING
                val y = offsetY + row * (CELL_HEIGHT + LINE_WIDTH) + CELL_PADDING

                val drawableResId = when(cell) {
                    "x" -> R.drawable.ic_x
                    else -> R.drawable.ic_o
                }
                val signDrawable = AppCompatResources.getDrawable(context, drawableResId) as VectorDrawable
                signDrawable.setBounds(x.toInt(),
                    y.toInt(),
                    x.toInt() + SIGN_WIDTH,
                    y.toInt() + SIGN_HEIGHT)
                signDrawable.draw(canvas)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val requestedWidth = MeasureSpec.getSize(widthMeasureSpec)
        val requestedWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val requestedHeight = MeasureSpec.getSize(heightMeasureSpec)
        val requestedHeightMode = MeasureSpec.getMode(heightMeasureSpec)

        val width = when(requestedWidthMode) {
            MeasureSpec.EXACTLY -> requestedWidth
            MeasureSpec.UNSPECIFIED -> BOARD_WIDTH
            else -> minOf(requestedWidth.toFloat(), BOARD_WIDTH)
        }

        val height = when(requestedHeightMode) {
            MeasureSpec.EXACTLY -> requestedHeight
            MeasureSpec.UNSPECIFIED -> BOARD_HEIGHT
            else -> minOf(requestedHeight.toFloat(), BOARD_HEIGHT)
        }

        setMeasuredDimension(width.toInt(), height.toInt())
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN) return false

        val offsetX = (width - BOARD_WIDTH) / 2
        val offsetY = (height - BOARD_HEIGHT) / 2
        val x = event.x - offsetX
        val y =  event.y - offsetY
        var row = 0
        var col = 0

        if (x <= BOARD_WIDTH && y <= BOARD_HEIGHT) {
            col = x.toInt() / CELL_WIDTH
            row = y.toInt() / CELL_HEIGHT
        }

        onMoveListener?.onMove(row, col)
        invalidate()

        return true
    }

    interface OnMoveListener {
        fun onMove(row: Int, col: Int)
    }
}