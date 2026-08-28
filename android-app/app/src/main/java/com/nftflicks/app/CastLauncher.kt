package com.nftflicks.app

import android.content.Context
import android.widget.Toast
import androidx.mediarouter.app.MediaRouteChooserDialog
import com.google.android.gms.cast.Cast
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import org.json.JSONObject

/** Opens the Cast device picker and sends the NFT Flicks receiver payload. */
class CastLauncher(private val activityContext: Context) {

    companion object {
        const val CUSTOM_NS = "urn:x-cast:com.nftflicks.cast"
    }

    private val appContext = activityContext.applicationContext
    private var pendingToken: String? = null
    private var pendingUrl: String? = null
    private var listenerAttached = false
    private var channelReady = false

    private val noopMessageCallback =
        Cast.MessageReceivedCallback { _, _, _ -> /* receiver → sender unused */ }

    private val sessionListener =
        object : SessionManagerListener<CastSession> {
            override fun onSessionStarted(session: CastSession, sessionId: String) {
                ensureChannel(session)
                sendPayload(session)
            }

            override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
                ensureChannel(session)
                // Only resend when we have a fresh pending cast request.
                if (pendingToken != null) sendPayload(session)
            }

            override fun onSessionEnded(session: CastSession, error: Int) {
                channelReady = false
                clearChannel(session)
            }

            override fun onSessionStarting(session: CastSession) {}

            override fun onSessionStartFailed(session: CastSession, error: Int) {
                Toast.makeText(appContext, R.string.cast_failed, Toast.LENGTH_LONG).show()
            }

            override fun onSessionEnding(session: CastSession) {
                clearChannel(session)
            }

            override fun onSessionResuming(session: CastSession, sessionId: String) {}

            override fun onSessionResumeFailed(session: CastSession, error: Int) {
                Toast.makeText(appContext, R.string.cast_failed, Toast.LENGTH_LONG).show()
            }

            override fun onSessionSuspended(session: CastSession, reason: Int) {
                channelReady = false
            }
        }

    fun start(token: String, receiverUrl: String) {
        if (token.isBlank() || receiverUrl.isBlank()) {
            Toast.makeText(appContext, R.string.cast_failed, Toast.LENGTH_SHORT).show()
            return
        }
        if (BuildConfig.CAST_APP_ID.isBlank()) {
            Toast.makeText(appContext, R.string.cast_not_configured, Toast.LENGTH_LONG).show()
            return
        }
        if (!isAllowedReceiverUrl(receiverUrl)) {
            Toast.makeText(appContext, R.string.cast_failed, Toast.LENGTH_LONG).show()
            return
        }
        pendingToken = token
        pendingUrl = receiverUrl
        try {
            val castContext = CastContext.getSharedInstance(appContext)
            val sm = castContext.sessionManager
            if (!listenerAttached) {
                sm.addSessionManagerListener(sessionListener, CastSession::class.java)
                listenerAttached = true
            }
            val existing = sm.currentCastSession
            if (existing != null && existing.isConnected) {
                ensureChannel(existing)
                sendPayload(existing)
                return
            }
            val selector = castContext.mergedSelector
            if (selector == null) {
                Toast.makeText(appContext, R.string.cast_failed, Toast.LENGTH_LONG).show()
                return
            }
            val dialog = MediaRouteChooserDialog(activityContext)
            dialog.routeSelector = selector
            dialog.show()
        } catch (_: Exception) {
            Toast.makeText(appContext, R.string.cast_failed, Toast.LENGTH_LONG).show()
        }
    }

    /** Only HTTPS cast.html on exact production hosts (blocks malicious receiver URLs from XSS). */
    private fun isAllowedReceiverUrl(raw: String): Boolean {
        return try {
            val uri = android.net.Uri.parse(raw)
            if (uri.scheme?.equals("https", ignoreCase = true) != true) return false
            val host = uri.host?.lowercase() ?: return false
            val path = (uri.path ?: "").lowercase()
            if (path != "/cast.html" && !path.endsWith("/cast.html")) return false
            host == "nftflicks.com" || host == "www.nftflicks.com"
        } catch (_: Exception) {
            false
        }
    }

    fun dispose() {
        try {
            if (listenerAttached) {
                val castContext = CastContext.getSharedInstance(appContext)
                castContext.sessionManager.removeSessionManagerListener(
                    sessionListener,
                    CastSession::class.java,
                )
                listenerAttached = false
            }
        } catch (_: Exception) {
            /* ignore */
        }
        pendingToken = null
        pendingUrl = null
        channelReady = false
    }

    private fun ensureChannel(session: CastSession?) {
        if (session == null || !session.isConnected) return
        try {
            session.setMessageReceivedCallbacks(CUSTOM_NS, noopMessageCallback)
            channelReady = true
        } catch (_: Exception) {
            channelReady = false
        }
    }

    private fun clearChannel(session: CastSession?) {
        try {
            session?.removeMessageReceivedCallbacks(CUSTOM_NS)
        } catch (_: Exception) {
            /* ignore */
        }
        channelReady = false
    }

    private fun sendPayload(session: CastSession?) {
        val token = pendingToken ?: return
        val url = pendingUrl ?: return
        if (session == null || !session.isConnected) return
        if (!channelReady) ensureChannel(session)
        try {
            val payload =
                JSONObject()
                    .put("token", token)
                    .put("url", url)
                    .toString()
            session.sendMessage(CUSTOM_NS, payload).setResultCallback { status ->
                if (status.isSuccess) {
                    Toast.makeText(appContext, R.string.cast_started, Toast.LENGTH_SHORT).show()
                    // Clear so resume doesn't replay an old title.
                    pendingToken = null
                    pendingUrl = null
                } else {
                    Toast.makeText(appContext, R.string.cast_failed, Toast.LENGTH_LONG).show()
                }
            }
        } catch (_: Exception) {
            Toast.makeText(appContext, R.string.cast_failed, Toast.LENGTH_LONG).show()
        }
    }
}
