package com.cit.albertjimenez.elementaldroid

import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.JsonReader
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import com.cit.albertjimenez.elementaldroid.dao.Element
import com.cit.albertjimenez.elementaldroid.utils.logOutGoogle
import com.cit.albertjimenez.elementaldroid.utils.snackbarMakeIt
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_teacher.*
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection


class TeacherActivity : AppCompatActivity() {

    //    val dataManagerFB: DataManagerJ = DataManagerJ.getInstance()
    val localesAPI = listOf("https://en.wikipedia.org/api/rest_v1/page/summary/", "https://es.wikipedia.org/api/rest_v1/page/summary/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)
        setSupportActionBar(toolbarTeacher)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        search_wiki_element.setOnClickListener {
            fetchApiWiki()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.teacher_menu, menu);
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
            if (key == "original")
                string = jsonReader.nextString()
            else jsonReader.skipValue()
        }
        jsonReader.endObject()
        return string

    }

    fun fetchApiWiki() {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        var elem: Element = Element()
        if (isConnected && !inputWikiElement.editText?.text.isNullOrBlank())
            AsyncTask.execute({
                var wikiElement: URL
                if (Locale.getDefault().language != "es")
                    wikiElement = URL(localesAPI[0] + inputWikiElement.editText?.text)
                else
                    wikiElement = URL(localesAPI[1] + inputWikiElement.editText?.text)
                val httpsURLConnection = wikiElement.openConnection() as HttpsURLConnection
                if (httpsURLConnection.responseCode == 200) {
                    val responseBody = httpsURLConnection.inputStream
                    val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
                    val jsonReader = JsonReader(responseBodyReader)
                    elem = parseJSONtoElement(jsonReader)
                    Log.d("ELEMENT WIKI", elem.toString())
                    val url = URL(elem.original)
                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    runOnUiThread { element_photo.setImageBitmap(bmp) }
                } else
                    runOnUiThread({ Toasty.error(this, getString(R.string.rest_element_error)).show() })

                httpsURLConnection.disconnect()
            })
        else if (inputWikiElement.editText?.text.isNullOrBlank())
            snackbarMakeIt(teacherLayout, getString(R.string.empty_text), Snackbar.LENGTH_LONG)
        else runOnUiThread {
            if (teacherLayout != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(teacherLayout.windowToken, 0)
                snackbarMakeIt(teacherLayout, getString(R.string.no_internet), Snackbar.LENGTH_LONG)
            }
        }
    }

}
