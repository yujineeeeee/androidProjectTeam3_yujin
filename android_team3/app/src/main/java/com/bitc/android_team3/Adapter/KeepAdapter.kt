package com.bitc.android_team3.Adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bitc.android_team3.Data.KeepData
import com.bitc.android_team3.RetrofitBuilder
import com.bitc.android_team3.ViewHolder.KeepViewHolder
import com.bitc.android_team3.databinding.ItemKeepBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class KeepAdapter(private val keepInfoList: MutableList<KeepData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 각 아이템의 체크 상태를 저장하는 맵
    val checkedMap = mutableMapOf<Int, Boolean>()

    // ... (이하 생략)

    // 체크 상태를 반환하는 함수
    fun isChecked(position: Int): Boolean {
        return checkedMap[position] ?: false
    }

    // 체크 상태를 갱신하는 함수
    fun updateCheck(position: Int, isChecked: Boolean) {
        checkedMap[position] = isChecked
        notifyDataSetChanged() // 체크 상태가 변경될 때마다 RecyclerView 갱신
    }

    fun selectAllItems() {
        checkedMap.clear() // 초기화
        for (position in 0 until itemCount) {
            updateCheck(position, true)
        }
        notifyDataSetChanged()
    }

    fun clearAllSelections() {
        for (item in checkedMap) {
            updateCheck(item.key, false)
        }
        notifyDataSetChanged()
    }

    fun getKeepInfoList(): List<KeepData> {
        return keepInfoList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return KeepViewHolder(ItemKeepBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return keepInfoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        Log.d("msy-recycler" , "onBindViewHolder : $position")

        val binding = (holder as KeepViewHolder).binding

        // CheckBox 체크 상태 설정
        binding.checkBox.isChecked = isChecked(position)

        // CheckBox 클릭 리스너
        binding.checkBox.setOnClickListener {
            val checked = (it as CheckBox).isChecked
            updateCheck(position, checked)
        }

        val kpImg = "${keepInfoList[position].kpImage}"
        binding.basketItem.text = keepInfoList[position].kpName
        binding.basketCnt.text = keepInfoList[position].kpCnt.toString()

//        정가 1000원 단위 콤마로 구분
        val jeongGa = keepInfoList[position].kpJeongGa!! * binding.basketCnt.text.toString().toInt()
        val formattedJeongGa = NumberFormat.getNumberInstance(Locale.getDefault()).format(jeongGa)
        binding.basketJeongGa.text = "$formattedJeongGa 원"
        binding.basketJeongGa!!.setPaintFlags(binding.basketJeongGa!!.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

//        판매가 1000원 단위 콤마로 구분
        val totalPrice = keepInfoList[position].kpPrice!! * binding.basketCnt.text.toString().toInt()
        val formattedPrice = NumberFormat.getNumberInstance(Locale.getDefault()).format(totalPrice)
        binding.basketPrice.text = "$formattedPrice 원"
//        binding.basketPrice.text = (keepInfoList[position].kpPrice!! * binding.basketCnt.text.toString().toInt()).toString() + " 원"

        Glide.with(holder.itemView.context)
            .load(kpImg)
            .override(200, 200)
            .into(binding.basketImg)


        // sub 버튼 클릭 리스너 설정
        binding.subBtn.setOnClickListener {

            val kpId = "test1"
            val kpCd = keepInfoList[position].kpCd.toString()
            val kpCnt = binding.basketCnt.text.toString().toInt() - 1

            RetrofitBuilder.api.keepCntUpdate(kpId, kpCd, kpCnt).enqueue(object : Callback<List<KeepData>>{
                override fun onResponse(
                    call: Call<List<KeepData>>,
                    response: Response<List<KeepData>>
                ) {
                    if (response.isSuccessful) {
                        // API 호출이 성공한 경우
                        Log.d("api-keepUpdate", "성공~~")

                        keepInfoList[position].kpCnt?.let {
                            // kpCnt가 null이 아닌 경우에만 -1 연산 수행
                            if (it > 1) {
                                keepInfoList[position].kpCnt = it - 1
                                // 변경된 kpCnt 값을 TextView에 반영
                                binding.basketCnt.text = keepInfoList[position].kpCnt.toString()
                                val jeongGa = keepInfoList[position].kpJeongGa!! * binding.basketCnt.text.toString().toInt()
                                val formattedJeongGa = NumberFormat.getNumberInstance(Locale.getDefault()).format(jeongGa)
                                binding.basketJeongGa.text = "$formattedJeongGa 원"
                                val totalPrice = keepInfoList[position].kpPrice!! * binding.basketCnt.text.toString().toInt()
                                val formattedPrice = NumberFormat.getNumberInstance(Locale.getDefault()).format(totalPrice)
                                binding.basketPrice.text = "$formattedPrice 원"
                            }
                        }
                    } else {
                        // API 호출이 실패한 경우
                        Log.d("api-keepUpdate", "API 호출 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<KeepData>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

        // add 버튼 클릭 리스너 설정
        binding.addBtn.setOnClickListener {

//            val KeepDatas = KeepData(kpIdx = clickedPosition, kpCd = "1", kpName = "sdf", kpJeongGa = 1, kpDiscount = 1, kpPrice = 1, kpCnt = binding.basketCnt.text.toString().toInt() + 1, kpCreateDate = "2024-01-20 00:00:00", kpId = "test1")

            val kpIdx = keepInfoList[position].kpIdx
            val kpId = "test1"
            val kpCd = keepInfoList[position].kpCd.toString()
            val kpCnt = binding.basketCnt.text.toString().toInt() + 1

            RetrofitBuilder.api.keepCntUpdate(kpId, kpCd, kpCnt).enqueue(object : Callback<List<KeepData>>{
                override fun onResponse(
                    call: Call<List<KeepData>>,
                    response: Response<List<KeepData>>
                ) {
                    if (response.isSuccessful) {
                        // API 호출이 성공한 경우
                        Log.d("api-keepUpdate", "성공~~")

                        keepInfoList[position].kpCnt?.let {
                            if (it > 0) {
                                keepInfoList[position].kpCnt = it + 1
                                // 변경된 kpCnt 값을 TextView에 반영
                                binding.basketCnt.text = keepInfoList[position].kpCnt.toString()
                                val jeongGa = keepInfoList[position].kpJeongGa!! * binding.basketCnt.text.toString().toInt()
                                val formattedJeongGa = NumberFormat.getNumberInstance(Locale.getDefault()).format(jeongGa)
                                binding.basketJeongGa.text = "$formattedJeongGa 원"
                                val totalPrice = keepInfoList[position].kpPrice!! * binding.basketCnt.text.toString().toInt()
                                val formattedPrice = NumberFormat.getNumberInstance(Locale.getDefault()).format(totalPrice)
                                binding.basketPrice.text = "$formattedPrice 원"
                            }
                        }
                    } else {
                        // API 호출이 실패한 경우
                        Log.d("api-keepUpdate", "API 호출 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<KeepData>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

        binding.btnX.setOnClickListener {

            val kpId = "test1"
            // 현재 클릭된 아이템의 위치
            val kpCd = keepInfoList[position].kpCd.toString()
            val position = holder.adapterPosition

            RetrofitBuilder.api.keepDelete(kpId, kpCd).enqueue(object : Callback<List<KeepData>>{
                override fun onResponse(
                    call: Call<List<KeepData>>,
                    response: Response<List<KeepData>>
                ) {
                    if (response.isSuccessful) {
                        // API 호출이 성공한 경우
                        Log.d("api-keepUpdate", "성공~~")

                        // 리스트에서 해당 위치의 아이템 제거
                        keepInfoList.removeAt(position)

                        // 어댑터에게 해당 위치의 아이템이 제거되었음을 알림
                        notifyItemRemoved(position)

                    } else {
                        // API 호출이 실패한 경우
                        Log.d("api-keepUpdate", "API 호출 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<KeepData>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }



        binding.basketItem.setOnClickListener{
            Log.d("msy-recycler" , "item root click : $position")
        }
    }
}