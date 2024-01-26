package com.bitc.android_team3.Adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView
import com.bitc.android_team3.CategoryDetailActivity
import com.bitc.android_team3.ViewHolder.CategoryViewHolder
import com.bitc.android_team3.Data.CategoryData
import com.bitc.android_team3.databinding.CategoryItemBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class CategoryAdapter(val categoryItem: MutableList<CategoryData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return categoryItem.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bind = (holder as CategoryViewHolder).binding

        val dumCd = categoryItem[position].cd
        val dumPrice = categoryItem[position].price
        val dumJeonGa = categoryItem[position].jeongGa
        val dumImg = "${categoryItem[position].img}"
        val dumImgBig = "${categoryItem[position].imgBig}"

        if (dumJeonGa.equals(dumPrice)) {
            bind.textJeongGa.visibility = View.GONE
        } else {
            bind.textJeongGa.setTextSize(Dimension.SP, 12F)
            bind.textJeongGa.setTextColor(Color.parseColor("#adb5bd"))
            bind.textJeongGa!!.setPaintFlags(bind.textJeongGa!!.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
        }

        val decimal = DecimalFormat("#,###")

        bind.textName.text = "${categoryItem[position].name}"
//        bind.textJeongGa.text = "${categoryItem[position].jeongGa}₩"
//        bind.TextPrice.text = "${categoryItem[position].price}₩"
        bind.textJeongGa.text = "₩${decimal.format(categoryItem[position].jeongGa)}"
        bind.TextPrice.text = "₩${decimal.format(categoryItem[position].price)}"

        Glide.with(holder.itemView.context)
            .load(dumImg)
            .override(200, 200)
            .into(bind.imageViewProduct)


        bind.imageViewProduct.setOnClickListener{
            val intent = Intent(holder.itemView.context, CategoryDetailActivity::class.java).apply {
                putExtra("imageUrl", categoryItem[position].imgBig.toString())
                putExtra("productName", categoryItem[position].name.toString())
                putExtra("productJeongGa", categoryItem[position].jeongGa)
                putExtra("productPrice",  categoryItem[position].price)
                putExtra("productDiscount", categoryItem[position].discount)
                putExtra("productCd", categoryItem[position].cd)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

}