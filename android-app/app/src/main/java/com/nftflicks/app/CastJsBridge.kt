package com.nftflicks.app

import android.webkit.JavascriptInterface

/**
 * Called from the screening-room web UI when running inside the Android WebView.
 * Chromecast CAF sender does not work reliably in WebView — native Cast handles it.
 * Premium encrypted HLS uses Shaka in the WebView when live; dry/unavailable titles
 * must not silently fall back to progressive MP4.
 */
class CastJsBridge(
    private val hostAllowed: () -> Boolean = { true },
    private val onCast: (token: String, receiverUrl: String) -> Unit,
    private val notifyPremiumBlocked: (reason: String) -> Unit = {},
) {
    @JavascriptInterface
    fun isNativeCastAvailable(): Boolean =
        hostAllowed() && BuildConfig.CAST_APP_ID.isNotBlank()

    @JavascriptInterface
    fun cast(token: String, receiverUrl: String) {
        if (!hostAllowed()) return
        onCast(token.trim(), receiverUrl.trim())
    }

    /** Web screening room calls this when encrypted_hls is dry or player DRM is unavailable. */
    @JavascriptInterface
    fun onPremiumDrmUnavailable(reason: String) {
        if (!hostAllowed()) return
        notifyPremiumBlocked(reason.trim().ifEmpty { "premium_drm_unavailable" })
    }
}
