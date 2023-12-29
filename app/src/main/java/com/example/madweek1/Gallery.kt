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
interface OnImageClickListener {
    fun onImageClick(imageId: Int)
}
class Gallery : Fragment(), OnImageClickListener {
    val imageIds = listOf(
        R.drawable.a,
        R.drawable.b,
        R.drawable.c,
        R.drawable.d,
        R.drawable.e,
        R.drawable.f,
        R.drawable.g,
        R.drawable.h,
        R.drawable.i,
        R.drawable.j,
        R.drawable.k,
        R.drawable.l,
        R.drawable.m,
        R.drawable.n,
        R.drawable.o,
        R.drawable.p,
        R.drawable.q,
        R.drawable.r,
        R.drawable.s,
        R.drawable.t
    )
    var ImageList = arrayListOf<ImageItem>(
        ImageItem(2131165395,"emerald ocean", "Seogwipo", "2023-12-23", "galaxy 10"),
        ImageItem(2131165396, "stone island", "Jeju", "2023-12-24", "apple 20"),
        ImageItem(2131165397, "lights", "Daejeon", "2023-12-25", "olympus 55"),
        ImageItem(2131165398, "windmill", "Daejeon", "2023-12-26", "apple 20"),
        ImageItem(2131165399, "sky", "Jeju", "2023-12-25", "olympus 55"),
        ImageItem(2131165400, "lights", "Daejeon", "2023-12-25", "apple 20"),
        ImageItem(2131165401, "lights", "Seogwipo", "2023-12-23", "olympus 55"),
        ImageItem(2131165402, "lights", "Seogwipo", "2023-12-25", "apple 20"),
        ImageItem(2131165403, "lights", "Jeju", "2023-12-23", "olympus 55"),
        ImageItem(2131165404, "lights", "Daejeon", "2023-12-25", "galaxy 10"),
        ImageItem(2131165405, "seaOfMarseille", "Marseille", "2022-09-24", "iphone 12 pro"),
        ImageItem(2131165406, "sailing", "France", "2022-11-14", "canon"),
        ImageItem(2131165407, "Eiffel Tower", "Paris", "2023-01-01", "canon"),
        ImageItem(2131165408, "Chair and Man", "Paris", "2022-08-24", "iphone 12"),
        ImageItem(2131165409, "Library", "UC Berkely", "2023-08-30", "iphone 12"),
        ImageItem(2131165410, "pont", "Giverny", "2022-09-23", "canon"),
        ImageItem(2131165411, "Lyon", "Lyon", "2022-09-21", "iphone 12"),
        ImageItem(2131165412, "Rock n Sea", "Nice", "2022-09-03", "canon"),
        ImageItem(2131165413, "Metro", "Praha", "2022-12-23", "iphone 12"),
        ImageItem(2131165414, "Harbor", "Marseille", "2022-10-25", "canon"),

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
    override fun onImageClick(imageId: Int) {

        val bundle = Bundle()
        bundle.putInt("image_id", imageId)
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


class ImageAdapter(private val context: Context, private val imageIds: List<Int>, private val ImageList: List<ImageItem>, private val listener: OnImageClickListener) :
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
        holder.imageView.setImageResource(imageIds[position])
        holder.imageView.setOnClickListener {
            listener.onImageClick(imageIds[position])
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
        imageView.setImageResource(imageId)
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