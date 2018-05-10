package xyz.romakononovich.camera.presentation.gallery

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import android.widget.Toast
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
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun sharePhoto()
        fun deletePhoto()
        fun faceDetectPhoto()
        fun barcodePhoto()
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
                .apply(RequestOptions().transforms(RotateTransformation(90f)))
                .into(itemView.ivPhoto)
        Toast.makeText(context,position.toString()+" - "+getItemPosition(this),Toast.LENGTH_SHORT).show()
        clickListener?.apply {
            faceDetectPhoto()
            barcodePhoto()
            deletePhoto()
            sharePhoto()
        }
        container.addView(itemView)

        return itemView

    }



    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}