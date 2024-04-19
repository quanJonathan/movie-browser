package com.hitachivantara.mobilecoe.android.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.hitachivantara.mobilecoe.android.ui.MainActivity
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.preferences.PreferencesStore
import com.hitachivantara.mobilecoe.android.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    private val userId by lazy { PreferencesStore.getInstance(this).getLoginUserId() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val handler = Handler(mainLooper)
        handler.postDelayed({
            if (userId == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }, 2000)
    }
}