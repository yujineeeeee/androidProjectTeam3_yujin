package com.bitc.android_team3

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.bitc.android_team3.Data.KeepData
import com.bitc.android_team3.databinding.ActivityCategoryDetailBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class CategoryDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCategoryDetailBinding.inflate(layoutInflater)
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

        val sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE)

        val id = sharedPref.getString("id", "")
        val productImage = intent.getStringExtra("imageUrl")
        val productName = intent.getStringExtra("productName")
        val productJeongGa = intent.getIntExtra("productJeongGa", 0)
        val productPrice = intent.getIntExtra("productPrice", 0)
        val productDiscount = intent.getIntExtra("productDiscount", 0)
        val productCd = intent.getStringExtra("productCd")


//        가격 데이터 포맷 설정
        val decimal = DecimalFormat("#,###")


        binding.apply {
            tvProductName.text = productName
            tvProductPrice.text = "₩${decimal.format(productPrice)}"

            if (productDiscount == 0) {
                tvProductJeongGa.visibility = View.GONE
            } else {
                tvProductJeongGa.text = "₩${decimal.format(productJeongGa)}"
                tvProductJeongGa.paintFlags =
                    tvProductJeongGa.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

            Glide.with(this@CategoryDetailActivity)
                .load(productImage)
                .override(200, 200)
                .into(ivProductImage)
        }

        val productCount = binding.pdCount
        var pdCount = 1;

        var totalPrice = productPrice * pdCount

        productCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if(s.isNotEmpty()){
                        pdCount = s.toString().toInt()
                        totalPrice = productPrice * pdCount
                        binding.tvTotalPrice.text = "${decimal.format(totalPrice)} 원"
                    }
                    else {
                        binding.tvTotalPrice.text = ""
                    }
                }
            }
        })

        binding.tvTotalPrice.text = "${decimal.format(totalPrice)} 원"

        binding.btnPlus.setOnClickListener {
            pdCount++
            totalPrice = productPrice * pdCount
            binding.tvTotalPrice.text = "${decimal.format(totalPrice)} 원"
            productCount.setText(pdCount.toString())
        }

        binding.btnMinus.setOnClickListener {
            if (pdCount >= 1) {
                pdCount--
                totalPrice = productPrice * pdCount
                binding.tvTotalPrice.text = "${decimal.format(totalPrice)} 원"
                productCount.setText(pdCount.toString())
            }
        }

        binding.btnAddBasket.setOnClickListener {
            if (id == null || id == "") {
                Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            } else {

                val productCnt: Int = binding.pdCount.text.toString().toInt()

                if (productCnt == 0) {
                    Toast.makeText(this, "1개 이상 선택해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    val keepData = KeepData(
                        kpCd = productCd,
                        kpName = productName,
                        kpJeongGa = productJeongGa,
                        kpDiscount = productDiscount,
                        kpPrice = productPrice,
                        kpId = id,
                        kpCnt = productCnt,
                        kpCreateDate = null,
                        kpIdx = 0,
                        kpImage = productImage
                    )


                    RetrofitBuilder.api.KeepInsert(keepData).enqueue(object : Callback<Int> {
                        override fun onResponse(call: Call<Int>, response: Response<Int>) {
                            if (response.isSuccessful) {
                                val result = response.body()

                                if (result == 1) {
//                                    Toast.makeText(
//                                        this@CategoryDetailActivity,
//                                        "장바구니에 저장되었습니다.",
//                                        Toast.LENGTH_SHORT
//                                    ).show()

                                    AlertDialog.Builder(this@CategoryDetailActivity)
                                        .setTitle("장바구니 저장 완료")
                                        .setMessage("장바구니로 이동하시겠습니까?")
                                        .setPositiveButton("장바구니로 이동", object : DialogInterface.OnClickListener {
                                            override fun onClick(dialog: DialogInterface, which: Int) {
                                                val intent = Intent(this@CategoryDetailActivity, KeepActivity::class.java)
                                                startActivity(intent)
                                                Log.d("MyTag", "positive")
                                            }
                                        })
                                        .setNegativeButton("쇼핑 계속하기", object : DialogInterface.OnClickListener {
                                            override fun onClick(dialog: DialogInterface, which: Int) {
                                                Log.d("MyTag", "negative")
                                            }
                                        })
//                                        .setNeutralButton("neutral", object : DialogInterface.OnClickListener {
//                                            override fun onClick(dialog: DialogInterface, which: Int) {
//                                                Log.d("MyTag", "neutral")
//                                            }
//                                        })
                                        .create()
                                        .show()

                                }
                            } else {
                                Log.d("database-keepInsert", "디비 연결 실패")
                            }
                        }

                        override fun onFailure(call: Call<Int>, t: Throwable) {
                            Log.d("database-keepInsert", "리턴값 없음")
                        }

                    })
                }

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
//            R.id.LoginBtn -> {
//                // 툴바의 아이콘이 선택되었을 때 수행할 동작 정의
//                startActivity(Intent(applicationContext, LoginActivity::class.java))
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