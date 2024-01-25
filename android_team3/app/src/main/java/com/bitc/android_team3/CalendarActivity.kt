package com.bitc.android_team3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitc.android_team3.Adapter.BasketAdapter
import com.bitc.android_team3.Adapter.CalendarAdapter
import com.bitc.android_team3.Data.BasketData
import com.bitc.android_team3.databinding.ActivityCalendarBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarActivity : AppCompatActivity() {

    lateinit var binding: ActivityCalendarBinding
    // 년월 변수
    lateinit var selectedDate: LocalDate

    var id: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        id = sharedPref.getString("id", "")
        var name = sharedPref.getString("name", "")
        var email = sharedPref.getString("email", "")
        var phone = sharedPref.getString("phone", "")
        var createDate = sharedPref.getString("createDate", "")


//        로그인 하지 않았을 때
        if(id == null || id == ""){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
//        로그인 했을 때
        else {
            binding.tvMyPageId.text = "${name} 님"
            binding.tvMyPageCreateDate.text = "가입일 : $createDate"

//            로그아웃
            binding.tvLogout.setOnClickListener {
                editor.clear()
                editor.commit()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

//        회원정보 수정
        binding.llUserUpdate.setOnClickListener {
            UserInfoUpdateDialog().show(supportFragmentManager, "dialog")
        }


        // 현재 날짜
        selectedDate = LocalDate.now()
        // 화면 설정
        setMonthView()

        binding.preBtn.setOnClickListener {
            // 현재 월 -1 변수 담기
            selectedDate = selectedDate.minusMonths(1)
            binding.BasketList.visibility = View.GONE
            setMonthView()
        }

        binding.nextBtn.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            binding.BasketList.visibility = View.GONE
            setMonthView()
        }
    }

    // 날짜 화면에 보여주기
    fun setMonthView() {
        binding.monthYearText.text = monthYearFromDate(selectedDate)

        val dayList = dayInMonthArray(selectedDate)

        val calendarAdapter = CalendarAdapter(dayList, id)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = calendarAdapter


        calendarAdapter.itemClickListener = object : CalendarAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val item = dayList[position]
                var BYear = item?.year
                var BMonth = item?.monthValue
                var BDay = item?.dayOfMonth
                var Day: String = ""
                var Month: String = ""

                if (BDay != null) {
                    if (BDay < 10) {
                        Day = "0${BDay}"
                    } else {
                        Day = "${BDay}"
                    }
                }

                if (BMonth != null) {
                    if (BMonth < 10) {
                        Month = "0${BMonth}"
                    } else {
                        Month = "${BMonth}"
                    }
                }

                val sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE)

                var id = sharedPref.getString("id", "")

                var TotalDaily: String = "$BYear" + "$Month" + "$Day"
                var pdId = id
                val pdCreateDate: String = TotalDaily
                val items = mutableListOf<BasketData>()

                Thread {
                    RetrofitBuilder.api.BasketView(pdId, pdCreateDate)
                        .enqueue(object : Callback<List<BasketData>> {
                            override fun onResponse(
                                call: Call<List<BasketData>>,
                                response: Response<List<BasketData>>
                            ) {
                                val BasketList: List<BasketData>? = response.body()

                                // 가져온 상품 정보를 처리
                                val sharedPref =
                                    getSharedPreferences("basket_list", Context.MODE_PRIVATE)
                                val editor = sharedPref.edit()

                                val gson = Gson()
                                val json = gson.toJson(BasketList)
                                editor.putString("BasketList", json)
                                editor.apply()

                                val type = object : TypeToken<List<BasketData>>() {}.type
                                val BasketInfoList = gson.fromJson<List<BasketData>>(json, type)

                                for (Basket in BasketInfoList) {
                                    Basket?.let {
                                        items.add(BasketData(it.pdIdx, it.pdCd, it.pdName, it.pdJeongGa, it.pdDiscount, it.pdPrice, it.pdCnt, it.pdCreateDate, it.pdId))
                                    }
                                }

                                if (items.size != 0) {
                                    binding.BasketList.visibility = View.VISIBLE

                                    var sumTotal = 0
                                    for (i in 0..items.size - 1) {
                                        sumTotal += items[i].pdPrice!! * items[i].pdCnt!!
                                    }

                                    val decimal = DecimalFormat("#,###")

//                                    binding.totalText.text = "총 금액: " + sumTotal.toString() + "₩"
                                    binding.totalText.text = "총 금액 ₩${decimal.format(sumTotal)}"

                                    val basketAdapter = BasketAdapter(items)

                                    binding.recyclerText.itemAnimator = null
                                    binding.recyclerText.layoutManager = LinearLayoutManager(this@CalendarActivity)
                                    binding.recyclerText.adapter = basketAdapter
                                } else {
                                    binding.BasketList.visibility = View.GONE
                                }

                            }

                            override fun onFailure(call: Call<List<BasketData>>, t: Throwable) {
                                Log.d("kkang_error", "실패!!!!!ㄴㄴ")
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

    // 날짜 타입 설정
    fun monthYearFromDate(data: LocalDate): String {
        var formatter = DateTimeFormatter.ofPattern("MM월 yyyy")

        //받아온 날짜를 해당 포맷으로 변경
        return data.format(formatter)
    }

    fun dayInMonthArray(data: LocalDate): ArrayList<LocalDate?> {
        var dayList = ArrayList<LocalDate?>()
        var yearMonth = YearMonth.from(data)
        var lastDay = yearMonth.lengthOfMonth()
        var firstDay = selectedDate.withDayOfMonth(1)
        var dayOfWeek = firstDay.dayOfWeek.value

        for (i in 1..41) {
            if (i <= dayOfWeek || i > (lastDay + dayOfWeek)) {
                dayList.add(null)
            } else {
                dayList.add(LocalDate.of(selectedDate.year, selectedDate.monthValue, i - dayOfWeek))
            }
        }

        return dayList
    }


}