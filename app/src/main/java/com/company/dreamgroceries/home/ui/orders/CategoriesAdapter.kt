package com.company.dreamgroceries.home.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.company.dreamgroceries.R
import com.company.dreamgroceries.data.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoriesAdapter(val listener: CategoryClickListener)


    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mSelectedCategory: Category? = null
    private var mList: List<Category> = ArrayList()


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
        val category = mList[position]
        categoryHolder.tvCategory.text = category.name
        categoryHolder.tvCategory.isSelected = category.isSelected
        categoryHolder.tvCategory.setOnClickListener {
            onCategorySelected(categoryHolder,category)
            listener.onCategoryClicked(category,position)


        }

    }

    private fun onCategorySelected(
        categoryHolder: CategoryHolder,
        category: Category
    ) {
        if (mSelectedCategory != null) {
            mSelectedCategory?.isSelected = false
        }
        categoryHolder.tvCategory.isSelected = !categoryHolder.tvCategory.isSelected
        category.isSelected = categoryHolder.tvCategory.isSelected
        mSelectedCategory = category
        notifyDataSetChanged()
    }

    fun setList(categoryList: List<Category>?) {
        if (categoryList != null) {
            mList = categoryList
            notifyDataSetChanged()
        }

    }

    fun getSelectedCategoriesId(): IntArray {

        for (category in mList) {
            if (category.isSelected)
                return intArrayOf(category.id)
        }
        return IntArray(0)

    }


    inner class CategoryHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val tvCategory: TextView = mView.tvCategory

    }
}