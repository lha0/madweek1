package com.example.madweek1

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import org.w3c.dom.Text

class Profile : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.profile, container, false)
        val CALL_REQUEST_CODE = 1

        val name = view.findViewById<TextView>(R.id.profileName)
        val number = view.findViewById<TextView>(R.id.profileNumber)
        val listbutton = view.findViewById<Button>(R.id.listbutton)
        val callbutton = view.findViewById<Button>(R.id.callbutton)

        val phoneNumberItem = arguments?.getParcelable<PhoneNumberItem>("phoneNumberItem")
        name.text = phoneNumberItem?.name
        number.text = phoneNumberItem?.number

        listbutton.setOnClickListener {
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val beforeFragment = PhoneBook()

            // 목록으로 돌아가기
            fragmentManager.beginTransaction()
                .replace(R.id.popup, beforeFragment)
                .addToBackStack(null)
                .commit()
        }

        callbutton.setOnClickListener{
                var intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel: "+ phoneNumberItem?.number)
                startActivity(intent)
        }


        return view
    }

}