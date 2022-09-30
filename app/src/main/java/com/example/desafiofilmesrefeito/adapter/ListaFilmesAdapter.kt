package com.example.desafiofilmes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.R

class ListaFilmesAdapter() :
    RecyclerView.Adapter<ListaFilmesAdapter.ViewHolder>() {


    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(posicao: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    val listaFilmes = arrayListOf<Filme>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_lista, parent, false)
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filme = listaFilmes[position]
        holder.vincula(filme)
    }

    override fun getItemCount(): Int {
        return listaFilmes.size
    }

    class ViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        fun vincula(filme: Filme) {
            val imgVposter = itemView.findViewById<ImageView>(R.id.imageview_poster)
            Glide.with(itemView).load(filme.concatPoster()).into(imgVposter)
        }

    }

    fun populaAdapter(novaLista: List<Filme>) {
        val antigaPosicaoDosItens = listaFilmes.size
        val novaPosicaoDosItens = novaLista.size
        listaFilmes.clear()
        listaFilmes.addAll(novaLista)
        notifyItemRangeInserted(antigaPosicaoDosItens, novaPosicaoDosItens)
    }

}