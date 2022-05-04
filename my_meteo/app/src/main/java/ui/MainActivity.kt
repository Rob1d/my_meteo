package ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.my_meteo.NewActivity
import com.example.my_meteo.R
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser


class MainActivity : AppCompatActivity() {
    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var list: ArrayList<String>
    lateinit var adapter: ArrayAdapter<*>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val csvParser = CSVParser(applicationContext.assets.open("villes_france.csv").bufferedReader(), CSVFormat.DEFAULT)
        searchView = findViewById(R.id.searchView)
        listView = findViewById(R.id.listView)
        list = ArrayList()
        for (csvRecord in csvParser) {
            list.add(csvRecord.get(3).toString().lowercase().capitalize())
        }
        list.sort()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (list.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(this@MainActivity, "Pas de villes avec ce nom", Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, NewActivity::class.java)
            intent.putExtra("Town", listView.getItemAtPosition(position).toString())
            startActivity(intent)
        }
    }
}