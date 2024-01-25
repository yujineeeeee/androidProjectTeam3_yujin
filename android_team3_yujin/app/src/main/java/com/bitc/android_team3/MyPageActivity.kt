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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
//        로그인 했을 때
        else {
            binding.tvMyPageId.text = "${name} 님"
            binding.tvMyPageCreateDate.text = "가입일 : $createDate"

//            로그아웃
            binding.tvLogout.setOnClickListener {
                editor.clear()
                editor.commit()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }

//        회원정보 수정
        binding.llUserUpdate.setOnClickListener {
            UserInfoUpdateDialog().show(supportFragmentManager, "dialog")
        }

    }
}