package com.consica.code.domain

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient

class HtmlRenderer(private val context: Context) {
    private var webView: WebView? = null

    data class HtmlResult(
        val renderedHtml: String,
        val sanitizedHtml: String,
        val error: String? = null
    )

    fun wrapHtml(code: String): String = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            body {
                font-family: 'Nunito', system-ui, -apple-system, sans-serif;
                background: #E8F5E9;
                color: #1A1A1A;
                padding: 16px;
                margin: 0;
                line-height: 1.5;
            }
            h1 { color: #2D5A27; font-size: 2em; }
            h2 { color: #4A7A43; font-size: 1.5em; }
            h3 { color: #558B2F; font-size: 1.25em; }
            p { color: #333; }
            a { color: #4DA8DA; }
            code, pre {
                background: #1E1E1E;
                color: #E0E0E0;
                padding: 8px 12px;
                border-radius: 8px;
                font-family: 'Fira Code', monospace;
                overflow-x: auto;
            }
            img { max-width: 100%; height: auto; border-radius: 12px; }
            ul, ol { padding-left: 24px; }
            button {
                background: #2D5A27; color: white;
                border: none; padding: 10px 20px;
                border-radius: 50px; font-size: 16px;
                cursor: pointer;
            }
        </style>
    </head>
    <body>
        $code
    </body>
    </html>
    """.trimIndent()

    fun sanitizeHtml(html: String): String {
        return html
            .replace(Regex("<script[\\s\\S]*?</script>", RegexOption.IGNORE_CASE), "")
            .replace(Regex("on\\w+\\s*=\\s*\"[^\"]*\"", RegexOption.IGNORE_CASE), "")
            .replace(Regex("on\\w+\\s*=\\s*'[^']*'", RegexOption.IGNORE_CASE), "")
            .replace(Regex("javascript:", RegexOption.IGNORE_CASE), "")
    }

    fun renderHtml(code: String): HtmlResult {
        val sanitized = sanitizeHtml(code)
        val wrapped = wrapHtml(sanitized)
        return HtmlResult(renderedHtml = wrapped, sanitizedHtml = sanitized)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun createPreviewWebView(): WebView {
        val wv = WebView(context).apply {
            settings.javaScriptEnabled = false
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            setBackgroundColor(0xFFE8F5E9.toInt())
            webViewClient = WebViewClient()
        }
        webView = wv
        return wv
    }

    fun loadHtml(webView: WebView, code: String) {
        val rendered = renderHtml(code)
        webView.loadDataWithBaseURL(
            null,
            rendered.renderedHtml,
            "text/html",
            "UTF-8",
            null
        )
    }

    fun release() {
        webView?.destroy()
        webView = null
    }
}
