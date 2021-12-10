package com.example.myapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.apiResults.filmInfoAll.Actor
import com.example.myapplication.databinding.ItemActorBinding
import java.sql.DriverManager.println
import javax.inject.Inject

interface StarsFilmsInterface {
    fun toStarFilms(id: String)
}

class StarsAdapter(
    private val starsList: List<Actor>,
    private val starsFilmsInterface: StarsFilmsInterface
) : RecyclerView.Adapter<StarsAdapter.ViewHolder>() {

    override fun getItemCount() = starsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val star = starsList[position]
        holder.bind(star)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_actor, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemActorBinding.bind(view)
        fun bind(star: Actor) {
            binding.itemActorPhoto
            binding.itemActorLay. setOnClickListener {
                    starsFilmsInterface.toStarFilms(star.id)
                }

            Glide.with(itemView).
            load(star.image).
            circleCrop().
            into(binding.itemActorPhoto)
        }
    }
}