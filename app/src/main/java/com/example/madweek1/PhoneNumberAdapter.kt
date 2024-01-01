package com.example.madweek1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PhoneNumberAdapter (val context: Context, val PhoneNumberList: ArrayList<PhoneNumberItem>): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View = LayoutInflater.from(context).inflate(R.layout.phonebook_item, null)

        //id로 부터 view를 찾아라
        val layout = view.findViewById<LinearLayout>(R.id.phoneItemLayout)
        val name = view.findViewById<TextView>(R.id.name)

        val phone = PhoneNumberList[position]

        name.text= phone.name

        layout.setOnClickListener {
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val profileFragment = Profile()

            // 클릭한 아이템 정보를 Bundle에 담아 프로필 프래그먼트로 전달
            val args = Bundle()
            args.putParcelable("phoneNumberItem", PhoneNumberList[position])
            profileFragment.arguments = args

            // 프로필 프래그먼트를 팝업창으로 보여줍니다.
            fragmentManager.beginTransaction()
                .replace(R.id.mainPhoneBook, profileFragment) // fragment_container는 프로필을 보여줄 레이아웃의 ID입니다.
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    override fun getCount(): Int {
        return PhoneNumberList.size

    }

    override fun getItem(position: Int): Any {
        return PhoneNumberList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }


}