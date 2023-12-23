package com.smilegate.url_shortener

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.smilegate.url_shortener.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /* Status Bar & Navigation Bar */
        val barColor = ContextCompat.getColor(this, R.color.mainColor)
        with(window) {
            statusBarColor = barColor
        }
        with(WindowInsetsControllerCompat(window, window.decorView)) {
            isAppearanceLightStatusBars = true
        }

        // 스플래시 화면 디스플레이 시간 조절 (예: 2000 밀리초, 즉 2초)
        setInitialize(1500)
    }

    private fun setInitialize(delayMillis: Long) {
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, delayMillis)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
    }

    // ...
}