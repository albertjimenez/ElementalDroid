package com.cit.albertjimenez.elementaldroid

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.JsonReader
import android.util.Log
import com.cit.albertjimenez.elementaldroid.dao.Element
import com.cit.albertjimenez.elementaldroid.datastructures.DataManagerJ
import kotlinx.android.synthetic.main.activity_teacher.*
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection


class TeacherActivity : AppCompatActivity() {

    val dataManagerFB: DataManagerJ = DataManagerJ.getInstance()
    val localesAPI = listOf<String>("https://en.wikipedia.org/api/rest_v1/page/summary/", "https://es.wikipedia.org/api/rest_v1/page/summary/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)
        supportActionBar?.setDisplayShowTitleEnabled(false)

//        fab.setOnClickListener { view -> Snackbar.make(view, "Not implemented yet", Toast.LENGTH_SHORT) }
        search_wiki_element.setOnClickListener {
            AsyncTask.execute({
                var wikiElement: URL
                if (Locale.getDefault() != Locale("es"))
                    wikiElement = URL(localesAPI[0] + inputWikiElement.editText?.text)
                else
                    wikiElement = URL(localesAPI[1] + inputWikiElement.editText?.text)
                val httpsURLConnection = wikiElement.openConnection() as HttpsURLConnection
                if (httpsURLConnection.responseCode == 200) {
                    val responseBody = httpsURLConnection.inputStream
                    val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
                    val jsonReader = JsonReader(responseBodyReader)
                    Log.d("ELEMENT WIKI", parseJSONtoElement(jsonReader).toString())
                }
//                else {
//                    Toasty.error(this, getString(R.string.rest_element_error)).show()
//                }
                httpsURLConnection.disconnect()
            })
        }
        //Data manager
//        val dataManagerFB = DataManagerJ.getInstance()
        //        ImageView imageView = findViewById(R.id.pluton);
        //        Log.d("BASE64", MyBase64ParserKt.encodeImage(((BitmapDrawable)imageView.getDrawable()).getBitmap()));

    }


    fun parseJSONtoElement(jsonReader: JsonReader): Element {
        val strings = arrayListOf("title", "extract", "thumbnail")
        var myElement = Element()
        var value: String = "-"
        var desc: String = "-"
        var photo: String = "-"
        jsonReader.beginObject() // Start processing the JSON object
        while (jsonReader.hasNext()) { // Loop through all keys
            val key = jsonReader.nextName() // Fetch the next key
            Log.d("JSON", key)
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
        var string = "--"
        jsonReader.beginObject() // Start processing the JSON object
        while (jsonReader.hasNext()) {
            var key = jsonReader.nextName()
            if (key == "original")
                string = jsonReader.nextString()
            else jsonReader.skipValue()
        }
        jsonReader.endObject()
        return string

    }

}
