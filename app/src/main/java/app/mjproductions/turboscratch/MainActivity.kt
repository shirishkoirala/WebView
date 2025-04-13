package app.mjproductions.turboscratch
import android.widget.Toast

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.mjproductions.turboscratch.databinding.ActivityMainBinding
import app.mjproductions.turboscratch.utils.EncryptDecryptConstant
import com.google.androidbrowserhelper.trusted.TwaLauncher
import org.json.JSONObject
import java.net.URLEncoder


class MainActivity : AppCompatActivity() {
    private var url: String = "https://feature-staging.d1izslsso66vbm.amplifyapp.com/"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences("userData", MODE_PRIVATE)
        binding.userIdEditText.setText(sharedPrefs.getString("userId", ""))
        binding.usernameEditText.setText(sharedPrefs.getString("username", ""))
        binding.emailEditText.setText(sharedPrefs.getString("email", ""))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnStartPlaying.root.setOnClickListener {
            openTwa()
        }

    }

   private fun openTwa() {
        if (binding.userIdEditText.text.toString().trim().isEmpty() ||
            binding.usernameEditText.text.toString().trim().isEmpty() ||
            binding.emailEditText.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        val launcher = TwaLauncher(this)
        val jsonObject = JSONObject()
        jsonObject.put("user_id", binding.userIdEditText.text)
        jsonObject.put("name", binding.usernameEditText.text)
        jsonObject.put("email", binding.emailEditText.text)
        Log.d("jsonObject", jsonObject.toString())
        val encryptedData = EncryptDecryptConstant.encryptForPWA(jsonObject.toString())
        Log.e("encryptedData", encryptedData)
        launcher.launch(
            Uri.parse(
                "${url}?authToken=${
                    URLEncoder.encode(
                        encryptedData,
                        "UTF-8"
                    )
                }"
            )
        )
    }

    private fun openWebView() {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("user_id", binding.userIdEditText.text.toString())
        intent.putExtra("name", binding.usernameEditText.text.toString())
        intent.putExtra("email", binding.emailEditText.text.toString())
        intent.putExtra("host_name", url)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        val sharedPrefs = getSharedPreferences("userData", MODE_PRIVATE)
        sharedPrefs.edit()
            .putString("userId", binding.userIdEditText.text.toString())
            .putString("username", binding.usernameEditText.text.toString())
            .putString("email", binding.emailEditText.text.toString())
            .apply()
    }
}