package com.example.myapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemFilmStandardBinding
import com.example.myapplication.model.dataModels.FilmCardStandard

class FilmStandardAdapter(
    val filmInterface: FilmInterface,
    filmListNew: List<FilmCardStandard>
) : RecyclerView.Adapter<FilmStandardAdapter.ViewHolder>() {

    private var filmList = filmListNew

    override fun getItemCount(): Int = filmList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_film_standard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filmCard = filmList[position]
        holder.bind(filmCard)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemFilmStandardBinding.bind(view)
        fun bind(filmCard: FilmCardStandard) = with(binding) {
            filmStandardTitle.text = filmCard.title
            itemFilmStandardLay.setOnClickListener {
                filmInterface.goToFilm(filmCard.id)
            }
            Glide
                .with(itemView)
                .load(filmCard.poster)
                .optionalCenterCrop()
                .into(filmStandardPoster)
        }
    }
}