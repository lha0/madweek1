package com.example.madweek1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class PhoneBook : Fragment()
{
    var PhoneNumberList = arrayListOf<PhoneNumberItem>(
        PhoneNumberItem("홍길동", "010-0000-0000"),
        PhoneNumberItem("윤영호", "010-0000-0000"),
        PhoneNumberItem("임세진", "010-0000-0000"),
        PhoneNumberItem("한주희", "010-0000-0000"),
        PhoneNumberItem("서동민", "010-0000-0000"),
        PhoneNumberItem("손현우", "010-0000-0000"),


    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_phonebook, container, false)
        val item: ListView = view.findViewById(R.id.listView)

        val adapter = PhoneNumberAdapter(requireContext(), PhoneNumberList)
        item.adapter = adapter
        return view
    }
}