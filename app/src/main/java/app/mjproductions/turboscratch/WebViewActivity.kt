package app.mjproductions.turboscratch

import android.graphics.Color
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WebViewActivity : AppCompatActivity() {
    private lateinit var myWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_webview)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val extras = intent.extras
        if (extras != null) {
            val userId = extras.getString("user_id")
            val username = extras.getString("name")
            val email = extras.getString("email")
            val hostName = extras.getString("host_name")

            myWebView = findViewById(R.id.myWebView)
            setupWebView(hostName, userId, username, email)

            onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (myWebView.canGoBack()) {
                            myWebView.goBack()
                        } else {
                            onBackPressedDispatcher.onBackPressed()
                        }
                    }
                })
        }
    }

    private fun setupWebView(
        hostName: String?,
        userId: String?,
        username: String?,
        email: String?
    ) {
        myWebView.clearCache(true);
        myWebView.clearHistory();
        myWebView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        // Enable JavaScript (Required for React apps)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.mediaPlaybackRequiresUserGesture = false
        myWebView.settings.domStorageEnabled = true

        myWebView.settings.useWideViewPort = true
        myWebView.settings.loadWithOverviewMode = true
        myWebView.settings.allowFileAccess = true
        myWebView.settings.allowContentAccess = true
        myWebView.settings.setSupportMultipleWindows(true)
        myWebView.settings.loadsImagesAutomatically = true
        myWebView.setBackgroundColor(Color.TRANSPARENT)
        myWebView.webChromeClient = WebChromeClient()
        myWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        myWebView.loadUrl("$hostName/$userId/$username/$email")
    }

    override fun onDestroy() {
        if (myWebView != null) {
            myWebView.loadUrl("about:blank")
            myWebView.clearCache(true)
            myWebView.clearHistory()
            myWebView.removeAllViews()
            myWebView.destroy()
        }
        super.onDestroy()
    }
}