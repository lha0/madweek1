package com.example.madweek1

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract

import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class PhoneBook : Fragment()
{
    var PhoneNumberList = arrayListOf<PhoneNumberItem>()
    private val READ_CONTACTS_PERMISSION_REQUEST = 1
    private val ADD_CONTACT_REQUEST_CODE = 1001
    lateinit var item: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_phonebook, container, false)
        item = view.findViewById(R.id.listView)

        loadContacts()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add Contact 버튼 클릭 시, 연락처 추가 Intent 시작
        view.findViewById<Button>(R.id.Addbutton).setOnClickListener {
            val addContactIntent = Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(addContactIntent, ADD_CONTACT_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // 연락처 추가 후, 목록을 다시 불러옴
            loadContacts()
        }
    }

    private fun loadContacts() {
        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        PhoneNumberList = arrayListOf<PhoneNumberItem>()

        cursor?.let {
            while (it.moveToNext()) {
                val contactid = it.getInt(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                PhoneNumberList.add(PhoneNumberItem(contactid, name, number)) // 이름과 번호를 문자열로 합쳐서 리스트에 추가
            }

            val adapter = PhoneNumberAdapter(requireContext(), PhoneNumberList)
            item.adapter = adapter
        }

        cursor?.close()
    }

}

