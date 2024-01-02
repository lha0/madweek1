package com.example.madweek1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageItem(
    val id: Int,
    val name: String,
    val location: String,
    val date: String,
    val camera: String
) : Parcelable

@Parcelize
data class ImageResource(
    val id: Int,
    val address: Int,
    val uri : String
) : Parcelable


interface OnImageClickListener {
    fun onImageClick(imageId: Int, imageAddress: Int, uri: String)
}
object images {
    val imageIds = arrayListOf<ImageResource>(
        //ImageResource(0, R.drawable.a, "None"),
    )
    var no_change_imageIds = arrayListOf<ImageResource>(
        //ImageResource(0, R.drawable.a, "None"),
    )
    var ImageList = arrayListOf<ImageItem>(
        //ImageItem(0,"emerald ocean", "Seogwipo", "2023-12-23", "galaxy 10"),

    )
}