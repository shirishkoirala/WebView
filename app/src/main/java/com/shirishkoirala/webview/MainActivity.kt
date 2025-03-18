package com.shirishkoirala.webview

import android.R.attr.value
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.androidbrowserhelper.trusted.TwaLauncher


class MainActivity : AppCompatActivity() {
    private lateinit var webViewButton: Button
    private lateinit var twaButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        webViewButton = findViewById(R.id.web_view_button)
        twaButton = findViewById(R.id.twa_button)
        val userIdEditText = findViewById<EditText>(R.id.user_id_edit_text)
        val usernameEditText = findViewById<EditText>(R.id.username_edit_text)
        val emailEditText = findViewById<EditText>(R.id.emailÏ_edit_text)

        webViewButton.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("user_id", userIdEditText.text.toString())
            intent.putExtra("username", usernameEditText.text.toString())
            intent.putExtra("email", emailEditText.text.toString())
            startActivity(intent)
        }

        twaButton.setOnClickListener {
            val launcher = TwaLauncher(this)
            launcher.launch(Uri.parse("http://192.168.128.52:3000/${userIdEditText.text}/${usernameEditText.text}/${emailEditText.text}"))
        }
    }
}