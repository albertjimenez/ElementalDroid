package com.cit.albertjimenez.elementaldroid

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.cit.albertjimenez.elementaldroid.dao.Element
import com.cit.albertjimenez.elementaldroid.dao.RegularUser
import com.cit.albertjimenez.elementaldroid.datastructures.DataManagerJ
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_view_item.*
import kotlinx.android.synthetic.main.content_view_item.*
import org.jetbrains.anko.share

class ViewItemActivity : AppCompatActivity() {

    private val dataManager = DataManagerJ.getInstance()

    companion object {
        var user: RegularUser = RegularUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_layout.setExpandedTitleColor(Color.BLACK)
        toolbar_layout.setCollapsedTitleTextColor(Color.BLACK)
        val element: Element = intent.getSerializableExtra("element") as Element
        supportActionBar?.title = element.title
        element_view_description.text = element.extract
        val imageView = ImageView(this)
        Picasso.with(this).load(element.original).into(imageView, object : Callback {
            override fun onSuccess() {
                toolbar_layout.background = imageView.drawable
            }

            override fun onError() {
                Toasty.error(this@ViewItemActivity, getString(R.string.load_image_error)).show()
            }
        })
        user = dataManager.retrieveUser(intent.getStringExtra("email"))

        shareText.setOnClickListener {
            val str = "${element.title}\n${element.extract}"
            share(str)
        }
    }
}
