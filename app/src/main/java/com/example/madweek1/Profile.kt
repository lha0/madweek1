package com.example.madweek1

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import org.w3c.dom.Text

class Profile : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.profile, container, false)

        val name = view.findViewById<TextView>(R.id.profileName)
        val number = view.findViewById<TextView>(R.id.profileNumber)
        val button = view.findViewById<Button>(R.id.button)

        val phoneNumberItem = arguments?.getSerializable("phoneNumberItem") as PhoneNumberItem
        name.text = phoneNumberItem.name
        number.text = phoneNumberItem.number

        button.setOnClickListener {
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val beforeFragment = PhoneBook()

            // 프로필 프래그먼트를 팝업창으로 보여줍니다.
            fragmentManager.beginTransaction()
                .replace(R.id.popup, beforeFragment)
                .addToBackStack(null)
                .commit()
        }


        return view
    }

}