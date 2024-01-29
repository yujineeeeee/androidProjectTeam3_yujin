package com.bitc.android_team3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitc.android_team3.Adapter.CategoryAdapter
import com.bitc.android_team3.Data.CategoryData
import com.bitc.android_team3.databinding.ActivityDetailBinding
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

private const val API_KEY = "fca10ff5a9273b3688ca93d0ed8e772b"
private const val BASE_URL = "http://openapi.11st.co.kr/openapi/OpenApiService.tmall?key=$API_KEY"
val dataCategory = mutableListOf<CategoryData>()

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)   //왼쪽 버튼 사용설정(기본은 뒤로가기)

        binding.headerLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.headerKeep.setOnClickListener {
            if (isUserLoggedIn()) {
                val intent = Intent(this,KeepActivity::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
        }

//        로그인된 상태에서는 로그인 버튼 사라짐
        if (isUserLoggedIn()) {
            binding.headerLogin.visibility = View.GONE
        }
        else {
            binding.headerLogin.visibility = View.VISIBLE
        }

        var value1 = intent.getStringExtra("name")
        binding.detailText.text = value1.toString()
        val updatedUrl = "$BASE_URL&apiCode=ProductSearch&keyword=$value1"

        dataCategory.clear()
        val thread = Thread(NetworkThread(updatedUrl))
        thread.start() // 쓰레드 시작
        thread.join()


        val categoryAdapter = CategoryAdapter(dataCategory)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 2)
        binding.cateRecyclerView.layoutManager = manager
        binding.cateRecyclerView.adapter = categoryAdapter


//        binding.categoPre.setOnClickListener {
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//        }

    }

    //    툴바
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

//            R.id.logoFragment -> {
//                // 툴바의 아이콘이 선택되었을 때 수행할 동작 정의
//                val intent = Intent(applicationContext, MainActivity::class.java)
//                startActivity(intent)
//                return true
//            }
//
//            R.id.LoginBtn -> {
//                // 툴바의 아이콘이 선택되었을 때 수행할 동작 정의
//                startActivity(Intent(applicationContext, LoginActivity::class.java))
//                return true
//            }
//
//            R.id.basketFragment -> {
//                // 장바구니 클릭 시 로그인 상태 확인
//                if (isUserLoggedIn()) {
//                    startActivity(Intent(applicationContext, KeepActivity::class.java))
//                } else {
//                    // 로그인 상태가 아니면 로그인 화면으로 이동
//                    startActivity(Intent(applicationContext, LoginActivity::class.java))
//                }
//                return true
//            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val id = sharedPref.getString("id", "")

        // id가 비어있지 않으면 로그인 상태로 판단
        return id?.isNotEmpty() == true
    }
}

class NetworkThread(
    var url: String): Runnable {

    override fun run() {

        try {

            val xml : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)

            xml.documentElement.normalize()

            //찾고자 하는 데이터가 어느 노드 아래에 있는지 확인
            val list: NodeList = xml.getElementsByTagName("Product")

            //list.length-1 만큼 얻고자 하는 태그의 정보를 가져온다
            for(i in 0..list.length-1){
                val n: Node = list.item(i)
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    val elem = n as Element
                    val map = mutableMapOf<String,String>()

                    // 이부분은 어디에 쓰이는지 잘 모르겠다.
                    for(j in 0..elem.attributes.length - 1) {
                        map.putIfAbsent(elem.attributes.item(j).nodeName, elem.attributes.item(j).nodeValue)
                    }
                    val ApiName = "${elem.getElementsByTagName("ProductName").item(0).textContent}"
                    val ApiJeongGa: Int = elem.getElementsByTagName("ProductPrice").item(0).textContent.toInt()
                    val ApiDiscount: Int = elem.getElementsByTagName("Discount").item(0).textContent.toInt()
                    val ApiImg = "${elem.getElementsByTagName("ProductImage110").item(0).textContent}"
                    val ApiImgBig =  "${elem.getElementsByTagName("ProductImage300").item(0).textContent}"
                    val ApiCd = "${elem.getElementsByTagName("ProductCode").item(0).textContent}"
                    val ApiPrice: Int = ApiJeongGa - ApiDiscount

                    dataCategory.add(CategoryData(name = ApiName, jeongGa = ApiJeongGa, price = ApiPrice, img = ApiImg, cd = ApiCd, imgBig =  ApiImgBig, discount = ApiDiscount))

                }
            }
        } catch (e: Exception) {
            Log.d("TTT", "오픈API ${e.toString()}")
        }
    }
}