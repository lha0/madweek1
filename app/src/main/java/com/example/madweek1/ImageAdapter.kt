import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.madweek1.ImageItem
import com.example.madweek1.ImageResource
import com.example.madweek1.OnImageClickListener

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