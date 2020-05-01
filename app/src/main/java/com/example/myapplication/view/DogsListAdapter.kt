package com.example.myapplication.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.DogBreed
import com.example.myapplication.util.getProgressDrawable
import com.example.myapplication.util.loadImage
import kotlinx.android.synthetic.main.fragment_detail.view.text_view_dog_name
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(private val dogsList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogsListAdapter.DogViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount(): Int = dogsList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.itemView.text_view_dog_name?.text = dogsList[position].dogBreed
        holder.itemView.text_view_life_span?.text = dogsList[position].lifeSpan
        holder.itemView.img_dog_icon

        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(ListFragmentDirections.actionListDetail())
        }

        holder.itemView.img_dog_icon.loadImage(
            dogsList[position].imageUrl.toString(),
            getProgressDrawable(holder.itemView.context)
        )
    }

    fun updateDogsList(newDogsList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    class DogViewHolder(view: View) : RecyclerView.ViewHolder(view)
}