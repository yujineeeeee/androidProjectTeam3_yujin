package com.bitc.android_team3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.bitc.android_team3.databinding.ActivityJoinBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding

    private var idFlag = false
    private var pwCheckFlag = false
    private var nameFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = binding.etId
        val pw = binding.etPw
        val pwCheck = binding.etPwCheck
        val name = binding.etName
        val email = binding.etEmail
        val phone = binding.etPhone

//        0 : 아이디 중복 확인 전, 1 : 아이디 중복됨, 2 : 아이디 중복 확인 완료 & 아이디 중복 되지 않음
        var idCheckResult: Int? = 0


        id.addTextChangedListener(object : TextWatcher {
            //            입력하기 전
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            //            타이핑 되는 텍스트에 변화가 있을 때
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            //            입력이 끝난 후
            override fun afterTextChanged(s: Editable?) {
                if(s != null){

                    if(s.isEmpty()){
                        binding.idInputLayout.error = "아이디를 입력해주세요."
                        idFlag = false

                        flagCheck()
                    }
                    else{

                        val userId = id.text.toString()

                        RetrofitBuilder.api.userIdCheck(userId).enqueue(
                            object : Callback<Int> {
                                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                                    if (response.isSuccessful) {
                                        idCheckResult = response.body()

                                        if (idCheckResult == 1) {
                                            binding.idInputLayout.error = "이미 사용중인 아이디입니다."
                                            idFlag = false

                                        } else {
                                            idCheckResult = 2
                                            binding.idInputLayout.error = null
                                            idFlag = true
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<Int>, t: Throwable) {
                                    Log.d("database-idCheck", "못불러옴")
                                }

                            })

                        flagCheck()
                    }

                }
            }

        })

        pw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            //            텍스트 변화가 있을 시
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (pw.text.toString() == pwCheck.text.toString()) {
                    binding.pwCheckInputLayout.error = null
                    pwCheckFlag = true


                } else {
                    binding.pwCheckInputLayout.error = "비밀번호가 일치하지 않습니다."
                    pwCheckFlag = false
                }

                flagCheck()
            }

            //            입력이 끝난 후
            override fun afterTextChanged(s: Editable?) {
                if (pw.text.toString() == pwCheck.text.toString()) {
                    binding.pwCheckInputLayout.error = null
                    pwCheckFlag = true

                } else {
                    binding.pwCheckInputLayout.error = "비밀번호가 일치하지 않습니다."
                    pwCheckFlag = false
                }

                flagCheck()
            }
        })


        pwCheck.addTextChangedListener(object : TextWatcher {
            //            입력하기 전
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            //            텍스트 변화가 있을 시
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (pw.text.toString() == pwCheck.text.toString()) {
                    binding.pwCheckInputLayout.error = null
                    pwCheckFlag = true

                } else {
                    binding.pwCheckInputLayout.error = "비밀번호가 일치하지 않습니다."
                    pwCheckFlag = false
                }

                flagCheck()
            }

            //            입력이 끝난 후
            override fun afterTextChanged(s: Editable?) {
                if (pw.text.toString() == pwCheck.text.toString()) {
                    binding.pwCheckInputLayout.error = null
                    pwCheckFlag = true

                } else {
                    binding.pwCheckInputLayout.error = "비밀번호가 일치하지 않습니다."
                    pwCheckFlag = false
                }

                flagCheck()
            }

        })

        name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                flagCheck()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                flagCheck()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isEmpty()) {
                        binding.nameInputLayout.error = "이름을 입력해주세요."
                        nameFlag = false
                    } else {
                        binding.nameInputLayout.error = null
                        nameFlag = true
                    }

                    flagCheck()
                }
            }

        })



        binding.btnJoin.setOnClickListener {
            val user = UserInfoData(
                id = id.text.toString(),
                pw = pw.text.toString(),
                name = name.text.toString(),
                email = email.text.toString(),
                phone = phone.text.toString(),
                createDate = null
            )

            RetrofitBuilder.api.userInsert(user).enqueue(
                object : Callback<Int> {
                    override fun onResponse(call: Call<Int>, response: Response<Int>) {
                        if (response.isSuccessful) {
                            val result = response.body()

                            if (result == 1) {
                                Toast.makeText(
                                    this@JoinActivity,
                                    "회원가입이 완료되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this@JoinActivity, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@JoinActivity,
                                    "회원가입 중 오류가 발생했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        Log.d("database-insertUser", "데이터베이스 불러오는 중 오류 발생")
                    }

                }
            )

        }
    }

    fun flagCheck() {
        binding.btnJoin.isEnabled = idFlag && pwCheckFlag && nameFlag
    }
}