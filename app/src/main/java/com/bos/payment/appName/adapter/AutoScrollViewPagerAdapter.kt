package com.bos.payment.appName.adapter
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bos.payment.appName.R

class AutoScrollViewPagerAdapter(private val context: Context, private val images: ArrayList<Int>) : PagerAdapter() {

    private val autoScrollDelay = 2000L // Delay between auto-scrolling in milliseconds
    private var currentPosition = 0
    private var isAutoScrollEnabled = false
    private var viewPager: ViewPager? = null
    private val handler = Handler(Looper.getMainLooper())
    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            if (currentPosition == count) {
                currentPosition = 0
                viewPager?.setCurrentItem(currentPosition, true)
            } else {
                viewPager?.setCurrentItem(currentPosition++, true)
            }
            handler.postDelayed(this, autoScrollDelay)
        }
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view, container, false)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        imageView.setImageResource(images[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun startAutoScroll(viewPager: ViewPager) {
        this.viewPager = viewPager
        isAutoScrollEnabled = true
        handler.postDelayed(autoScrollRunnable, autoScrollDelay)
    }

    fun stopAutoScroll() {
        isAutoScrollEnabled = false
        handler.removeCallbacks(autoScrollRunnable)
    }

    fun isAutoScrollEnabled(): Boolean {
        return isAutoScrollEnabled
    }

    fun setupDotsLayout(dotsLayout: LinearLayout) {
        val dots = arrayOfNulls<ImageView>(count)

        for (i in 0 until count) {
            dots[i] = ImageView(context)
            dots[i]?.setImageResource(R.drawable.dot_indicator_inactive)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            dotsLayout.addView(dots[i], params)
        }

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                setCurrentDot(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        //setCurrentDot(0)
    }

    private fun setCurrentDot(position: Int) {
        val dotsLayout = viewPager?.parent as LinearLayout

        for (i in 0 until dotsLayout.childCount) {
            val dot = dotsLayout.getChildAt(i) as ImageView
            dot.setImageResource(R.drawable.dot_indicator_inactive)
        }

        val currentDot = dotsLayout.getChildAt(position) as ImageView
        currentDot.setImageResource(R.drawable.dot_indicator_active)
    }
}