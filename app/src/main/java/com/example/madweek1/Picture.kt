package com.example.madweek1

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class Picture : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val defaultImageItems = arrayListOf(ImageItem(0, "emerald ocean", "Seogwipo", "2023-12-23", "galaxy 10"))

        val view = inflater.inflate(R.layout.fragment_picture, container, false)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val nameTextView: TextView = view.findViewById(R.id.name)
        val locTextView: TextView = view.findViewById(R.id.loc)
        val dateTextView: TextView = view.findViewById(R.id.date)
        val cameraTextView: TextView = view.findViewById(R.id.camera)

        val imageId = arguments?.getInt("image_id", 0) ?: 0
        val imageAdress = arguments?.getInt("image_address", 0) ?: R.drawable.a
        val imageAdress_gal = arguments?.getString("image_address", "None") ?:"None"

        nameTextView.setOnClickListener { showEditDialog("name", nameTextView) }
        locTextView.setOnClickListener { showEditDialog("date", locTextView) }
        dateTextView.setOnClickListener { showEditDialog("location", dateTextView) }

        imageView.setImageResource(imageAdress)
        if (imageAdress_gal != "None") {
            val uri = Uri.parse(imageAdress_gal)
            imageView.setImageURI(uri)
        }
        //println(imageId)

        val imageItems: ArrayList<ImageItem> = arguments?.getParcelableArrayList("image_list") ?: defaultImageItems

        // ArrayList의 각 ImageItem에서 String 속성을 얻음
        imageItems?.forEach { imageItem ->
            if (imageId == imageItem.id) {
                nameTextView.text = imageItem.name
                locTextView.text = imageItem.location
                dateTextView.text = imageItem.date
                cameraTextView.text = imageItem.camera
            }
        }

        return view
    }
    private fun showEditDialog(field: String, textView: TextView) {
        val editText = EditText(context)
        editText.setText(textView.text)

        AlertDialog.Builder(context)
            .setTitle("Edit $field")
            .setView(editText)
            .setPositiveButton("Save") { dialog, which ->
                textView.text = editText.text.toString()

                // Here you can also update the model or database with the new value
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

}