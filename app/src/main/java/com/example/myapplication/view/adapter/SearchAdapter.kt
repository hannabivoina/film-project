package com.example.myapplication.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemHistoryBinding
import com.example.myapplication.databinding.ItemSearchBinding
import com.example.myapplication.model.dataModels.HistoryFilm
import javax.inject.Inject

private const val statusHistory = "history"

interface SaveFilmInterface{
    fun addFilmToSaved(historyFilm: HistoryFilm)
}

class SearchDiffCallback(
    private val oldList: List<HistoryFilm>,
    private val newList: List<HistoryFilm>
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

class SearchAdapter(private val content: String, private val filmInterface: FilmInterface, private val saveFilmInterface: SaveFilmInterface) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var queriesList = ArrayList<HistoryFilm>()

    override fun getItemCount() = queriesList.size

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val filmQuery = queriesList[position]
        holder.bind(filmQuery)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemSearchBinding.bind(view)
        fun bind(filmQuery: HistoryFilm) {
            binding.apply {
                searchItemTitle.text = filmQuery.title
                searchItemTitle.setTextColor(getSearchTitleColor(view))
                searchItemLay.setOnClickListener {
                    saveFilmInterface.addFilmToSaved(filmQuery)
                    filmInterface.goToFilm(filmQuery.id)
                }
            }
        }


        private fun getSearchTitleColor(view: View) =
            when (content) {
                statusHistory -> ContextCompat.getColor(view.context, R.color.holo_red)
                else -> Color.WHITE
            }
    }

    fun updateList(newQueriesList: List<HistoryFilm>) {
        val diffCallback = SearchDiffCallback(queriesList, newQueriesList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        queriesList.clear()
        queriesList.addAll(newQueriesList)

        diffResult.dispatchUpdatesTo(this)
    }
}