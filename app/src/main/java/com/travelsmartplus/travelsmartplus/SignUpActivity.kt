package com.travelsmartplus.travelsmartplus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.travelsmartplus.travelsmartplus.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_sign_up)
    }
}