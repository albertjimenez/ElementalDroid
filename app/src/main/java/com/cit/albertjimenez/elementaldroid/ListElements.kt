package com.cit.albertjimenez.elementaldroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class ListElements : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_elements)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}
