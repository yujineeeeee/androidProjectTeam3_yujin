package com.bitc.android_team3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bitc.android_team3.databinding.ActivityCategoryDetailBinding
import com.bumptech.glide.Glide

class CategoryDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productImage = intent.getStringExtra("imageUrl")
        val productName = intent.getStringExtra("productName")
        val productJeongGa = intent.getIntExtra("productJeongGa", 0)
        val productPrice = intent.getIntExtra("productPrice", 0)
        val productDiscount = intent.getIntExtra("productDiscount", 0)

        binding.apply {
            tvProductName.text = productName
            tvProductPrice.text = productPrice.toString()

            Glide.with(this@CategoryDetailActivity)
                .load(productImage)
                .override(200, 200)
                .into(ivProductImage)
        }

        val productCount = binding.pdCount
        var count = 0;

        binding.btnPlus.setOnClickListener {
            count ++
            binding.pdCount.setText(count.toString())
        }

        binding.btnMinus.setOnClickListener {
            if(count < 0 || count == 0){
                Toast.makeText(this, "0개 이상 선택하세요", Toast.LENGTH_SHORT).show()
            }
            else {
                count --
                binding.pdCount.setText(count.toString())
            }
        }



    }
}