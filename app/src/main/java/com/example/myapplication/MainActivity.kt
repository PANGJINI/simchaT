package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        window.statusBarColor = ContextCompat.getColor(this, R.color.pink)


        // 현재 사용자의 인증 상태를 확인
        val currentUser = mAuth.currentUser
        if (currentUser == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return //로그인되어 있지 않으면 이후의 코드 실행하지 않음
        }

        //뷰페이저에 어댑터 연결하기
        binding.viewpager.adapter = ViewPagerAdapter(this)

        var tabIconList = listOf(
            R.drawable.profile_icon_gray,
            R.drawable.chat_icon_gray,
            R.drawable.balance_icon_gray
        )

        //탭과 뷰페이저 연결하기
        var tabTextList = listOf("사용자", "채팅방", "밸런스게임")
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = tabTextList[position]
            tab.setIcon(tabIconList[position])
        }.attach()

        val switchToBalanceFragment = intent.getBooleanExtra("switch_to_balance_fragment", false)
        if (switchToBalanceFragment) {
            switchToBalanceFragment()
        }

        //버튼 누르면 로그아웃하기
        binding.btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("로그아웃")
            builder.setMessage("정말 로그아웃하시겠습니까?")

            builder.setPositiveButton("로그아웃") { dialog, which ->
                mAuth.signOut() // 로그아웃
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            builder.setNegativeButton("취소") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

    }//onCreate 끝

    class ViewPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
        private lateinit var viewPagerAdapter: ViewPagerAdapter
        val fragments = listOf<Fragment>(FragmentUserList(), FragmentChatRoomList(), FragmentBalance())

        //프래그먼트 페이지 수 반환
        override fun getItemCount(): Int = fragments.size

        //프래그먼트 객체 얻기
        override fun createFragment(position: Int): Fragment = fragments[position]

    }

    fun switchToBalanceFragment() {
        viewPager.currentItem = 2   //2번 인덱스 = 밸런스게임 프래그먼트
    }

}