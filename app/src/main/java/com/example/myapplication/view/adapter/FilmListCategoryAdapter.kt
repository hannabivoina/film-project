package com.example.myapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.dataModels.FilmListCategory
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemFilmCategoryBinding
import com.example.myapplication.model.dataModels.HistoryFilm
import javax.inject.Inject


class MainDiffCallback(
    private val oldList: List<FilmListCategory>,
    private val newList: List<FilmListCategory>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldSearch = oldList[oldItemPosition]
        val newSearch = newList[newItemPosition]

        return oldSearch.id == newSearch.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldSearch = oldList[oldItemPosition]
        val newSearch = newList[newItemPosition]

        return oldSearch == newSearch
    }

}

class FilmListCategoryAdapter(val filmInterface: FilmInterface) :
    RecyclerView.Adapter<FilmListCategoryAdapter.ViewHolder>() {

    private val categoryList = ArrayList<FilmListCategory>()

    override fun getItemCount() = categoryList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutInflater.from(parent.context).inflate(R.layout.item_film_category, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemFilmCategoryBinding.bind(view)
        fun bind(category: FilmListCategory) = with(binding) {
            filmCategoryTitle.text = category.title
            val filmListAdapter = FilmListAdapter(filmInterface, category.filmList)
            filmCategoryRecyclerView.adapter = filmListAdapter
            filmCategoryRecyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    fun updateList(newCategory: List<FilmListCategory>) {
        val diffCallback = MainDiffCallback(categoryList, newCategory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        categoryList.clear()
        categoryList.addAll(newCategory)

        diffResult.dispatchUpdatesTo(this)
    }
}