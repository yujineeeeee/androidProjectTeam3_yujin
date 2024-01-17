package com.bitc.android_team3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bitc.android_team3.databinding.ActivityApiTestBinding

class ApiTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityApiTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var textView = binding.textView

        textView.text = ""

        binding.btnSearch.setOnClickListener {
        }

    }
}