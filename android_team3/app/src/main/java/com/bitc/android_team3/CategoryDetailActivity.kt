package com.bitc.android_team3

import android.content.Context
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
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

            if(productDiscount == 0){
                tvProductJeongGa.visibility = View.GONE
            }
            else {
                tvProductJeongGa.text = "₩${decimal.format(productJeongGa)}"
                tvProductJeongGa.paintFlags = tvProductJeongGa.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

            Glide.with(this@CategoryDetailActivity)
                .load(productImage)
                .override(200, 200)
                .into(ivProductImage)
        }

        val productCount = binding.pdCount
        var pdCount = 1;

        var totalPrice =  productPrice * pdCount

//        productCount.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                if(s != null){
//                    pdCount = binding.pdCount.text.toString().toInt()
//                }
//            }
//
//        })

        binding.tvTotalPrice.text = "${decimal.format(totalPrice)} 원"

        binding.btnPlus.setOnClickListener {
            pdCount ++
            totalPrice =  productPrice * pdCount
            binding.tvTotalPrice.text =  "${decimal.format(totalPrice)} 원"
            productCount.setText(pdCount.toString())
        }

        binding.btnMinus.setOnClickListener {
            if (pdCount >= 1){
                pdCount --
                totalPrice =  productPrice * pdCount
                binding.tvTotalPrice.text = "${decimal.format(totalPrice)} 원"
                productCount.setText(pdCount.toString())
            }
        }

        binding.btnAddBasket.setOnClickListener {

            if(id == null || id == ""){
                Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
            else {

                val productCnt: Int = binding.pdCount.text.toString().toInt()

                if(productCnt == 0){
                    Toast.makeText(this, "1개 이상 선택해주세요", Toast.LENGTH_SHORT).show()
                }
                else {
                    val keepData = KeepData(kpCd = productCd, kpName =  productName, kpJeongGa = productJeongGa, kpDiscount = productDiscount, kpPrice = productPrice, kpId = id, kpCnt = productCnt, kpCreateDate = null, kpIdx = null, kpImage = productImage)


                    RetrofitBuilder.api.KeepInsert(keepData).enqueue(object : Callback<Int>{
                        override fun onResponse(call: Call<Int>, response: Response<Int>) {
                            if(response.isSuccessful){
                                val result = response.body()

                                if(result == 1){
                                    Toast.makeText(this@CategoryDetailActivity, "장바구니에 저장되었습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else
                            {
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
}