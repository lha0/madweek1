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
        ImageResource(0, R.drawable.a, "None"),
        ImageResource(1, R.drawable.b, "None"),
        ImageResource(2, R.drawable.c, "None"),
        ImageResource(3, R.drawable.d, "None"),
        ImageResource(4, R.drawable.e, "None"),
        ImageResource(5, R.drawable.f, "None"),
        ImageResource(6, R.drawable.g, "None"),
        ImageResource(7, R.drawable.h, "None"),
        ImageResource(8, R.drawable.i, "None"),
        ImageResource(9, R.drawable.j, "None"),
        ImageResource(10, R.drawable.k, "None"),
        ImageResource(11, R.drawable.l, "None"),
        ImageResource(12, R.drawable.m, "None"),
        ImageResource(13, R.drawable.n, "None"),
        ImageResource(14, R.drawable.o, "None"),
        ImageResource(15, R.drawable.p, "None"),
        ImageResource(16, R.drawable.q, "None"),
        ImageResource(17, R.drawable.r, "None"),
        ImageResource(18, R.drawable.s, "None"),
        ImageResource(19, R.drawable.t, "None"),
    )

    var ImageList = arrayListOf<ImageItem>(
        ImageItem(0,"emerald ocean", "Seogwipo", "2023-12-23", "galaxy 10"),
        ImageItem(1, "stone island", "Jeju", "2023-12-24", "apple 20"),
        ImageItem(2, "lights", "Daejeon", "2023-12-25", "olympus 55"),
        ImageItem(3, "windmill", "Daejeon", "2023-12-26", "apple 20"),
        ImageItem(4, "sky", "Jeju", "2023-12-25", "olympus 55"),
        ImageItem(5, "lights", "Daejeon", "2023-12-25", "apple 20"),
        ImageItem(6, "lights", "Seogwipo", "2023-12-23", "olympus 55"),
        ImageItem(7, "lights", "Seogwipo", "2023-12-25", "apple 20"),
        ImageItem(8, "lights", "Jeju", "2023-12-23", "olympus 55"),
        ImageItem(9, "lights", "Daejeon", "2023-12-25", "galaxy 10"),
        ImageItem(10, "seaOfMarseille", "Marseille", "2022-09-24", "iphone 12 pro"),
        ImageItem(11, "sailing", "France", "2022-11-14", "canon"),
        ImageItem(12, "Eiffel Tower", "Paris", "2023-01-01", "canon"),
        ImageItem(13, "Chair and Man", "Paris", "2022-08-24", "iphone 12"),
        ImageItem(14, "Library", "UC Berkely", "2023-08-30", "iphone 12"),
        ImageItem(15, "pont", "Giverny", "2022-09-23", "canon"),
        ImageItem(16, "Lyon", "Lyon", "2022-09-21", "iphone 12"),
        ImageItem(17, "Rock n Sea", "Nice", "2022-09-03", "canon"),
        ImageItem(18, "Metro", "Praha", "2022-12-23", "iphone 12"),
        ImageItem(19, "Harbor", "Marseille", "2022-10-25", "canon"),
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