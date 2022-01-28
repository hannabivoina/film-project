package com.example.myapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.model.dataModels.FilmCard
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemFilmBinding

interface FilmInterface {
    fun goToFilm(id: String)
}

class FilmListAdapter(
    val filmInterface: FilmInterface,
    filmListNew: List<FilmCard>
) :
    RecyclerView.Adapter<FilmListAdapter.ViewHolder>() {

    private var filmList = filmListNew

    override fun getItemCount(): Int = filmList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filmCard = filmList[position]
        holder.bind(filmCard)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemFilmBinding.bind(view)
        fun bind(filmCard: FilmCard) = with(binding) {
            filmTitle.text = filmCard.title
            itemFilmLay.setOnClickListener {
                filmInterface.goToFilm(filmCard.id)
            }
            filmRating.text = filmCard.imdbRating
            filmYear.text = filmCard.year
            Glide
                .with(itemView)
                .load(filmCard.poster)
                .optionalCenterCrop()
                .into(filmPoster)
        }
    }

}