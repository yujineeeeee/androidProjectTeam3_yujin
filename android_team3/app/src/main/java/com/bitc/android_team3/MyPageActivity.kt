package com.bitc.android_team3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bitc.android_team3.databinding.ActivityMyPageBinding

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        var id = sharedPref.getString("id", "")
        var name = sharedPref.getString("name", "")
        var email = sharedPref.getString("email", "")
        var phone = sharedPref.getString("phone", "")
        var createDate = sharedPref.getString("createDate", "")

        Log.d("api-data", "id: $id, name: $name, email: $email, phone: $phone, createDate: $createDate")

//        로그인 하지 않았을 때
        if(id == null || id == ""){
            binding.linearNoUser.visibility = View.VISIBLE
            binding.linearUser.visibility = View.GONE

//            회원가입
            binding.btnMyPageJoin.setOnClickListener {
                val intent = Intent(this, JoinActivity::class.java)
                startActivity(intent)
            }

//            로그인
            binding.btnMyPageLogin.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
//        로그인 했을 때
        else {
            binding.linearNoUser.visibility = View.GONE
            binding.linearUser.visibility = View.VISIBLE

            binding.tvMyPageId.text = id
            binding.tvMyPageCreateDate.text = "가입일 : $createDate"

//            로그아웃
            binding.btnMyPageLogout.setOnClickListener {
                editor.clear()
                editor.commit()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            binding.btnUserUpdate.setOnClickListener {
                UserInfoUpdateDialog().show(supportFragmentManager, "dialog")
            }

        }


    }
}