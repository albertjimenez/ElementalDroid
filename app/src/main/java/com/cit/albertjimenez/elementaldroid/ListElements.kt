package com.cit.albertjimenez.elementaldroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.transition.TransitionInflater
import android.view.Menu
import android.view.MenuItem
import com.cit.albertjimenez.elementaldroid.barcode.BarcodeCaptureActivity
import com.cit.albertjimenez.elementaldroid.datastructures.DataManagerJ
import com.cit.albertjimenez.elementaldroid.utils.logOutGoogle
import com.cit.albertjimenez.elementaldroid.views.ReciclerAdapter
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.android.synthetic.main.activity_list_elements.*
import org.jetbrains.anko.startActivity

class ListElements : AppCompatActivity() {

    private val dataManagerFB: DataManagerJ = DataManagerJ.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_elements)
        supportActionBar?.title = intent.getStringExtra("PROFILEUSERNAME")
        val email = intent.getStringExtra("PROFILEEMAIL")
        with(reciclerViewListItems) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            val list = dataManagerFB.mockretrieveElementsByUser(email)
            val myAdapter = ReciclerAdapter(list, email)
            adapter = ScaleInAnimationAdapter(myAdapter)
            itemAnimator = LandingAnimator()
        }
        with(swipeContainer) {
            setOnRefreshListener {
                startActivity<AfterRefresh>()
                isRefreshing = false
            }
            setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light)

        }
        fabAddElements.setOnClickListener {
            val intent = Intent(applicationContext, BarcodeCaptureActivity::class.java)
            startActivityForResult(intent, 1)
        }
        setupWindowAnimations()
    }

    private fun setupWindowAnimations() {
        val fade = TransitionInflater.from(this).inflateTransition(R.transition.activity_fade)
        window.enterTransition = fade
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode = data.getParcelableExtra<Barcode>("Barcode")
                    Toasty.info(this, barcode.displayValue).show()
                } else
                    Toasty.error(this, getString(R.string.no_qr_captured)).show()
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.listelements_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_settingsListElements)
            logOutGoogle(this, Welcome::class.java)

        return super.onOptionsItemSelected(item)
    }
}
