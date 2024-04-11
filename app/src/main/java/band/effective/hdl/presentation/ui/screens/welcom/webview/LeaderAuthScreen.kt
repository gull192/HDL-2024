package band.effective.hdl.presentation.ui.screens.welcom.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import band.effective.hdl.BuildConfig
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

//TODO: Так лучше не делать, а использовать нормальную либу)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LeaderAuthScreen(callback: (String) -> Unit) {
    val redirectUri = "https://localhost"
    val url = "${BuildConfig.AUTH_SERVER_URL}?client_id=${BuildConfig.AUTH_CLIENT_ID}&redirect_uri=$redirectUri&response_type=code"
    val state = rememberWebViewState(url = url)
    val client = remember {
        object : AccompanistWebViewClient() {
            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                Log.i("WebView", url ?: "")
                if (url?.startsWith(redirectUri) == true) {
                    callback(url.substringAfter("="))
                } else {
                    super.onPageStarted(view, url, favicon)
                }
            }
        }
    }
    WebView(state = state, onCreated = { it.settings.javaScriptEnabled = true }, client = client)
}