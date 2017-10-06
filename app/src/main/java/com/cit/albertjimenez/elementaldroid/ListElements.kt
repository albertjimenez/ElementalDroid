package com.cit.albertjimenez.elementaldroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.cit.albertjimenez.elementaldroid.barcode.BarcodeCaptureActivity
import com.cit.albertjimenez.elementaldroid.datastructures.DataManagerJ
import com.cit.albertjimenez.elementaldroid.views.ReciclerAdapter
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.android.synthetic.main.activity_list_elements.*

class ListElements : AppCompatActivity() {

    val dataManagerFB: DataManagerJ = DataManagerJ.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataManagerFB.discoverElements()
        setContentView(R.layout.activity_list_elements)
        supportActionBar?.title = intent.getStringExtra("PROFILEUSERNAME")
        val email = intent.getStringExtra("PROFILEEMAIL")
        //initialize ImageView
//        Glide.with(this).load(intent.getStringExtra("PROFILEPHOTO")).into(profileUser)
        with(reciclerViewListItems) {

            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            val list = dataManagerFB.retrieveElementsByUser(email)
            val myAdapter = ReciclerAdapter(list, email)
            adapter = ScaleInAnimationAdapter(myAdapter)
            itemAnimator = LandingAnimator()
        }

        fabAddElements.setOnClickListener {
            val intent = Intent(applicationContext, BarcodeCaptureActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode = data.getParcelableExtra<Barcode>("Barcode")
//                    val p = barcode.cornerPoints
                    Toasty.info(this, barcode.displayValue).show()
                } else
                    Toasty.error(this, "No QR code captured").show()
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}
