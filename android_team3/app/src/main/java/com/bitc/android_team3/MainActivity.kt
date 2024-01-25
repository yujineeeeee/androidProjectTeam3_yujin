package com.bitc.android_team3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bitc.android_team3.Adapter.RecyclerAdapter
import com.bitc.android_team3.databinding.ActivityMainBinding
import com.bitc.android_team3.Adapter.InfiniteAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var list = mutableListOf(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)
    private var numBanner = 3
    private var currentPosition = Int.MAX_VALUE / 2
    private var myHandler = MyHandler()
    private val intervalTime = 1500.toLong() // 몇초 간격으로 페이지를 넘길것인지 (1500 = 1.5초)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        var id = sharedPref.getString("id", "")

//        로그인 안했을 때
        if(id == null || id == ""){
            binding.btnLogout.visibility = View.GONE
//            binding.btnMyPage.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
        }
//        로그인 했을 때
        else {
            binding.btnLogout.visibility = View.VISIBLE
            binding.btnCalendar.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            editor.clear()
            editor.commit()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        // 캐러셀 관련 코드
        binding.textViewTotalBanner.text = numBanner.toString()
        binding.autoScrollViewPager.adapter = InfiniteAdapter(list)
        binding.autoScrollViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.autoScrollViewPager.setCurrentItem(currentPosition, false)

        binding.autoScrollViewPager.apply {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.textViewCurrentBanner.text = "${(position % 3) + 1}"
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state) {
                        // 뷰페이저에서 손 떼었을때 / 뷰페이저 멈춰있을 때
                        ViewPager2.SCROLL_STATE_IDLE -> autoScrollStart(intervalTime)
                        // 뷰페이저 움직이는 중
                        ViewPager2.SCROLL_STATE_DRAGGING -> autoScrollStop()
                    }
                }
            })
        }


        // 리사이클 관련 코드
        val nameList = mutableListOf("식품", "생활", "가전", "뷰티", "운동", "여행", "육아", "도서", "가구")
        val recyclerAdapter = RecyclerAdapter(nameList){ name->

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }

        val layoutManager = GridLayoutManager(this,3)
        binding.recyclerView1.layoutManager = layoutManager
        binding.recyclerView1.adapter = recyclerAdapter


        //달력 넘어가는 버튼
        binding.btnCalendar.setOnClickListener {
            val intent = Intent(this,CalendarActivity::class.java)
            startActivity(intent)
        }

    }

    private fun autoScrollStart(intervalTime: Long) {
        myHandler.removeMessages(0) // 이거 안하면 핸들러가 1개, 2개, 3개 ... n개 만큼 계속 늘어남
        myHandler.sendEmptyMessageDelayed(0, intervalTime) // intervalTime 만큼 반복해서 핸들러를 실행하게 함
    }

    private fun autoScrollStop(){
        myHandler.removeMessages(0) // 핸들러를 중지시킴
    }

    private inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            if(msg.what == 0) {
                binding.autoScrollViewPager.setCurrentItem(++currentPosition, true) // 다음 페이지로 이동
                autoScrollStart(intervalTime) // 스크롤을 계속 이어서 한다.
            }
        }
    }

    // 다른 페이지 갔다가 돌아오면 다시 스크롤 시작
    override fun onResume() {
        super.onResume()
        autoScrollStart(intervalTime)
    }

    // 다른 페이지로 떠나있는 동안 스크롤이 동작할 필요는 없음. 정지
    override fun onPause() {
        super.onPause()
        autoScrollStop()
    }

}