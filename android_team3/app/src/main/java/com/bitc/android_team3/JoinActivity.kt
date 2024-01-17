package com.bitc.android_team3

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.bitc.android_team3.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper(this)
        var database = dbHelper.writableDatabase

        val id = binding.etId
        val pw = binding.etPw
        val pwCheck = binding.etPwCheck
        val name = binding.etName
        val email = binding.etEmail
        val phone = binding.etPhone

//        0 : 아이디 중복 확인 전, 1 : 아이디 중복됨, 2 : 아이디 중복 확인 완료 & 아이디 중복 되지 않음
        var idCheckResult = 0

        var pwCheckResult = false

        binding.btnIdCheck.setOnClickListener {
            if(id.text.toString() == "" || id.text.toString() == null){
                Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                idCheckResult = dbHelper.userIdCheck(database, id.text.toString())

                if (idCheckResult == 1) {
                    id.setText("")
                    Toast.makeText(this, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    idCheckResult = 2
                    Toast.makeText(this, "사용할 수 있는 아이디입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        pw.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            //            텍스트 변화가 있을 시
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (pw.text.toString() == pwCheck.text.toString()) {
                    pwCheckResult = true
                    binding.tvPwCheck.visibility = View.VISIBLE
                    binding.tvPwCheck.text = "비밀번호가 일치"
                    binding.tvPwCheck.setTextColor(Color.parseColor("#A4A4A4"))
                }
                else {
                    pwCheckResult = false
                    binding.tvPwCheck.visibility = View.VISIBLE
                    binding.tvPwCheck.text = "비밀번호가 불일치"
                    binding.tvPwCheck.setTextColor(Color.parseColor("#FF0000"))
                }
            }

            //            입력이 끝난 후
            override fun afterTextChanged(s: Editable?) {
                if (pw.text.toString() == pwCheck.text.toString()) {
                    pwCheckResult = true
                    binding.tvPwCheck.visibility = View.VISIBLE
                    binding.tvPwCheck.text = "비밀번호가 일치"
                    binding.tvPwCheck.setTextColor(Color.parseColor("#A4A4A4"))
                }
                else {
                    pwCheckResult = false
                    binding.tvPwCheck.visibility = View.VISIBLE
                    binding.tvPwCheck.text = "비밀번호가 불일치"
                    binding.tvPwCheck.setTextColor(Color.parseColor("#FF0000"))
                }
            }
        })


        pwCheck.addTextChangedListener(object : TextWatcher {
    //            입력하기 전
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

//            텍스트 변화가 있을 시
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (pw.text.toString() == pwCheck.text.toString()) {
                    pwCheckResult = true
                    binding.tvPwCheck.visibility = View.VISIBLE
                    binding.tvPwCheck.text = "비밀번호가 일치"
                    binding.tvPwCheck.setTextColor(Color.parseColor("#A4A4A4"))
                }
                else {
                    pwCheckResult = false
                    binding.tvPwCheck.visibility = View.VISIBLE
                    binding.tvPwCheck.text = "비밀번호가 불일치"
                    binding.tvPwCheck.setTextColor(Color.parseColor("#FF0000"))
                }
            }

//            입력이 끝난 후
            override fun afterTextChanged(s: Editable?) {
                if (pw.text.toString() == pwCheck.text.toString()) {
                    pwCheckResult = true
                    binding.tvPwCheck.visibility = View.VISIBLE
                    binding.tvPwCheck.text = "비밀번호가 일치"
                    binding.tvPwCheck.setTextColor(Color.parseColor("#A4A4A4"))
                }
                else {
                    pwCheckResult = false
                    binding.tvPwCheck.visibility = View.VISIBLE
                    binding.tvPwCheck.text = "비밀번호가 불일치"
                    binding.tvPwCheck.setTextColor(Color.parseColor("#FF0000"))
                }
            }

        })


        binding.btnJoin.setOnClickListener {
            if ((id.text.toString() == "" || id.text.toString() == null) || (pw.text.toString() == "" || pw.text.toString() == null) ||
                (pwCheck.text.toString() == "" || pwCheck.text.toString() == null) || (name.text.toString() == "" || name.text.toString() == null)) {
                Toast.makeText(this, "필수 정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()

            }
            else {
                if (idCheckResult == 1) {
                    Toast.makeText(this, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show()
                }
                else if (idCheckResult == 0) {
                    Toast.makeText(this, "아이디 중복체크가 필요합니다.", Toast.LENGTH_SHORT).show()
                }
                else if (!pwCheckResult){
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    val user = UserInfoData(id.text.toString(), pw.text.toString(), name.text.toString(), email.text.toString(), phone.text.toString())
                    val result =  dbHelper.userJoin(database, user)

                    if(result){
                        Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

}