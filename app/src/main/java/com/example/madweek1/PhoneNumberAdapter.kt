package com.example.madweek1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class PhoneNumberAdapter (val context: Context, val PhoneNumberList: ArrayList<PhoneNumberItem>): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View = LayoutInflater.from(context).inflate(R.layout.phonebook_item, null)

        //id로 부터 view를 찾아라
        val name = view.findViewById<TextView>(R.id.name)
        val number = view.findViewById<TextView>(R.id.phoneNumber)

        val phone = PhoneNumberList[position]

        name.text= phone.name
        number.text = phone.number

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