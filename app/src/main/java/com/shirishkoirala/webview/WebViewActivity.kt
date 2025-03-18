package com.shirishkoirala.webview

import android.os.Build
import android.os.Bundle
import android.view.View
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
            var userId = extras.getString("user_id")
            var username = extras.getString("username")
            var email = extras.getString("email")

            myWebView = findViewById(R.id.myWebView)
            setupWebView(userId, username, email)

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

    private fun setupWebView(userId: String?, username: String?, email: String?) {
        myWebView.clearCache(true);
        myWebView.clearHistory();
        val webSettings = myWebView.settings
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        // Enable JavaScript (Required for React apps)
        webSettings.javaScriptEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false
        // Improve rendering performance
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)

        // Enable hardware acceleration
        webSettings.setEnableSmoothTransition(true)

        // Reduce unnecessary overdraw
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true

        // Allow mixed content for secure & non-secure connections
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // Load WebView faster
        myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        myWebView.loadUrl("http://192.168.128.52:3000/$userId/$username/$email")
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