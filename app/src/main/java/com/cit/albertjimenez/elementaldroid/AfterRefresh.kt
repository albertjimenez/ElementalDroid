package com.cit.albertjimenez.elementaldroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_after_refresh.*

class AfterRefresh : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_refresh)
        with(animated_svg_view) {
            start()
            setOnClickListener { start() }
        }
    }
}
