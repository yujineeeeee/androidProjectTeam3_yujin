package com.bitc.android_team3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitc.android_team3.Adapter.KeepAdapter
import com.bitc.android_team3.Data.KeepData
import com.bitc.android_team3.databinding.ActivityKeepBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class KeepActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityKeepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)   //왼쪽 버튼 사용설정(기본은 뒤로가기)

        binding.headerLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.headerKeep.setOnClickListener {
            if (isUserLoggedIn()) {
                val intent = Intent(this,KeepActivity::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
        }

//        로그인된 상태에서는 로그인 버튼 사라짐
        if (isUserLoggedIn()) {
            binding.headerLogin.visibility = View.GONE
        }
        else {
            binding.headerLogin.visibility = View.VISIBLE
        }

        binding.selectAll.setOnCheckedChangeListener { _, isChecked ->
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = recyclerView.adapter as KeepAdapter

            if (isChecked) {
                adapter.selectAllItems()
            } else {
                adapter.clearAllSelections()
            }
        }

        val sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val loggedInUserId = sharedPref.getString("id", "") ?: ""

        // 예시: Retrofit을 사용한 API 호출
        RetrofitBuilder.api.keepView(loggedInUserId).enqueue(object : Callback<List<KeepData>> {
            override fun onResponse(
                call: Call<List<KeepData>>,
                response: Response<List<KeepData>>
            ) {
                if (response.isSuccessful) {
                    val keepInfoList: List<KeepData>? = response.body()

                    // 가져온 상품 정보를 처리
                    val sharedPref = getSharedPreferences("keep_info", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()

                    val gson = Gson()
                    val json = gson.toJson(keepInfoList)
                    editor.putString("keepInfoList", json)
                    editor.apply()

                    val type = object : TypeToken<List<KeepData>>() {}.type
                    val keepInfoList2 = gson.fromJson<List<KeepData>>(json, type)

                    for (keepInfo in keepInfoList2) {
                        // 각각의 keepInfo가 null이 아닌 경우에만 SharedPreferences에 저장
                        keepInfo?.let {
                            editor.putString("basketItem", it.kpName)
                            editor.putString("basketCnt", it.kpCnt.toString())
                            editor.putString("basketJeongGa", it.kpJeongGa.toString())
                            editor.putString("basketPrice", it.kpPrice.toString())
                        }
                    }

                    // 변경사항을 저장
                    editor.apply()

                    // 리사이클러뷰 어댑터 생성
                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                    recyclerView.layoutManager = LinearLayoutManager(this@KeepActivity)

                    val adapter = KeepAdapter(keepInfoList2.toMutableList())
                    recyclerView.adapter = adapter

                } else {
                    // 상품 정보를 가져오는 데 실패한 경우
                    Log.d("api-productInfo", "상품 정보를 가져오는 중 오류 발생: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<KeepData>>, t: Throwable) {
                // API 호출 자체가 실패한 경우
                Log.d("api-productInfo", "대체뭔데api 오류 발생", t)
            }
        })

        binding.btnOk.setOnClickListener {
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = recyclerView.adapter as KeepAdapter

            // checkedItems 리스트에는 체크된 아이템들이 들어있습니다.
            val checkedItems = mutableListOf<KeepData>()

            // 체크된 아이템을 가져와서 리스트에 추가
            for (i in 0 until adapter.itemCount) {
                if (adapter.isChecked(i)) {
                    checkedItems.add(adapter.getKeepInfoList()[i])
                }
            }

            Log.d("api-qqqq", "${checkedItems}")

            if (checkedItems.size != 0) {
                RetrofitBuilder.api.basketInsert(checkedItems).enqueue(object : Callback<Int> {
                    override fun onResponse(call: Call<Int>, response: Response<Int>) {
                        Log.d("api-qqq", "${checkedItems}")
                        Log.d("api-insert", "성공옹")
                        val result = response.body()
                        Log.d("api-insert", "${result}")

                        if (result == 1) {
                            Toast.makeText(this@KeepActivity, "구매 확정", Toast.LENGTH_SHORT).show()
                            finish() //인텐트 종료

                            overridePendingTransition(0, 0) //인텐트 효과 없애기

                            val intent = intent //인텐트

                            startActivity(intent) //액티비티 열기

                            overridePendingTransition(0, 0) //인텐트 효과 없애기

                        } else {
                            Toast.makeText(this@KeepActivity, "오류 발생", Toast.LENGTH_SHORT).show()
                            Log.d("api-insert", "왜!!")
                        }
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }else {
                Toast.makeText(this@KeepActivity, "상품을 선택해주세요", Toast.LENGTH_SHORT).show()
            }

        }

    }

    //    툴바
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

//            R.id.logoFragment -> {
//                // 툴바의 아이콘이 선택되었을 때 수행할 동작 정의
//                val intent = Intent(applicationContext, MainActivity::class.java)
//                startActivity(intent)
//                return true
//            }
//
//            R.id.basketFragment -> {
//                // 장바구니 클릭 시 로그인 상태 확인
//                if (isUserLoggedIn()) {
//                    startActivity(Intent(applicationContext, KeepActivity::class.java))
//                } else {
//                    // 로그인 상태가 아니면 로그인 화면으로 이동
//                    startActivity(Intent(applicationContext, LoginActivity::class.java))
//                }
//                return true
//            }


            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val id = sharedPref.getString("id", "")

        // id가 비어있지 않으면 로그인 상태로 판단
        return id?.isNotEmpty() == true
    }
}