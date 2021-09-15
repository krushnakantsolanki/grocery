package com.company.dreamgroceries.home.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.dreamgroceries.R
import com.company.dreamgroceries.data.Productimage
import com.company.dreamgroceries.extensions.loadImage
import kotlinx.android.synthetic.main.item_product_image.view.*

class ImagePagerAdapter(val productimages: List<Productimage>) :
    RecyclerView.Adapter<ImageHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_image, parent, false)
        return ImageHolder(view)
    }


    override fun getItemCount(): Int {
        return productimages.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {

        val productImage = productimages[position]
        holder.mView.ivProductImage.loadImage(productImage.image)

    }


}


class ImageHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
}
