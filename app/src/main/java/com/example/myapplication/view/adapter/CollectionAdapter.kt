package com.example.myapplication.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemCategoryBinding
import com.example.myapplication.model.dataModels.CategoryItem
import com.google.android.material.internal.CollapsingTextHelper
import java.sql.Array

interface CollectionInterface {
    fun goToCategory(title: String)
}

class CategoriesAdapter(
    val collectionInterface: CollectionInterface,
    colTitleList: List<String>
) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private val drawList = listOf(
        R.drawable.col_disney,
        R.drawable.col_drama,
        R.drawable.col_old,
        R.drawable.col_asian,
    )

    private val collectionList = convertToColItem(colTitleList)

    override fun getItemCount(): Int = collectionList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val col = collectionList[position]
        holder.bind(col)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCategoryBinding.bind(view)
        fun bind(col: CategoryItem) = with(binding) {
            itemCategoryImage.setImageResource(col.color)
            itemCategoryTitle.text = col.title
            itemCategoryLay.setOnClickListener {
                collectionInterface.goToCategory(col.title)
            }
        }
    }

    private fun convertToColItem(list: List<String>): List<CategoryItem>{
        val newList = ArrayList<CategoryItem>()
        for (i in drawList.indices){
            newList.add(CategoryItem(list[i], drawList[i]))
        }
        return newList
    }
}