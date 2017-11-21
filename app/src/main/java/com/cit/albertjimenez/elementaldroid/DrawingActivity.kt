package com.cit.albertjimenez.elementaldroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.cit.albertjimenez.elementaldroid.views.SketchSheetView
import kotlinx.android.synthetic.main.activity_drawing.*

class DrawingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)
        val myDrawingView = SketchSheetView(this)
        constraintLayDraw.addView(myDrawingView, ViewGroup.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT))

        fltRepaint.setOnClickListener { myDrawingView.repaintView() }
    }
}
