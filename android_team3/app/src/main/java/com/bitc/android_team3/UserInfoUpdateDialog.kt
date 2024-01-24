package com.bitc.android_team3

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bitc.android_team3.databinding.DialogUserInfoUpdateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserInfoUpdateDialog : DialogFragment() {

    private lateinit var binding: DialogUserInfoUpdateBinding

    private var nameFlag = true
    private var emailFlag = false
    private var phoneFlag = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DialogUserInfoUpdateBinding.inflate(inflater, container, false)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

//        다이얼로그 외의 영역 눌렀을때 취소되는거 방지
        isCancelable = false

        val sharedPref = this.requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        var id = sharedPref.getString("id", "")
        var name = sharedPref.getString("name", "")
        var email = sharedPref.getString("email", "")
        var phone = sharedPref.getString("phone", "")

        binding.etUpdateUserId.setText(id)
        binding.etUpdateUserName.setText(name)
        binding.etUpdateUserEmail.setText(email)
        binding.etUpdateUserPhone.setText(phone)


        binding.etUpdateUserName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameFlag = true

                userInfoFlagCheck()
            }

            override fun afterTextChanged(s: Editable?) {
                if(s != null){

                    if(s.isEmpty()){
                        binding.idUpdateNameInputLayout.error = "이름을 입력해주세요"
                        nameFlag = false
                    }
                    else {
                        binding.idUpdateNameInputLayout.error = null
                        nameFlag = true
                    }
                }

                userInfoFlagCheck()
            }

        })

        binding.etUpdateUserEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailFlag = true
                userInfoFlagCheck()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.etUpdateUserPhone.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                phoneFlag = true
                userInfoFlagCheck()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        binding.btnUpdateUserInfo.setOnClickListener {

            val updateUserName = binding.etUpdateUserName.text.toString()
            val updateUserEmail = binding.etUpdateUserEmail.text.toString()
            val updateUserPhone = binding.etUpdateUserPhone.text.toString()

            val user = UserInfoData(id = id, pw = null, name = updateUserName, email = updateUserEmail, phone = updateUserPhone, createDate = null)

            RetrofitBuilder.api.userUpdate(user).enqueue(
                object : Callback<Int>{
                    override fun onResponse(call: Call<Int>, response: Response<Int>) {
                        if(response.isSuccessful){
                            val result = response.body()

                            if(result == 1){
                                Log.d("database-updateUser", "회원정보 수정 완료")

                                editor.putString("name", updateUserName)
                                editor.putString("email", updateUserEmail)
                                editor.putString("phone", updateUserPhone)
                                editor.commit()
                            }
                        }
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        Log.d("database-updateUser", "데이터베이스 불러오는 중 오류 발생")
                    }
                }
            )

            Toast.makeText(context, "회원정보 수정 완료", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.btnUpdateCancel.setOnClickListener {
            dismiss()
        }

        binding.tvUserDelete.setOnClickListener {

            val alertDialog = AlertDialog.Builder(requireContext())
            var ediText: EditText = EditText(requireContext())


            alertDialog.setTitle("회원탈퇴")
            alertDialog.setMessage("탈퇴하시려면 \"탈퇴\"를 입력해주세요.")
            alertDialog.setView(ediText)
            alertDialog.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                var inputText = ediText.text.toString()

                if(inputText == "탈퇴"){

                    RetrofitBuilder.api.userDelete(id!!).enqueue(
                        object : Callback<Int>{
                            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                                if(response.isSuccessful){
                                    val result = response.body()

                                    if(result == 1){
                                        Toast.makeText(requireContext(), "탈퇴되었습니다.", Toast.LENGTH_SHORT).show()

//                                        로그인 값 지우기
                                        editor.clear()
                                        editor.commit()

//                                        탈퇴 확인 다이얼로그 종료
                                        dialog.dismiss()
//                                        회원수정 다이얼로그 종료
                                        dismiss()

                                        val intent = Intent(requireContext(), MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<Int>, t: Throwable) {
                                Log.d("database-deleteUser", "데이터베이스 불러오는 중 오류 발생")
                            }

                        }
                    )
                }
                else{
                    Toast.makeText(requireContext(), "다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                }

            })

            alertDialog.show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this@UserInfoUpdateDialog, 0.9f, 0.9f)
    }

    fun Context.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {

            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialogFragment.dialog?.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x, y)

        } else {

            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialogFragment.dialog?.window

            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
        }
    }

    fun userInfoFlagCheck(){
        binding.btnUpdateUserInfo.isEnabled = nameFlag || emailFlag || phoneFlag
    }

}