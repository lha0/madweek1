package com.example.madweek1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout



class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(PhoneBook(), "연락처")
        adapter.addFragment(Gallery(), "갤러리")
        adapter.addFragment(Picture(), "사진정보")

        viewPager = findViewById(R.id.pager)
        val tab: TabLayout = findViewById(R.id.tablayout)

        viewPager.adapter = adapter
        tab.setupWithViewPager(viewPager)

        //requestPermission()
    }

    fun moveToTab(tabIndex: Int) {
        viewPager.setCurrentItem(tabIndex, true)
    }
    private fun requestPermission() {
        val locationResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (!it) {
                Toast.makeText(this, "스토리지에 접근 권한을 허가해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        locationResultLauncher.launch(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}