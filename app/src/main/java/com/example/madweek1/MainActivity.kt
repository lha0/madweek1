package com.example.madweek1

import android.content.Context
import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout



class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private val REQUEST_CONTACTS = 1
    private val REQUEST_STORAGE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestContactsPermission()
    }

    private fun requestContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CONTACTS)
        } else {
            requestStoragePermission()
        }
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE)
        } else {
            setupViewPagerAndTabs()
        }
    }

    private fun setupViewPagerAndTabs() {
        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(PhoneBook(), "연락처")
        adapter.addFragment(Gallery(), "갤러리")
        adapter.addFragment(Picture(), "사진정보")

        viewPager = findViewById(R.id.pager)
        val tab: TabLayout = findViewById(R.id.tablayout)

        viewPager.adapter = adapter
        tab.setupWithViewPager(viewPager)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestStoragePermission()
            } else {
                requestStoragePermission()
            }
        } else if (requestCode == REQUEST_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupViewPagerAndTabs()
            } else {
                setupViewPagerAndTabs()
            }
        }
    }

    fun moveToTab(tabIndex: Int) {
        viewPager.setCurrentItem(tabIndex, true)
    }
}