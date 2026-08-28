package com.nftflicks.app

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.net.http.SslError
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.WindowCompat
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature

/**
 * Secure shell for the NFT Flicks website.
 * - HTTPS only (cleartext disabled in manifest + network security config)
 * - Navigation locked to allowlisted hosts (exact + narrow suffixes)
 * - Google OAuth opens in Chrome Custom Tabs
 * - Buy/sell/Stripe checkout open in the system browser (Play digital-goods policy)
 * - FLAG_SECURE on release builds (blocks screenshots of session UI)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progress: ProgressBar
    private lateinit var castLauncher: CastLauncher

    private val allowedHosts = setOf(
        "nftflicks.com",
        "www.nftflicks.com",
        "accounts.google.com",
        "oauth2.googleapis.com",
        "checkout.stripe.com",
        "js.stripe.com",
        "api.stripe.com",
        "m.stripe.com",
        "m.stripe.network",
        // Allow YouTube embed CDN hosts only — APK never downloads/proxies movie files.
        "www.youtube.com",
        "youtube.com",
        "www.youtube-nocookie.com",
        "s.ytimg.com",
        "i.ytimg.com",
        "cdn.jsdelivr.net",
        "unpkg.com",
        "cdnjs.cloudflare.com",
        "challenges.cloudflare.com",
        "fonts.googleapis.com",
        "fonts.gstatic.com",
        "www.gstatic.com",
        "cast.google.com",
        "all-access.wax.io",
        "wax.greymass.com",
        "wax.api.atomicassets.io",
        "api.atomicassets.io",
    )

    private val allowedHostSuffixes = listOf(
        ".stripe.com",
        ".stripe.network",
        ".youtube.com",
        ".ytimg.com",
        ".googlevideo.com",
        ".wax.io",
        ".challenges.cloudflare.com",
        ".cloudflareinsights.com",
    )

    /** OAuth stays in Custom Tabs. */
    private val oauthExternalHosts = setOf(
        "accounts.google.com",
        "oauth2.googleapis.com",
    )

    /** Commerce must leave the app (system browser) — not Custom Tabs. */
    private val commerceExternalHosts = setOf(
        "checkout.stripe.com",
        "billing.stripe.com",
        "pay.stripe.com",
        "js.stripe.com",
        "api.stripe.com",
        "m.stripe.com",
        "m.stripe.network",
    )

    private val commerceHostSuffixes = listOf(
        ".stripe.com",
        ".stripe.network",
    )

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE,
            )
        }
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        progress = findViewById(R.id.progress)
        castLauncher = CastLauncher(this)

        configureWebView()
        bindBack()
        findViewById<TextView>(R.id.legalBtn).setOnClickListener { showLegalMenu() }

        val startUrl = resolveIncomingUrl(intent) ?: BuildConfig.SITE_URL
        webView.loadUrl(startUrl)
    }

    private fun sitePage(path: String): String {
        val base = BuildConfig.SITE_URL.trimEnd('/')
        return "$base/${path.trimStart('/')}"
    }

    private fun showLegalMenu() {
        val labels =
            arrayOf(
                getString(R.string.menu_terms),
                getString(R.string.menu_privacy),
                getString(R.string.menu_cookies),
                getString(R.string.menu_risks),
                getString(R.string.menu_refunds),
                getString(R.string.menu_dmca),
                getString(R.string.menu_marketplace),
                getString(R.string.menu_producer_license),
                getString(R.string.menu_delete_account),
            )
        val paths =
            arrayOf(
                "terms.html",
                "privacy.html",
                "cookie-policy.html",
                "risk-disclosures.html",
                "refund-policy.html",
                "dmca.html",
                "marketplace-terms.html",
                "producer-license.html",
                "delete-account.html",
            )
        AlertDialog.Builder(this)
            .setTitle(R.string.legal_dialog_title)
            .setMessage(R.string.legal_dialog_message)
            .setItems(labels) { _, which ->
                if (which in paths.indices) {
                    webView.loadUrl(sitePage(paths[which]))
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        resolveIncomingUrl(intent)?.let { webView.loadUrl(it) }
    }

    /** Map nftflicks://oauth?... → https site URL with the same query (login handoff). */
    private fun resolveIncomingUrl(intent: Intent?): String? {
        val uri = intent?.data ?: return null
        if (uri.scheme.equals("nftflicks", true) && uri.host.equals("oauth", true)) {
            val site = Uri.parse(BuildConfig.SITE_URL).buildUpon()
            uri.queryParameterNames.forEach { key ->
                site.appendQueryParameter(key, uri.getQueryParameter(key))
            }
            return site.build().toString()
        }
        val url = uri.toString()
        return url.takeIf { isAllowedUrl(it) }
    }

    private fun configureWebView() {
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        val s = webView.settings
        s.javaScriptEnabled = true
        s.domStorageEnabled = true
        s.cacheMode = WebSettings.LOAD_DEFAULT
        s.mediaPlaybackRequiresUserGesture = false
        s.allowFileAccess = false
        s.allowContentAccess = false
        s.setSupportMultipleWindows(false)
        s.javaScriptCanOpenWindowsAutomatically = false
        s.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        s.builtInZoomControls = false
        s.displayZoomControls = false
        s.useWideViewPort = true
        s.loadWithOverviewMode = true
        s.userAgentString = s.userAgentString + " NFTFlicksApp/1.0"

        if (WebViewFeature.isFeatureSupported(WebViewFeature.SAFE_BROWSING_ENABLE)) {
            WebSettingsCompat.setSafeBrowsingEnabled(s, true)
        }

        @SuppressLint("JavascriptInterface")
        webView.addJavascriptInterface(
            CastJsBridge(
                onCast = { token, receiverUrl ->
                    runOnUiThread { castLauncher.start(token, receiverUrl) }
                },
                notifyPremiumBlocked = { reason ->
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Premium DRM not available on this build ($reason)",
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                },
            ),
            "NftFlicksAndroid",
        )

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress.visibility = if (newProgress in 1..99) View.VISIBLE else View.GONE
                progress.progress = newProgress
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                when (externalNavMode(request.url)) {
                    ExternalNav.OAUTH_CUSTOM_TAB -> {
                        openCustomTab(url)
                        return true
                    }
                    ExternalNav.COMMERCE_SYSTEM_BROWSER -> {
                        openSystemBrowser(url)
                        return true
                    }
                    ExternalNav.NONE -> { /* fall through */ }
                }
                if (isAllowedUrl(url)) return false
                return when {
                    url.startsWith("mailto:", true) || url.startsWith("tel:", true) -> {
                        runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
                        true
                    }
                    else -> {
                        Toast.makeText(this@MainActivity, R.string.blocked_navigation, Toast.LENGTH_SHORT).show()
                        true
                    }
                }
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.cancel()
                Toast.makeText(this@MainActivity, R.string.ssl_error, Toast.LENGTH_LONG).show()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?,
            ) {
                if (request?.isForMainFrame == true) {
                    Toast.makeText(this@MainActivity, R.string.load_error, Toast.LENGTH_LONG).show()
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progress.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progress.visibility = View.GONE
                CookieManager.getInstance().flush()
            }
        }
    }

    private enum class ExternalNav {
        NONE,
        OAUTH_CUSTOM_TAB,
        COMMERCE_SYSTEM_BROWSER,
    }

    private fun externalNavMode(uri: Uri): ExternalNav {
        val host = uri.host?.lowercase() ?: return ExternalNav.NONE
        if (host in oauthExternalHosts) return ExternalNav.OAUTH_CUSTOM_TAB
        if (host in commerceExternalHosts || commerceHostSuffixes.any { host.endsWith(it) }) {
            return ExternalNav.COMMERCE_SYSTEM_BROWSER
        }
        val site = BuildConfig.SITE_HOST.lowercase()
        if (host == site || host == "www.nftflicks.com") {
            val path = (uri.path ?: "").lowercase()
            val frag = (uri.fragment ?: "").lowercase()
            if (
                path.contains("market") ||
                path.contains("checkout") ||
                path.contains("filmmakers") ||
                path.contains("producer.html") ||
                path.contains("/producer/") ||
                frag.startsWith("buy-")
            ) {
                return ExternalNav.COMMERCE_SYSTEM_BROWSER
            }
        }
        return ExternalNav.NONE
    }

    private fun openSystemBrowser(url: String) {
        runCatching {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            )
        }.onFailure {
            Toast.makeText(this, R.string.buy_on_website, Toast.LENGTH_LONG).show()
        }
    }

    private fun openCustomTab(url: String) {
        try {
            val tabs = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setUrlBarHidingEnabled(true)
                .build()
            tabs.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            tabs.launchUrl(this, Uri.parse(url))
        } catch (_: Exception) {
            openSystemBrowser(url)
        }
    }

    private fun isAllowedUrl(url: String): Boolean {
        val uri = runCatching { Uri.parse(url) }.getOrNull() ?: return false
        val scheme = uri.scheme?.lowercase() ?: return false
        if (scheme != "https") return false
        val host = uri.host?.lowercase() ?: return false
        if (host in allowedHosts) return true
        return allowedHostSuffixes.any { host.endsWith(it) }
    }

    private fun bindBack() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) webView.goBack() else finish()
                }
            },
        )
    }

    override fun onPause() {
        webView.onPause()
        CookieManager.getInstance().flush()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
        // After OAuth / website checkout, refresh entitlements / login state.
        if (::webView.isInitialized) {
            webView.evaluateJavascript(
                "(function(){try{if(window.NftFlicks && typeof window.NftFlicks.reloadSession==='function'){window.NftFlicks.reloadSession();}else if(typeof loadMe==='function'){loadMe();}}catch(e){}})();",
                null,
            )
        }
    }

    override fun onDestroy() {
        if (::castLauncher.isInitialized) castLauncher.dispose()
        webView.destroy()
        super.onDestroy()
    }
}
