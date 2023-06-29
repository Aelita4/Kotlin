package pl.mikorosa.webco

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import okhttp3.*
import java.io.IOException
import com.google.gson.Gson
import com.google.gson.JsonObject

class MainActivity : AppCompatActivity() {

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tables)

        val db = DatabaseConnector(this, null)
        val dataFromDb = db.getAll()
        if(dataFromDb.size == 0) {
            val button: Button = findViewById(R.id.loginButton)

            button.setOnClickListener {
                val usernameField: EditText = findViewById(R.id.editTextTextPersonName2)
                val passwordField: EditText = findViewById(R.id.editTextTextPassword)
                val error: TextView = findViewById(R.id.textView9)

                val username = usernameField.text.toString()
                val password = passwordField.text.toString()

                val loginAnswer = APIManager.login(username, password)

                val parsedObject = Gson().fromJson(loginAnswer, JsonObject::class.java)
                val code = parsedObject.get("code").asString

                if(code != "200") {
                    error.visibility = View.VISIBLE
                } else {
                    error.visibility = View.INVISIBLE
                    val accessToken = parsedObject.get("accessToken").asString
                    val username = parsedObject.get("username").asString

                    db.addAccessToken(accessToken, username)

                    val intent = Intent(this@MainActivity, OverviewActivity::class.java)
                    startActivity(intent)
                }
            }
        } else {
            val intent = Intent(this@MainActivity, OverviewActivity::class.java)
            startActivity(intent)
        }

        db.close()
    }
}