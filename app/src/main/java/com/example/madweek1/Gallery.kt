package com.example.madweek1

import ImageAdapter
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Parcelable

import android.util.Log
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.parcelize.Parcelize
import android.provider.MediaStore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
class Gallery : Fragment(), OnImageClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var switch: Switch
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        switch = view.findViewById(R.id.view)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val imageAdapter = ImageAdapter(requireContext(), images.imageIds, images.ImageList, this)



        recyclerView.adapter = imageAdapter

        switch.setOnCheckedChangeListener { _, isChecked ->
            toggleLayoutManager(isChecked)


        }

        val fab: FloatingActionButton = view.findViewById(R.id.plus)
        fab.setOnClickListener {
            openGalleryForImage()
        }
        val gal_camera = fetchCameraImages()
        println(gal_camera)
        gal_camera.forEach { uri ->
            val id = images.imageIds.size + 1
            val NewId = ImageResource(id, 0, uri.toString())
            images.imageIds.add(NewId)
            val NewInfo = ImageItem(id, "Image $id", "Gallery/Camera", "Unknown", "Unknown")
            images.ImageList.add(NewInfo)
        }

        printImageItemList(images.ImageList)

        return view
    }

    private fun fetchCameraImages(): List<Uri> {
        val imageList = mutableListOf<Uri>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN
        )

        // Query for images
        val cursor = requireActivity().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_TAKEN} DESC" // 최신 이미지부터 정렬
        )

        cursor?.use { c ->
            val idColumn = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = c.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (c.moveToNext()) {
                val id = c.getLong(idColumn)
                val name = c.getString(nameColumn)
                val date = c.getLong(dateColumn)
                val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageList.add(contentUri)
                images.imageIds.add(ImageResource(id.toInt(), 0, contentUri.toString()))
                images.ImageList.add(ImageItem(id.toInt(), name, "Gallery/Camera", convertLongToDate(date), "Unknown"))
            }
        } ?: Log.e("ImageQuery", "Cursor is null or failed to move")

        return imageList
    }

    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }
    fun printImageItemList(imageList: List<ImageItem>) {
        imageList.forEach { item ->
            Log.d("ImageItemLog", "ID: ${item.id}, Name: ${item.name}, Location: ${item.location}")
        }
    }
    override fun onImageClick(imageId: Int, imageAddress: Int, uri: String) {

        val bundle = Bundle()
        bundle.putInt("image_id", imageId)

        if (uri == "None") {
            bundle.putInt("image_address", imageAddress)
        } else {
            bundle.putString("image_address", uri)
        }

        bundle.putParcelableArrayList("image_list", images.ImageList)

        val detailFragment = Picture()
        detailFragment.arguments = bundle

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.detailPicture, detailFragment) // R.id.fragmentContainer를 실제 FrameLayout ID로 변경하세요.
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        (activity as? MainActivity)?.moveToTab(2)
    }
    private fun toggleLayoutManager(isGrid: Boolean) {
        if (isGrid) {
            // GridLayoutManager로 변경
            recyclerView.layoutManager = GridLayoutManager(context, 2) // 2는 열의 수입니다.
        } else {
            // LinearLayoutManager로 변경
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }
    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            val NewImage: String = selectedImageUri.toString()
            println(NewImage)

            val NewId = ImageResource(images.imageIds.size + 1, 0, NewImage)
            images.imageIds.add(NewId)

            val NewInfo = ImageItem(images.ImageList.size + 1, NewImage, "somewhere", "2000-01-01", "카메라 모델")
            images.ImageList.add(NewInfo)


            val bundle = Bundle()

            bundle.putInt("image_id", NewId.id)
            bundle.putString("image_address", NewImage)
            bundle.putParcelableArrayList("image_list", images.ImageList)

            val detailFragment = Picture()
            detailFragment.arguments = bundle

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.detailPicture, detailFragment) // R.id.fragmentContainer를 실제 FrameLayout ID로 변경하세요.
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

            (activity as? MainActivity)?.moveToTab(2)

        }
    }
}



