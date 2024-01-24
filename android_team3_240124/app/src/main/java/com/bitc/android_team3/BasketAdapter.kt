package com.bitc.android_team3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitc.android_team3.databinding.CalendarTextBinding

class BasketAdapter(val items: MutableList<BasketData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BasketViewHolder(CalendarTextBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bind = (holder as BasketViewHolder).binding
        bind.TextName.text = items[position].pdName
        bind.TextPrice.text = items[position].pdPrice.toString()
        bind.TextCnt.text = items[position].pdCnt.toString()
    }
}