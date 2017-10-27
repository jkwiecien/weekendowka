package net.techbrewery.weekendowka.print

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_print.*
import net.techbrewery.weekendowka.R
import net.techbrewery.weekendowka.base.view.BaseActivity
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException


/**
 * Created by Jacek Kwiecień on 27.10.2017.
 */
class PrintActivity : BaseActivity() {

    private val PRINT_DIALOG_URL = "https://www.google.com/cloudprint/dialog.html"
    private val JS_INTERFACE = "AndroidPrintDialog"
    private val CONTENT_TRANSFER_ENCODING = "base64"

    private val ZXING_URL = "http://zxing.appspot.com"
    private val ZXING_SCAN_REQUEST = 65743

    private val CLOSE_POST_MESSAGE_NAME = "cp-dialog-on-close"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print)

        val cloudPrintIntent = this.intent

        val settings = webViewAtPrintActivity.settings
        settings.javaScriptEnabled = true

        webViewAtPrintActivity.webViewClient = PrintDialogWebClient()
        webViewAtPrintActivity.addJavascriptInterface(PrintDialogJavaScriptInterface(), JS_INTERFACE)
        webViewAtPrintActivity.loadUrl(PRINT_DIALOG_URL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        webViewAtPrintActivity.loadUrl(intent.getStringExtra("SCAN_RESULT"));
    }

    internal inner class PrintDialogJavaScriptInterface {
        val type: String
            get() = intent.type

        val title: String
            get() = intent.extras.getString("title")

        val content: String
            get() {
                try {
                    val contentResolver = contentResolver
                    val `is` = contentResolver.openInputStream(intent.data)
                    val baos = ByteArrayOutputStream()

                    val buffer = ByteArray(4096)
                    var n = `is`!!.read(buffer)
                    while (n >= 0) {
                        baos.write(buffer, 0, n)
                        n = `is`.read(buffer)
                    }
                    `is`.close()
                    baos.flush()

                    return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return ""
            }

        val encoding: String
            get() = CONTENT_TRANSFER_ENCODING

        fun onPostMessage(message: String) {
            if (message.startsWith(CLOSE_POST_MESSAGE_NAME)) {
                finish()
            }
        }
    }

    private inner class PrintDialogWebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(ZXING_URL)) {
                val intentScan = Intent("com.google.zxing.client.android.SCAN")
                intentScan.putExtra("SCAN_MODE", "QR_CODE_MODE")
                try {
                    startActivityForResult(intentScan, ZXING_SCAN_REQUEST)
                } catch (error: ActivityNotFoundException) {
                    view.loadUrl(url)
                }

            } else {
                view.loadUrl(url)
            }
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            if (PRINT_DIALOG_URL == url) {
                // Submit print document.
                view.loadUrl("javascript:printDialog.setPrintDocument(printDialog.createPrintDocument("
                        + "window." + JS_INTERFACE + ".getType(),window." + JS_INTERFACE + ".getTitle(),"
                        + "window." + JS_INTERFACE + ".getContent(),window." + JS_INTERFACE + ".getEncoding()))")

                // Add post messages listener.
                view.loadUrl("javascript:window.addEventListener('message',"
                        + "function(evt){window." + JS_INTERFACE + ".onPostMessage(evt.data)}, false)")
            }
        }
    }
}