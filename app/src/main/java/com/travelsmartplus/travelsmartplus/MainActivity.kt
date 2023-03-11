package com.travelsmartplus.travelsmartplus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.windowInsetsController!!.hide(
            android.view.WindowInsets.Type.statusBars()
        )

        var isSignedIn = false

        if (isSignedIn) {
            setContentView(R.layout.activity_main)
        } else setContentView(R.layout.activity_sign_in)

    }
}