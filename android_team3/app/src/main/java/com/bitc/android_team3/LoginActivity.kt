package com.bitc.android_team3

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bitc.android_team3.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper(this)
        var database = dbHelper.writableDatabase

        val id = binding.etId
        val pw = binding.etPw

        binding.btnLogin.setOnClickListener {
            val result = dbHelper.userLogin(database, id.text.toString(), pw.text.toString())

            if(result == 1){
                val userInfo = dbHelper.userInfo(database, id.text.toString())

                val sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE)
                sharedPref.edit().run {
                    putString("id", userInfo.id)
                    putString("name", userInfo.name)
                    putString("email", userInfo?.email)
                    putString("phone", userInfo?.phone)
                    commit()
                }


                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnJoinActivity.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

    }
}