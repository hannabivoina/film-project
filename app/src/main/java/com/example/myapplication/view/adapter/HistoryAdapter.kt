package com.example.myapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemHistoryBinding
import com.example.myapplication.model.dataModels.HistoryFilmCard

class HistoryAdapter(
    val filmInterface: FilmInterface,
    historyFilmsList: List<HistoryFilmCard>
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var historyList = historyFilmsList

    override fun getItemCount(): Int = historyList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyFilm = historyList[position]
        holder.bind(historyFilm)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemHistoryBinding.bind(view)
        fun bind(historyFilm: HistoryFilmCard) = with(binding) {
            historyTitle.text = historyFilm.title
            historyLay.setOnClickListener {
                filmInterface.goToFilm(historyFilm.id)
            }
            historyYear.text = historyFilm.genres

            Glide
                .with(itemView)
                .load(historyFilm.poster)
                .optionalCenterCrop()
                .into(historyImage)
        }
    }

    fun updateList(newHistoryList: List<HistoryFilmCard>){
        historyList.toMutableList().addAll(newHistoryList)
        notifyDataSetChanged()
    }
}