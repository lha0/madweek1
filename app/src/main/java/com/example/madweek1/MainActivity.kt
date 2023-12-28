package com.example.madweek1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(PhoneBook(), "연락처")
        adapter.addFragment(Gallery(), "갤러리")
        adapter.addFragment(Picture(), "사진정보")

        val viewpager: ViewPager = findViewById(R.id.pager)
        val tab: TabLayout = findViewById(R.id.tablayout)

        viewpager.adapter = adapter
        tab.setupWithViewPager(viewpager)
    }
}