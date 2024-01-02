package com.example.madweek1

import android.content.ContentUris
import android.content.Context
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

object images {
    val imageIds = arrayListOf<ImageResource>(
        //ImageResource(0, R.drawable.a, "None"),
    )
    var no_change_imageIds = arrayListOf<ImageResource>(
        //ImageResource(0, R.drawable.a, "None"),
    )
    var ImageList = arrayListOf<ImageItem>(
        //ImageItem(0,"emerald ocean", "Seogwipo", "2023-12-23", "galaxy 10"),

    )
}

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

        requestPermission()
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