package com.bitc.android_team3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.edit
import com.bitc.android_team3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        var id = sharedPref.getString("id", "")

//        로그인 안했을 때
        if(id == null || id == ""){
            binding.btnLogout.visibility = View.GONE
//            binding.btnMyPage.visibility = View.GONE
            binding.btnLoginActivity.visibility = View.VISIBLE
        }
//        로그인 했을 때
        else {
            binding.btnLogout.visibility = View.VISIBLE
            binding.btnMyPage.visibility = View.VISIBLE
            binding.btnLoginActivity.visibility = View.GONE
        }

        binding.btnLoginActivity.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            editor.clear()
            editor.commit()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnMyPage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }
    }
}