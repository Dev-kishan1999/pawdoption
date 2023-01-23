//https://stackoverflow.com/questions/41156698/loading-images-in-recyclerview-with-picasso-from-api
//https://stackoverflow.com/questions/29991780/how-could-i-develop-an-image-carousel-on-android
//https://www.geeksforgeeks.org/android-image-slider-using-viewpager-in-kotlin/
package com.yadav.pawdoption.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.squareup.picasso.Picasso
import com.yadav.pawdoption.R

class PetDetailImageCorousalAdapter(private val context: Context, private var imageList: MutableList<String>) : PagerAdapter() {
    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =  (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.pet_detail_image_corousal_item, null)
        val ivImages = view.findViewById<ImageView>(R.id.ivPetDetailsImageCorousal)

        imageList[position].let {
            Picasso.with(context)
                .load(it)
                .into(ivImages);
        }

        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}