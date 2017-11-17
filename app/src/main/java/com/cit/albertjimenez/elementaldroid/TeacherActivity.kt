package com.cit.albertjimenez.elementaldroid

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.JsonReader
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
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
    private val localesAPI = listOf("https://en.wikipedia.org/api/rest_v1/page/summary/", "https://es.wikipedia.org/api/rest_v1/page/summary/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)
        setSupportActionBar(toolbarTeacher)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        searchView.isIconified = false
        searchView.queryHint = getString(R.string.query_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0.isNullOrBlank())
                    fetchApiWiki()
                else
                    fetchApiWiki(p0!!)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean = true
        })
        fab.setOnClickListener { startActivity<TeacherActivity>() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.teacher_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_settings)
            logOutGoogle(this, Welcome::class.java)


        return super.onOptionsItemSelected(item)
    }

    fun parseJSONtoElement(jsonReader: JsonReader): Element {
        val strings = arrayListOf("title", "extract", "thumbnail")
        val myElement = Element()
        var value = ""
        var desc = ""
        var photo = ""
        jsonReader.beginObject() // Start processing the JSON object
        while (jsonReader.hasNext()) { // Loop through all keys
            val key = jsonReader.nextName() // Fetch the next key
            if (key == strings[0]) {
                value = jsonReader.nextString()
            } else if (key == strings[1]) {
                desc = jsonReader.nextString()
            } else if (key == strings[2])
                photo = parsePhoto(jsonReader)
            else
                jsonReader.skipValue() // Skip values of other keys

        }
        with(myElement) {
            title = value
            extract = desc
            original = photo
        }
        jsonReader.endObject()
        jsonReader.close()
        return myElement
    }

    fun parsePhoto(jsonReader: JsonReader): String {
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

    fun fetchApiWiki(query: String = "") {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        var elem: Element
        if (isConnected && !query.isBlank()) {
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
                    //When you've got the photo
                    uiThread {
                        dataManagerFB.storeNewElement(elem)
                        element_name.text = elem.title
                        element_desc.text = elem.extract
                        Picasso.with(applicationContext).load(elem.original).fit().centerCrop().into(element_photo)
                    }

                } else
                    uiThread { Toasty.error(applicationContext, getString(R.string.rest_element_error), Toast.LENGTH_SHORT).show() }

                httpsURLConnection.disconnect()
            }
        }

    }
//TODO save the query into the bundle
}
