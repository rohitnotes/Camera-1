package xyz.romakononovich.camera.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by RomanK on 05.05.18.
 */
class GridView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var linePaint = Paint()

    init {
        linePaint.apply {
            isAntiAlias = true
            color = Color.parseColor("#45e0e0e0")
            strokeWidth = 3f
        }

    }

    override fun onDraw(canvas: Canvas) {
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        val width = screenWidth / 3
        val height = screenHeight / 3

        (1..2).forEach {
            canvas.drawLine(width * it.toFloat(), 0f, width * it.toFloat(), screenHeight.toFloat(), linePaint)
            canvas.drawLine(0f, height * it.toFloat(), screenWidth.toFloat(), height * it.toFloat(), linePaint)
        }
    }


}
