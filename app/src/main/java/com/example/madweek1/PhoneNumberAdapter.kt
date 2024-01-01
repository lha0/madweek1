package com.example.madweek1

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PhoneNumberAdapter (val context: Context, val PhoneNumberList: ArrayList<PhoneNumberItem>): BaseAdapter() {
    private val WRITE_CONTACTS_REQUEST_CODE = 1002
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

        layout.setOnLongClickListener {
            // 롱클릭 이벤트 핸들러 - 삭제 다이얼로그 띄우기
            showDeleteConfirmationDialog(position)
            true
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

    private fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("삭제 확인")
            .setMessage("삭제하시겠습니까?")
            .setPositiveButton("삭제") {_, _ ->
                deleteContactFromPhoneBook(position)
                deleteContact(position)
            }
            .setNegativeButton("취소") {
                _, _ ->
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteContact(position: Int) {
        PhoneNumberList.removeAt(position)
        notifyDataSetChanged()
    }

    private fun deleteContactFromPhoneBook(position: Int) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용된 경우
            val contentResolver: ContentResolver = context.contentResolver
            val where = "${ContactsContract.RawContacts.CONTACT_ID} = ?"
            val selectionArgs: Array<String> = arrayOf(PhoneNumberList[position].id.toString())
            contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, where, selectionArgs)
        } else {
            // 사용자에게 권한을 요청
            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.WRITE_CONTACTS), WRITE_CONTACTS_REQUEST_CODE)
        }
    }



}