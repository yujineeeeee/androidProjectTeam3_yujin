package com.bitc.android_team3.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitc.android_team3.Data.Total
import com.bitc.android_team3.RetrofitBuilder
import com.bitc.android_team3.databinding.CalendarItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.time.LocalDate


class CalendarAdapter(private val dayList: ArrayList<LocalDate?>, val id: String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(position: Int){}
    }

    var itemClickListener: OnItemClickListener? = null


    inner class CalendarHolder(val binding: CalendarItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.ItemVeiw.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CalendarHolder(
            CalendarItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as CalendarHolder).binding


        var day = dayList[holder.adapterPosition]

        if ((position+1)%7 == 0){
            binding.dayText.setTextColor(Color.parseColor("#FF0099CC"))
        }else if (position == 0 || position %7 == 0){
            binding.dayText.setTextColor(Color.RED)
        }

        if (day == null) {
            binding.priceText.text = ""
            binding.dayText.text = ""
        } else {
            binding.dayText.text = day.dayOfMonth.toString()

            var iYear = day?.year
            var iMonth = day?.monthValue
            var iDay = day?.dayOfMonth
            var Day: String = ""
            var Month: String = ""

            if (iDay != null) {
                if (iDay < 10) {
                    Day = "0${iDay}"
                } else {
                    Day = "${iDay}"
                }
            }

            if (iMonth != null) {
                if (iMonth < 10) {
                    Month = "0${iMonth}"
                } else {
                    Month = "${iMonth}"
                }
            }

            var TotalDaily: String = "$iYear" + "$Month" + "$Day"
            val total: Total = Total(pdId = id, pdCreateDate = TotalDaily)

            val decimal = DecimalFormat("#,###")

            // 달력에 금액 표시
            Thread {
                RetrofitBuilder.api.totalAmount(total).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val price = response.body()?.toInt()

                        binding.priceText.text = "₩${decimal.format(price)}"
//                        binding.priceText.text = response.body().toString() + "₩"
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("kkang", "실패")
                    }

                })
            }.start()

            try {
                Thread.sleep(20)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

}
