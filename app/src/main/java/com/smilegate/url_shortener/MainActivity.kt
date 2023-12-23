package com.smilegate.url_shortener

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.smilegate.url_shortener.databinding.ActivityMainBinding
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShorten.setOnClickListener {
            val originalUrl = binding.etOriginalUrl.text.toString()
            shortenUrl(originalUrl)
        }

//        binding.btnExpand.setOnClickListener {
//            val shortenedUrl = binding.etShortenedUrl.text.toString()
//            expandUrl(
//                shortenedUrl,
//                onSuccess = { expandedUrl ->
//                    binding.tvExpandedUrl.text = expandedUrl
//                },
//                onError = {
//                    Log.e("ExpandUrl", "Error expanding URL")
//                }
//            )
//        }

    }

    private fun shortenUrl(originalUrl: String) {
        val encodedUrl = try {
            URLEncoder.encode(originalUrl, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            // Handle encoding error
            return
        }

        val apiUrl = "https://openapi.naver.com/v1/util/shorturl?url=$encodedUrl"

        val request = object : JsonObjectRequest(
            Request.Method.GET, apiUrl, null,
            Response.Listener { response ->
                val resultUrl = response.getJSONObject("result").getString("url")
                binding.tvShortenedUrl.text = resultUrl
                copyToClipboard(resultUrl)
                shareUrl(resultUrl)
            },
            Response.ErrorListener { error ->
                Log.e("ShortenUrl", "Error shortening URL: $error")
            }

        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["X-Naver-Client-Id"] = NaverConsts.CLIENT_ID
                headers["X-Naver-Client-Secret"] = NaverConsts.CLIENT_SECRET
                return headers
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun copyToClipboard(text: String) {
        Log.d("ShortenUrl", "Copying to clipboard: $text")
        val clipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Shortened URL", text)
        clipboardManager.setPrimaryClip(clipData)
        // Notify the user that the URL has been copied if needed
    }

    private fun shareUrl(url: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

//    private fun expandUrl(shortenedUrl: String, onSuccess: (String) -> Unit, onError: () -> Unit) {
//        val decodeUrl = try {
//            URLDecoder.decode(shortenedUrl, "UTF-8")
//        } catch (e: UnsupportedEncodingException) {
//            // Handle encoding error
//            return
//        }
//
//        val apiUrl = "https://openapi.naver.com/v1/util/shorturl?url=$shortenedUrl"
//
//        val request = object : JsonObjectRequest(
//            Request.Method.GET, apiUrl, null,
//            Response.Listener { response ->
//                val resultUrl = response.getJSONObject("result").getString("url")
//                onSuccess.invoke(resultUrl)
//            },
//            Response.ErrorListener {
//                onError.invoke()
//            }
//        ) {
//            override fun getHeaders(): MutableMap<String, String> {
//                val headers = HashMap<String, String>()
//                headers["X-Naver-Client-Id"] = NaverConsts.CLIENT_ID
//                headers["X-Naver-Client-Secret"] = NaverConsts.CLIENT_SECRET
//                return headers
//            }
//        }
//
//        Volley.newRequestQueue(this).add(request)
//    }
}
