package app.mjproductions.turboscratch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.mjproductions.turboscratch.utils.EncryptDecryptConstant
import com.google.androidbrowserhelper.trusted.TwaLauncher
import org.json.JSONObject
import java.net.URLEncoder


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
        val emailEditText = findViewById<EditText>(R.id.email_edit_text)
        val hostName = findViewById<EditText>(R.id.host_name_edit_text)

        webViewButton.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("user_id", userIdEditText.text.toString())
            intent.putExtra("name", usernameEditText.text.toString())
            intent.putExtra("email", emailEditText.text.toString())
            intent.putExtra("host_name", hostName.text.toString())
            startActivity(intent)
        }

        twaButton.setOnClickListener {
            val launcher = TwaLauncher(this)
            val jsonObject = JSONObject()
            jsonObject.put("user_id",  userIdEditText.text)
            jsonObject.put("name", usernameEditText.text)
            jsonObject.put("email", emailEditText.text)
            Log.d("jsonObject", jsonObject.toString())
            val encryptedData = EncryptDecryptConstant.encryptForPWA(jsonObject.toString())
            Log.e("encryptedData", encryptedData)
            launcher.launch(Uri.parse("${hostName.text}?authToken=${ URLEncoder.encode(encryptedData, "UTF-8")}"))
        }
    }
}