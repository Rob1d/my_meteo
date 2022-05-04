package com.example.my_meteo

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import data.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener



class NewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity)
        val Townname = this.intent.getStringExtra("Town")
        val Tview: TextView = this.findViewById(R.id.town_name)
        Tview.text = Townname
        val url = "https://api.openweathermap.org/data/2.5/weather?q=" + Townname +
                ",fr&APPID=8aa161a17d513c5b9d4b033e8e79240e&mode=json&units=metric&lang=fr"
        coroutine(url)
    }

    fun set_background(str : String)
    {
        val Layout = findViewById(R.id.global_town) as LinearLayout
        var check = false
        if (str == "ciel dégagé" || str == "soleil") {
            Layout.setBackgroundResource(R.drawable.sun)
            check = true
        }
        if (str == "légère pluie" || str == "pluie") {
            Layout.setBackgroundResource(R.drawable.rain)
            check = true
        }
        if (!check) {
            Layout.setBackgroundResource(R.drawable.clouds)
        }
    }
    fun update_text(str : String, View :TextView, unit : String)
    {
        val add = str + unit
        View.text = add
    }
    fun update_weather(str : String)
    {
        val Tview: TextView = this.findViewById(R.id.actual_weather)
        val jsonObject = JSONTokener(str).nextValue() as JSONObject
        val weather = jsonObject.getString("weather")
        val parsed_weather = JSONTokener(weather).nextValue() as JSONArray
        val main = jsonObject.getString("main")
        val temp = JSONTokener(main).nextValue() as JSONObject
        Tview.text = parsed_weather.getJSONObject(0).getString("description").capitalize()
        update_text(temp.getString("temp"), this.findViewById(R.id.temperture), "°C")
        set_background(parsed_weather.getJSONObject(0).getString("description"))
        update_text(temp.getString("feels_like"), this.findViewById(R.id.ressenti), "°C")
        update_text(temp.getString("pressure"), this.findViewById(R.id.pressure), " hPa")
        update_text(temp.getString("temp_max"), this.findViewById(R.id.max), "°C")
        update_text(temp.getString("temp_min"), this.findViewById(R.id.min), "°C")
        update_text(temp.getString("humidity"), this.findViewById(R.id.humidity), "%")
    }

    fun coroutine(url : String)
    {
        var result_json = ""
        GlobalScope.launch(Dispatchers.Default) {
            result_json = Request(url).run()
            launch(Dispatchers.Main) {
                if (result_json != "")
                    update_weather(result_json)
            }
        }
    }


}
