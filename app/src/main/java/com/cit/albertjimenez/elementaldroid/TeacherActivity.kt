package com.cit.albertjimenez.elementaldroid

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Toast

class TeacherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view -> Snackbar.make(view, "Not implemented yet", Toast.LENGTH_SHORT) }
        //Data manager
//        val dataManagerFB = DataManagerJ.getInstance()
        //        ImageView imageView = findViewById(R.id.pluton);
        //        Log.d("BASE64", MyBase64ParserKt.encodeImage(((BitmapDrawable)imageView.getDrawable()).getBitmap()));

    }

}
