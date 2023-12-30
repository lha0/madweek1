package com.example.madweek1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.example.madweek1.databinding.ActivityMainBinding
import com.example.madweek1.databinding.FragmentPhonebookBinding
import org.json.JSONObject
import org.json.JSONArray


class PhoneBook : Fragment()
{
    var PhoneNumberList = arrayListOf<PhoneNumberItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_phonebook, container, false)
        val item: ListView = view.findViewById(R.id.listView)

        val jsonFileName = "phonebook.json"

        val json_object = readJSONFromAssets(requireContext(), jsonFileName)
        if (json_object != null) {
            jsonParsing(json_object)
        }

        val adapter = PhoneNumberAdapter(requireContext(), PhoneNumberList)
        item.adapter = adapter

        return view
    }

    private fun jsonParsing(json: JSONObject) {
        try {
            val phoneArray: JSONArray = json.getJSONArray("totalPhoneBook")

            for (i in 0 until phoneArray.length()) {
                val phoneObject: JSONObject = phoneArray.getJSONObject(i)
                val phone = PhoneNumberItem(phoneObject.getString("name"), phoneObject.getString("phone"))
                PhoneNumberList.add(phone)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readJSONFromAssets(context: Context, fileName: String): JSONObject? {
        val json: String?
        val jsonObject: JSONObject?

        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
            jsonObject = JSONObject(json)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }

        return jsonObject
    }
}

