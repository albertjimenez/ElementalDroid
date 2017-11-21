package com.cit.albertjimenez.elementaldroid

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.System.SCREEN_BRIGHTNESS
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.transition.TransitionInflater
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
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
    private lateinit var seekbarOnClick: SeekbarOnClick
    private var currentBrightness = 50
    private val requestPermission = 4

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
        permissions()
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
        if (item?.itemId == R.id.action_paint)
            startActivity<DrawingActivity>()
        else {
            currentBrightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
            val alertDialog = AlertDialog.Builder(this)
            val seekBar = SeekBar(this)

            with(seekBar) {
                max = 255
                keyProgressIncrement = 1
                seekbarOnClick = SeekbarOnClick(cr = contentResolver, window = window)
                setOnSeekBarChangeListener(seekbarOnClick)
            }
            with(alertDialog) {
                setIcon(android.R.drawable.btn_star_big_on)
                setView(seekBar)
                setTitle(getString(R.string.choose_brightness))
                show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun permissions() {
        if (!Settings.System.canWrite(applicationContext)) {
            val intent = Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    .setData(Uri.parse("package:" + packageName))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == requestPermission) {
            seekbarOnClick = SeekbarOnClick(cr = contentResolver, window = window)
        } else
            permissions()
    }

    class SeekbarOnClick(private var brightness: Int = 50, cr: ContentResolver, window: Window) : OnSeekBarChangeListener {

        private var window: Window? = window
        private var cResolver: ContentResolver? = cr


        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            brightness = if (p1 <= 20) {
                20
            } else {
                p1
            }
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
            Settings.System.putInt(cResolver, SCREEN_BRIGHTNESS, brightness)
            //Get the current window attributes
            val layoutpars = window!!.getAttributes()
            //Set the brightness of this window
            layoutpars.screenBrightness = brightness / 255.toFloat()
            //Apply attribute changes to this window
            window!!.attributes = layoutpars
        }
    }
}
