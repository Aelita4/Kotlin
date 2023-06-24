package pl.mikorosa.webco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.loginButton)

        button.setOnClickListener {
            val usernameField: EditText = findViewById(R.id.fieldUsername)
            val passwordField: EditText = findViewById(R.id.fieldPassword)

            val username = usernameField.getText().toString()
            val password = passwordField.getText().toString()

            
        }

    }
}