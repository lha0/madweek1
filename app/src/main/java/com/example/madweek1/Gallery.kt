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

class ImageItem(val name: String, val loc: String, val date: String, val camera: String)
interface OnImageClickListener {
    fun onImageClick(imageId: Int)
}
class Gallery : Fragment(), OnImageClickListener {
    val imageIds = listOf(
        R.drawable.a,
        R.drawable.b,
        R.drawable.c,
    )
    var ImageList = arrayListOf<ImageItem>(
        ImageItem("emerald ocean", "Seogwipo", "2023-12-23", "galaxy 10"),
        ImageItem("stone island", "Jeju", "2023-12-24", "apple 20"),
        ImageItem("lights", "Daejeon", "2023-12-25", "olympus 55")
        )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val imageAdapter = ImageAdapter(requireContext(), imageIds, this)
        recyclerView.adapter = imageAdapter

        return view
    }
    override fun onImageClick(imageId: Int) {

        val bundle = Bundle()
        bundle.putInt("image_id", imageId)

        val detailFragment = DetailFragment()
        detailFragment.arguments = bundle

        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.recyclerView, detailFragment)
            ?.addToBackStack(null)
            ?.commit()
    }
}


class ImageAdapter(private val context: Context, private val imageIds: List<Int>, private val listener: OnImageClickListener) :
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
        val imageId = arguments?.getInt("image_id", 0) ?: 0
        imageView.setImageResource(imageId)
        return view
    }
}