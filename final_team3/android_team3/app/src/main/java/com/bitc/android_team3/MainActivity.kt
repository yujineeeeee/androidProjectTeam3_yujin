package com.bitc.android_team3

import HomeFragment
import MyPageFragment
import SearchFragment
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bitc.android_team3.databinding.ActivityMainBinding

private const val TAG_HOME = "home_fragment"
private const val TAG_SEARCH = "search_fragment"
private const val TAG_MY_PAGE = "my_page_fragment"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.logo.bringToFront()

        // 툴바 설정
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


//        프레그먼트
        setFragment(TAG_HOME, HomeFragment())

        binding.bottomTabs.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> setFragment(TAG_HOME, HomeFragment())
                R.id.searchFragment -> setFragment(TAG_SEARCH, SearchFragment())
//                R.id.myPageFragment -> setFragment(TAG_MY_PAGE, MyPageFragment())
                R.id.myPageFragment -> {
                    // 마이페이지 클릭 시 로그인 상태 확인
                    if (isUserLoggedIn()) {
                        setFragment(TAG_MY_PAGE, MyPageFragment())
                    } else {
                        // 로그인 상태가 아니면 로그인 화면으로 이동
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                }
            }
            true
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
            R.id.logoFragment -> {
                // 툴바의 아이콘이 선택되었을 때 수행할 동작 정의
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                return true
            }

//            R.id.basketFragment -> {
//                val intent = Intent(applicationContext, BasketActivity::class.java)
//                startActivity(intent)
//                return true
//            }

            R.id.basketFragment -> {
                // 장바구니 클릭 시 로그인 상태 확인
                if (isUserLoggedIn()) {
                    startActivity(Intent(applicationContext, KeepActivity::class.java))
                } else {
                    // 로그인 상태가 아니면 로그인 화면으로 이동
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                }
                return true
            }

            android.R.id.home -> {
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val transaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            transaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val category = manager.findFragmentByTag(TAG_SEARCH)
        val myPage = manager.findFragmentByTag(TAG_MY_PAGE)

        if (home != null) {
            transaction.hide(home)
        }

        if (category != null) {
            transaction.hide(category)
        }

        if (myPage != null) {
            transaction.hide(myPage)
        }

        if (tag == TAG_HOME) {
            if (home != null) {
                transaction.show(home)
            }
        } else if (tag == TAG_SEARCH) {
            if (category != null) {
                transaction.show(category)
            }
        } else if (tag == TAG_MY_PAGE) {
            if (myPage != null) {
                transaction.show(myPage)
            }
        }

        transaction.commitAllowingStateLoss()
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val id = sharedPref.getString("id", "")

        // id가 비어있지 않으면 로그인 상태로 판단
        return id?.isNotEmpty() == true
    }


}