package com.example.madweek1

import ImageAdapter
import android.app.Activity

import android.content.ContentUris
import android.content.ContentValues.TAG

import android.content.Intent

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import android.util.Log
import android.widget.Switch

import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

import android.provider.MediaStore
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Gallery : Fragment(), OnImageClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var switch: Switch
    private lateinit var imageAdapter: ImageAdapter
    private val IMAGE_PICK_CODE = 1000

    private val galleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*"
                )
                fetchCameraImages()
            } else
                Log.d(TAG, "deny")
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        switch = view.findViewById(R.id.view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            galleryPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
        else
            galleryPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)


        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        imageAdapter = ImageAdapter(requireContext(), images.imageIds, this)

        recyclerView.adapter = imageAdapter

        switch.setOnCheckedChangeListener { _, isChecked ->
            toggleLayoutManager(isChecked)
        }

        val fab: FloatingActionButton = view.findViewById(R.id.plus)
        val dateFilterEditText: EditText = view.findViewById(R.id.dateFilterEditText)
        fab.setOnClickListener {
            openGalleryForImage()
        }


        dateFilterEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // 날짜에 해당되는 이미지만 필터링하여 표시
                val filteredImages =
                    filterImagesByDate(v.text.toString(), images.ImageList, images.imageIds)
                updateGalleryWithImages(filteredImages)
                true
            } else {
                false
            }
        }
        imageAdapter.notifyDataSetChanged()
        return view
    }

    fun filterImagesByDate(
        date: String,
        imageList: List<ImageItem>,
        imageIds: List<ImageResource>
    ): List<ImageResource> {

        //val dateFormatRegex = Regex("\\d{4}-\\d{2}-\\d{2}")
        val onlydateFormat = Regex("\\d{8}")
        var filteredImageIds = listOf<ImageResource>()

        if (onlydateFormat.matches(date)) {
            var new_date =
                date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6)
            var imageList_date = imageList.filter { it.date == new_date }
            filteredImageIds = imageIds.filter { imageResource ->
                imageList_date.any { imageItem ->
                    imageItem.id == imageResource.id
                }
            }

        }

        if (!onlydateFormat.matches(date)) {
            filteredImageIds = images.no_change_imageIds
        }

        return filteredImageIds
    }


    fun updateGalleryWithImages(filteredImages: List<ImageResource>) {
        // ImageAdapter에 이미지 리스트를 업데이트하고 RecyclerView를 새로 고침
        imageAdapter.imageIds = ArrayList(filteredImages)
        imageAdapter.notifyDataSetChanged()
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

        // imagesIds 의 사본을 no_chage_imageIds로 저장
        images.no_change_imageIds = images.imageIds

        return imageList
    }

    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

    override fun onImageClick(imageId: Int, imageAddress: Int, uri: String) {
        val bundle = Bundle()

        bundle.putInt("image_id", imageId)

        // drawable 안의 image를 R.drawable.a 식으로 띄우는 경우, int 형 주소를 호출 | 갤러리에서 uri로 가져온 image의 경우 string uri로 직접 호출
        if (uri == "None") {
            bundle.putInt("image_address", imageAddress)
        } else {
            bundle.putString("image_address", uri)
        }

        // bundle에 imageList 전달 (전체 이미지 정보 포함하는 리스트)
        bundle.putParcelableArrayList("image_list", images.ImageList)

        val detailFragment = Picture()
        detailFragment.arguments = bundle

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.detailPicture, detailFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        // 사진 정보 탭으로 전환
        (activity as? MainActivity)?.moveToTab(2)
    }

    private fun toggleLayoutManager(isGrid: Boolean) {
        if (isGrid) {
            // GridLayoutManager로 변경
            recyclerView.layoutManager = GridLayoutManager(context, 2)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            val NewImage: String = selectedImageUri.toString()
            println(NewImage)

            val NewId = ImageResource(images.imageIds.size + 1, 0, NewImage)
            images.imageIds.add(NewId)

            val NewInfo = ImageItem(images.ImageList.size + 1, NewImage, "somewhere", "2000-01-01", "카메라 모델")
            images.ImageList.add(NewInfo)

            onImageClick(NewId.id, 0, NewImage)

        }

    }

}
