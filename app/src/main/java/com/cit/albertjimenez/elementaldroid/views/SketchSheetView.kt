package com.cit.albertjimenez.elementaldroid.views

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by Albert Jiménez on 21/11/17 for Programming Mobile Devices.
 */
class SketchSheetView : View {

    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private lateinit var path2: Path
    private lateinit var paint: Paint
    private lateinit var drawingClassArrayList: ArrayList<DrawingClass>

    constructor(context: Context) : super(context) {
        this.bitmap = Bitmap.createBitmap(820, 480, Bitmap.Config.ARGB_4444)
        this.canvas = Canvas(bitmap)
        this.setBackgroundColor(Color.WHITE)
        this.drawingClassArrayList = ArrayList()
        this.path2 = Path()
        this.paint = Paint()
        with(paint) {
            isDither = true
            color = Color.parseColor("#000000")
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 5f
        }
    }

    public fun repaintView() = path2.reset()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val pathWithPaint = DrawingClass()

        canvas.drawPath(path2, paint)

        if (event?.action == MotionEvent.ACTION_DOWN) {

            path2.moveTo(event.x, event.y)

            path2.lineTo(event.x, event.y)
        } else if (event?.action == MotionEvent.ACTION_MOVE) {

            path2.lineTo(event.x, event.y)

            pathWithPaint.drawingClassPath = path2

            pathWithPaint.drawingClassPaint = paint

            drawingClassArrayList.add(pathWithPaint)
        }

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (drawingClassArrayList.size > 0)
            canvas?.drawPath(
                    drawingClassArrayList[drawingClassArrayList.size - 1].drawingClassPath,

                    drawingClassArrayList[drawingClassArrayList.size - 1].drawingClassPaint)
    }
}

data class DrawingClass(var drawingClassPath: Path = Path(), var drawingClassPaint: Paint = Paint())