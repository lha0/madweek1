package com.example.madweek1

import ImageAdapter
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentUris
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.io.File

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
    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 1
    private val IMAGE_PICK_CODE = 1000

    private val galleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*"
                )
                fetchCameraImages()
            }else
                Log.d(TAG, "deny")
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        switch = view.findViewById(R.id.view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            galleryPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
        else
            galleryPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)


        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        imageAdapter = ImageAdapter(requireContext(), images.imageIds, images.ImageList, this)

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
                val filteredImages = filterImagesByDate(v.text.toString(), images.ImageList)
                updateGalleryWithImages(filteredImages)
                true
            } else {
                false
            }
        }


        return view
    }
    fun filterImagesByDate(date: String, imageList: List<ImageItem>): List<ImageItem> {
        // 날짜 형식은 "yyyy-MM-dd"를 가정
        return imageList.filter { it.date == date }
    }

    fun updateGalleryWithImages(filteredImages: List<ImageItem>) {
        // ImageAdapter에 이미지 리스트를 업데이트하고 RecyclerView를 새로 고침
        images.ImageList = ArrayList(filteredImages)
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

                if (imageList.size <= 50) {
                    imageList.add(contentUri)
                }
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
//        openGalleryResult(intent)
    }

    private fun openGalleryResult(data: Intent?) {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { requestCode ->
            Log.d(TAG, "imageLauncher enter!!")
            if (requestCode.resultCode == Activity.RESULT_OK) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

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
