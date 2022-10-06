package com.example.desafiofilmesrefeito.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.R

class ListaFavoritosAdapter : RecyclerView.Adapter<ListaFavoritosAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(posicao: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    private val listaFavoritos = mutableListOf<Filme>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_favorito, parent, false)
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filme = listaFavoritos[position]
        holder.vincula(filme)
    }

    override fun getItemCount(): Int {
        return listaFavoritos.size
    }

    class ViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition)
            }
        }

        fun vincula(filme: Filme) {
            val imgVposter = itemView.findViewById<ImageView>(R.id.ImageVPosterFav)
            Glide.with(itemView).load(filme.concatPoster()).into(imgVposter)
            val titulo = itemView.findViewById<TextView>(R.id.TextVTituloFav)
            titulo.text = filme.title.uppercase()
            val data = itemView.findViewById<TextView>(R.id.TextVDataFav)
            data.text = filme.release_date
        }

    }

    fun populaAdapter(novaLista: List<Filme>) {
        listaFavoritos.clear()
        listaFavoritos.addAll(novaLista)
        notifyDataSetChanged()
    }
}