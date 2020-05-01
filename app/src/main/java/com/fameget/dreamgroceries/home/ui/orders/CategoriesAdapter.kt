package com.fameget.dreamgroceries.home.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fameget.dreamgroceries.R
import kotlinx.android.synthetic.main.item_category.view.*

class CategoriesAdapter(list: List<String>)


    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mList: List<String> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryHolder(view)
    }


    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val categoryHolder: CategoryHolder = holder as CategoryHolder;
        categoryHolder.tvCategory.setText(mList.get(position))
        categoryHolder.tvCategory.setOnClickListener {

            categoryHolder.tvCategory.isSelected = !categoryHolder.tvCategory.isSelected

        }

    }


    inner class CategoryHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val tvCategory: TextView = mView.tvCategory

    }
}