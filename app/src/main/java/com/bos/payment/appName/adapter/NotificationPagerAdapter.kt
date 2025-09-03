import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bos.payment.appName.R
import com.squareup.picasso.Picasso

class NotificationPagerAdapter(
    private val context: Context,
    private val imageUrls: List<String?>
) : PagerAdapter() {

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_view, container, false)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        // Load the image using Picasso
        val imageUrl = imageUrls[position]
        Picasso.get()
            .load(imageUrl)
            .error(R.drawable.no_image)  // Fallback image if loading fails
            .into(imageView)

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
