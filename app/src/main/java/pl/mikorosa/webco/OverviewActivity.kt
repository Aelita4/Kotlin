package pl.mikorosa.webco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.*

class OverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        val db = DatabaseConnector(this, null)

        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            db.removeAccessToken()
            val intent = Intent(this@OverviewActivity, MainActivity::class.java)
            startActivity(intent)
        }

        val dataFromDb = db.getAll()[0]

        val body = APIManager.fetch("https://col.ael.ovh/api/getPlanets", "GET", dataFromDb.token, null)
        val parsedObject = Gson().fromJson(body, JsonObject::class.java)
        val planets = parsedObject.get("planets").asJsonArray

        var name = ""
        var owner = ""

        val recyclerview = findViewById<RecyclerView>(R.id.galaxyView)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val data = ArrayList<GalaxyViewModel>()

        var i = 1

        for(planet in planets) {
            if(planet.asJsonObject.get("owner").asJsonObject.get("tmp").asString == dataFromDb.token) {
                name = planet.asJsonObject.get("name").asString
                owner = planet.asJsonObject.get("owner").asJsonObject.get("username").asString
            }

            data.add(GalaxyViewModel(i++, planet.asJsonObject.get("name").asString, planet.asJsonObject.get("owner").asJsonObject.get("username").asString))
        }

        val greeting: TextView = findViewById(R.id.textGreeting)
        greeting.text = "Hello " + owner + ", you are on " + name

        val resources = APIManager.getOwnResources(dataFromDb.token)

        val coalAmount: TextView = findViewById(R.id.coalAmount)
        val copperAmount: TextView = findViewById(R.id.copperAmount)
        val goldAmount: TextView = findViewById(R.id.goldAmount)
        val ironAmount: TextView = findViewById(R.id.ironAmount)
        val oilAmount: TextView = findViewById(R.id.oilAmount)
        val uraniumAmount: TextView = findViewById(R.id.uraniumAmount)
        val woodAmount: TextView = findViewById(R.id.woodAmount)

        coalAmount.text = resources.get("coal").asInt.toString()
        copperAmount.text = resources.get("copper").asInt.toString()
        goldAmount.text = resources.get("gold").asInt.toString()
        ironAmount.text = resources.get("iron").asInt.toString()
        oilAmount.text = resources.get("oil").asInt.toString()
        uraniumAmount.text = resources.get("uranium").asInt.toString()
        woodAmount.text = resources.get("wood").asInt.toString()

        val rates = APIManager.getDefaultMiningRates()
        Timer().scheduleAtFixedRate( object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    coalAmount.text = ((coalAmount.text as String).toInt() + rates.get("coal").asInt).toString()
                    copperAmount.text = ((copperAmount.text as String).toInt() + rates.get("copper").asInt).toString()
                    goldAmount.text = ((goldAmount.text as String).toInt() + rates.get("gold").asInt).toString()
                    ironAmount.text = ((ironAmount.text as String).toInt() + rates.get("iron").asInt).toString()
                    oilAmount.text = ((oilAmount.text as String).toInt() + rates.get("oil").asInt).toString()
                    uraniumAmount.text = ((uraniumAmount.text as String).toInt() + rates.get("uranium").asInt).toString()
                    woodAmount.text = ((woodAmount.text as String).toInt() + rates.get("wood").asInt).toString()
                }
            }
        }, 0, 1000)

        val adapter = CustomAdapter(data)
        recyclerview.adapter = adapter
    }
}