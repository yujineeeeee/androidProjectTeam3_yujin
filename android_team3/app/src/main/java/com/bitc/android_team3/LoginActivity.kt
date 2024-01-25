package com.bitc.android_team3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bitc.android_team3.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = binding.etId
        val pw = binding.etPw

        var loginResult: Int? = null

        binding.btnLogin.setOnClickListener {

            if ((id.text.toString() == "" || id.text.toString() == null) || (pw.text.toString() == "" || pw.text.toString() == null)){
                Toast.makeText(this, "로그인 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                val userLoginData: UserLoginData = UserLoginData(id = id.text.toString(), pw = pw.text.toString())

//            로그인 성공 확인
                RetrofitBuilder.api.userLogin(userLoginData).enqueue(object : Callback<Int> {
                    override fun onResponse(call: Call<Int>, response: Response<Int>) {
                        loginResult = response.body()
                        Log.d("api-loginResult", response.body().toString())

                        if(loginResult == 1){

//                        로그인한 유저정보 가져오기
                            RetrofitBuilder.api.userInfo(id.text.toString()).enqueue(object :
                                Callback<UserInfoData> {
                                override fun onResponse(
                                    call: Call<UserInfoData>,
                                    response: Response<UserInfoData>
                                ) {
                                    if(response.isSuccessful){
                                        val userInfo = response.body()
                                        Log.d("api-userInfo", "id: ${userInfo?.id}, name: ${userInfo?.name}, email: ${userInfo?.email}, phone: ${userInfo?.phone}")

                                        val sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE)
                                        sharedPref.edit().run {
                                            putString("id", userInfo?.id)
                                            putString("name", userInfo?.name)
                                            putString("email", userInfo?.email)
                                            putString("phone", userInfo?.phone)
                                            putString("createDate", userInfo?.createDate)
                                            commit()
                                        }

                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }

                                override fun onFailure(call: Call<UserInfoData>, t: Throwable) {
                                    Log.d("api-userInfo", "데이터베이스 불러오는 중 오류 발생")
                                }

                            })
                        }
                        else {
                            Toast.makeText(this@LoginActivity, "아이디 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        Log.d("api-loginResult", "데이터베이스 불러오는 중 오류 발생")
                    }
                })
            }
        }

        binding.tvJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
    }
}