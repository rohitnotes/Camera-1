package xyz.romakononovich.camera.presentation.gallery

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_pager.view.*
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.utils.RotateTransformation

/**
 * Created by RomanK on 06.05.18.
 */
class GalleryAdapter(context: Context) : PagerAdapter(){
    private lateinit var context: Context
    private lateinit var pathsList: List<String>
    private var clickListener: ClickListener? = null
    private val inflater = LayoutInflater.from(context)

    constructor(context: Context, images: List<String>, listener: ClickListener) : this(context) {
        this.context = context
        this.pathsList = images
        clickListener = listener
    }

    constructor(context: Context, images: List<String>) : this(context) {
        this.context = context
        this.pathsList = images
    }


    interface ClickListener {
        fun sharePhoto(imagePath: String)
        fun deletePhoto(imagePath: String)
        fun detailPhoto(imagePath: String)
        fun editPhoto(imagePath: String)
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return pathsList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = inflater.inflate(R.layout.item_pager, container, false)
        TransformationUtils.getExifOrientationDegrees(1)
        Glide.with(context)
                .load(pathsList[position])
                .apply(RequestOptions().transforms(RotateTransformation(90f)))
                .into(itemView.ivPhoto)

        container.addView(itemView)

        return itemView

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
        container.removeView(`object` as View)
    }

}