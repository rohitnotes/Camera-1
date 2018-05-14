package xyz.romakononovich.camera.presentation.gallery

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import kotlinx.android.synthetic.main.item_pager.view.*
import xyz.romakononovich.camera.R

/**
 * Created by RomanK on 06.05.18.
 */
class GalleryAdapter(context: Context) : PagerAdapter() {
    private lateinit var context: Context
    private lateinit var pathsList: MutableList<String>
    private var clickListener: ClickListener? = null
    private val inflater = LayoutInflater.from(context)

    constructor(context: Context, images: List<String>, listener: ClickListener) : this(context) {
        this.context = context
        this.pathsList = images.toMutableList()
        clickListener = listener
    }

    fun delete(id: Int) {
        pathsList.removeAt(id)
        if (pathsList.size == 0) run {
            clickListener?.onLastPageDelete()
        }
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onClickViewPager()
        fun onLastPageDelete()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return pathsList.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = inflater.inflate(R.layout.item_pager, container, false)
        TransformationUtils.getExifOrientationDegrees(1)
        Glide.with(context)
                .load(pathsList[position])
                .into(itemView.ivPhoto)
        itemView.setOnClickListener {
            clickListener?.onClickViewPager()
        }
        container.addView(itemView)

        return itemView

    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}