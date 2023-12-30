package com.example.madweek1

import android.content.Context
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
import android.widget.TextView
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
    val address: Int
) : Parcelable



interface OnImageClickListener {
    fun onImageClick(imageId: Int, imageAddress: Int)
}
class Gallery : Fragment(), OnImageClickListener {
    val imageIds = arrayListOf<ImageResource>(
        ImageResource(0, R.drawable.a),
        ImageResource(1, R.drawable.b),
        ImageResource(2, R.drawable.c),
        ImageResource(3, R.drawable.d),
        ImageResource(4, R.drawable.e),
        ImageResource(5, R.drawable.f),
        ImageResource(6, R.drawable.g),
        ImageResource(7, R.drawable.h),
        ImageResource(8, R.drawable.i),
        ImageResource(9, R.drawable.j),
        ImageResource(10, R.drawable.k),
        ImageResource(11, R.drawable.l),
        ImageResource(12, R.drawable.m),
        ImageResource(13, R.drawable.n),
        ImageResource(14, R.drawable.o),
        ImageResource(15, R.drawable.p),
        ImageResource(16, R.drawable.q),
        ImageResource(17, R.drawable.r),
        ImageResource(18, R.drawable.s),
        ImageResource(19, R.drawable.t),
    )

    var ImageList = arrayListOf<ImageItem>(
        ImageItem(0,"emerald ocean", "Seogwipo", "2023-12-23", "galaxy 10"),
        ImageItem(1, "stone island", "Jeju", "2023-12-24", "apple 20"),
        ImageItem(2, "lights", "Daejeon", "2023-12-25", "olympus 55"),
        ImageItem(3, "windmill", "Daejeon", "2023-12-26", "apple 20"),
        ImageItem(4, "sky", "Jeju", "2023-12-25", "olympus 55"),
        ImageItem(5, "lights", "Daejeon", "2023-12-25", "apple 20"),
        ImageItem(6, "lights", "Seogwipo", "2023-12-23", "olympus 55"),
        ImageItem(7, "lights", "Seogwipo", "2023-12-25", "apple 20"),
        ImageItem(8, "lights", "Jeju", "2023-12-23", "olympus 55"),
        ImageItem(9, "lights", "Daejeon", "2023-12-25", "galaxy 10"),
        ImageItem(10, "seaOfMarseille", "Marseille", "2022-09-24", "iphone 12 pro"),
        ImageItem(11, "sailing", "France", "2022-11-14", "canon"),
        ImageItem(12, "Eiffel Tower", "Paris", "2023-01-01", "canon"),
        ImageItem(13, "Chair and Man", "Paris", "2022-08-24", "iphone 12"),
        ImageItem(14, "Library", "UC Berkely", "2023-08-30", "iphone 12"),
        ImageItem(15, "pont", "Giverny", "2022-09-23", "canon"),
        ImageItem(16, "Lyon", "Lyon", "2022-09-21", "iphone 12"),
        ImageItem(17, "Rock n Sea", "Nice", "2022-09-03", "canon"),
        ImageItem(18, "Metro", "Praha", "2022-12-23", "iphone 12"),
        ImageItem(19, "Harbor", "Marseille", "2022-10-25", "canon"),
        )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val imageAdapter = ImageAdapter(requireContext(), imageIds, ImageList, this)
        recyclerView.adapter = imageAdapter

        return view
    }
    override fun onImageClick(imageId: Int, imageAddress: Int) {

        val bundle = Bundle()
        bundle.putInt("image_id", imageId)
        bundle.putInt("image_address", imageAddress)
        bundle.putParcelableArrayList("image_list", ImageList)

        val detailFragment = DetailFragment()
        detailFragment.arguments = bundle

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.detailPicture, detailFragment) // R.id.fragmentContainer를 실제 FrameLayout ID로 변경하세요.
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        (activity as? MainActivity)?.moveToTab(2)
    }
}


class ImageAdapter(private val context: Context, private val imageIds: List<ImageResource>, private val ImageList: List<ImageItem>, private val listener: OnImageClickListener) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    class ViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
            adjustViewBounds = true
        }

        return ViewHolder(imageView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(imageIds.map { it.address }[position])
        holder.imageView.setOnClickListener {
            listener.onImageClick(imageIds.map { it.id }[position], imageIds.map { it.address }[position])
        }
    }

    override fun getItemCount() = imageIds.size
}

class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_picture, container, false)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val nameTextView: TextView = view.findViewById(R.id.name)
        val locTextView: TextView = view.findViewById(R.id.loc)
        val dateTextView: TextView = view.findViewById(R.id.date)
        val cameraTextView: TextView = view.findViewById(R.id.camera)

        val imageId = arguments?.getInt("image_id", 0) ?: 0
        val imageAdress = arguments?.getInt("image_address", 0) ?: 0
        imageView.setImageResource(imageAdress)
        println(imageId)

        val imageItems: ArrayList<ImageItem>? = arguments?.getParcelableArrayList("image_list")

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
}