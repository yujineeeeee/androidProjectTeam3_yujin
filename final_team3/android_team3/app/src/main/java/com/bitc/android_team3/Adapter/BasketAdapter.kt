package com.bitc.android_team3.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitc.android_team3.ViewHolder.BasketViewHolder
import com.bitc.android_team3.Data.BasketData
import com.bitc.android_team3.databinding.CalendarTextBinding
import java.text.DecimalFormat

class BasketAdapter(val items: MutableList<BasketData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BasketViewHolder(CalendarTextBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val decimal = DecimalFormat("#,###")

        val bind = (holder as BasketViewHolder).binding
        bind.TextName.text = items[position].pdName
        bind.TextCnt.text = items[position].pdCnt.toString()
        bind.TextPrice.text = decimal.format(items[position].pdPrice).toString()
    }
}