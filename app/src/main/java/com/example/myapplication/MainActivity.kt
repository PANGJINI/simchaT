package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //액션바 설정
        supportActionBar?.title = "친구들"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFF7CAC9")))

        //뷰페이저에 어댑터 연결하기
        binding.viewpager.adapter = ViewPagerAdapter(this)

        //탭과 뷰페이저 연결하기
        var tabTextList = listOf("채팅방", "사용자")
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = tabTextList[position]
        }.attach()
    }//onCreate 끝

    class ViewPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
        private lateinit var viewPagerAdapter: ViewPagerAdapter
        val fragments = listOf<Fragment>(FragmentUserList(), FragmentChatList())

        //프래그먼트 페이지 수 반환
        override fun getItemCount(): Int = fragments.size

        //프래그먼트 객체 얻기
        override fun createFragment(position: Int): Fragment = fragments[position]

    }

    //생성할 메뉴를 지정하는 함수
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //메뉴 아이템 선택 기능 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            mAuth.signOut()     //로그아웃
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return true
    }
}