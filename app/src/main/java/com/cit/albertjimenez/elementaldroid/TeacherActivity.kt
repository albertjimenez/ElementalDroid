package com.cit.albertjimenez.elementaldroid

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.JsonReader
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.SeekBar
import android.widget.Toast
import com.cit.albertjimenez.elementaldroid.dao.Element
import com.cit.albertjimenez.elementaldroid.datastructures.DataManagerJ
import com.cit.albertjimenez.elementaldroid.utils.logOutGoogle
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_teacher.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection


class TeacherActivity : AppCompatActivity() {

    private val dataManagerFB: DataManagerJ = DataManagerJ.getInstance()
    private val localesAPI = listOf("https://en.wikipedia.org/api/rest_v1/page/summary/",
            "https://es.wikipedia.org/api/rest_v1/page/summary/")
    private lateinit var seekbarOnClick: ListElements.SeekbarOnClick
    private var currentBrightness = 50
    private val requestPermission = 4
    private val SAVEDINSTANCENAMES = listOf("NAME", "DESCRIPTION", "PHOTO")
    private var photoURL = "--"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)
        setSupportActionBar(toolbarTeacher)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //Searchview for querying into wikipedia database
        with(searchView) {
            isIconified = false
            queryHint = getString(R.string.query_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {

                    //Just execute the function if p0 is not null
                    p0?.let { fetchApiWiki(p0) }
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean = true
            })
        }
        //Defined lambda action listener
        fab.setOnClickListener { startActivity<TeacherActivity>() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.teacher_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 3 elements are available for the user.
     * The first one is for signing out of the GAccount
     * The second one for drawing a picture with a canvas
     * The last one for modifying the brightness of the app with a seekbar
     *
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_signout -> logOutGoogle(this, Welcome::class.java)
            R.id.action_paint -> startActivity<DrawingActivity>()
            R.id.action_brightness_seekbar -> {
                currentBrightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
                val alertDialog = AlertDialog.Builder(this)
                val seekBar = SeekBar(this)

                with(seekBar) {
                    max = 255
                    keyProgressIncrement = 1
                    seekbarOnClick = ListElements.SeekbarOnClick(cr = contentResolver, window = window)
                    setOnSeekBarChangeListener(seekbarOnClick)
                }
                with(alertDialog) {
                    setIcon(android.R.drawable.btn_star_big_on)
                    setView(seekBar)
                    setTitle(getString(R.string.choose_brightness))
                    show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * @param Uses JsonReader for converting to an Element
     *
     * @return An Element with data or empty Element if the JSON is not valid
     */
    private fun parseJSONtoElement(jsonReader: JsonReader): Element {
        val strings = arrayListOf("title", "extract", "thumbnail")
        val myElement = Element()
        var value = ""
        var desc = ""
        var photo = ""
        jsonReader.beginObject() // Start processing the JSON object
        while (jsonReader.hasNext()) { // Loop through all keys
            val key = jsonReader.nextName() // Fetch the next key
            when (key) {
                strings[0] -> value = jsonReader.nextString()
                strings[1] -> desc = jsonReader.nextString()
                strings[2] -> photo = parsePhoto(jsonReader)
                else -> jsonReader.skipValue()
            } // Skip values of other keys

        }
        with(myElement) {
            title = value
            extract = desc
            original = photo
            photoURL = original
        }
        jsonReader.endObject()
        jsonReader.close()
        return myElement
    }

    /**
     *  Parse the photo of the JSON request on other thread and return the URL
     */
    private fun parsePhoto(jsonReader: JsonReader): String {
        var string = ""
        jsonReader.beginObject() // Start processing the JSON object
        while (jsonReader.hasNext()) {
            val key = jsonReader.nextName()
            if (key == "source")
                string = jsonReader.nextString()
            else jsonReader.skipValue()
        }
        jsonReader.endObject()
        return string

    }

    /**
     * Based on the language on the device, it will go with a spanish or english version
     * of Wikipedia database
     */
    fun fetchApiWiki(query: String = "") {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        var elem: Element
        if (isConnected && !query.isBlank()) {
            //Doing Async task on the background
            doAsync {
                var wikiElement: URL
                if (Locale.getDefault().language != "es")
                    wikiElement = URL(localesAPI[0] + query)
                else
                    wikiElement = URL(localesAPI[1] + query)
                val httpsURLConnection = wikiElement.openConnection() as HttpsURLConnection
                if (httpsURLConnection.responseCode == 200) {
                    val jsonReader = JsonReader(InputStreamReader(httpsURLConnection.inputStream, "UTF-8"))
                    elem = parseJSONtoElement(jsonReader)
                    Log.d("ELEMENT WIKI", elem.toString())
                    //When you've got the photo, uiThread takes the values and execute the code
                    //on the main thread
                    uiThread {
                        dataManagerFB.storeNewElement(elem)
                        element_name.text = elem.title
                        element_desc.text = elem.extract
                        if (elem.original.isNotEmpty())
                            Picasso.with(applicationContext).load(elem.original).fit().centerCrop().into(element_photo)
                    }

                } else
                    uiThread { Toasty.error(applicationContext, getString(R.string.rest_element_error), Toast.LENGTH_SHORT).show() }

                httpsURLConnection.disconnect()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.let {
            with(outState) {
                putString(SAVEDINSTANCENAMES[0], element_name.text.toString())
                putString(SAVEDINSTANCENAMES[1], element_desc.text.toString())
                putString(SAVEDINSTANCENAMES[2], photoURL)
            }
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            with(savedInstanceState) {
                element_name.text = getString(SAVEDINSTANCENAMES[0])
                element_desc.text = getString(SAVEDINSTANCENAMES[1])
                Picasso.with(applicationContext).load(getString(SAVEDINSTANCENAMES[2])).fit().centerCrop().into(element_photo)
            }
        }
    }
}
