import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.madweek1.ImageItem
import com.example.madweek1.ImageResource
import com.example.madweek1.OnImageClickListener
import com.example.madweek1.R
import com.bumptech.glide.Glide

class ImageAdapter(
    private val context: Context,
    private var imageIds: List<ImageResource>,
    private var imageList: List<ImageItem>,
    private val listener: OnImageClickListener
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

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
        val imageResource = imageIds[position]
        val imageItem = imageList.find { it.id == imageResource.id }

        if (imageResource.uri == "None") {
            holder.imageView.setImageResource(imageResource.address)
            Glide.with(context)
                .load(imageResource.address)
                .into(holder.imageView)

            holder.imageView.setOnClickListener {
                listener.onImageClick(imageResource.id, imageResource.address, "None")
            }
        } else {
            val uri = Uri.parse(imageResource.uri)
            Glide.with(context)
                .load(uri)
                .into(holder.imageView)

            holder.imageView.setOnClickListener {
                listener.onImageClick(imageResource.id, 0, imageResource.uri)
            }
        }
    }


    override fun getItemCount() = imageIds.size
}