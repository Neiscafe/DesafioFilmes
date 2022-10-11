package com.example.desafiofilmes.adapter

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.R

class ListaFilmesAdapter() :
    RecyclerView.Adapter<ListaFilmesAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener
    lateinit var itemLongoClickListener: (position: Int) -> Unit


    interface onItemClickListener {
        fun onItemClick(posicao: Int)
        fun onItemLongClick(posicao: Int)
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
        holder.vincula(filme, position)

    }

    override fun getItemCount(): Int {
        return listaFilmes.size
    }

    class ViewHolder(
        itemView: View,
        listener: onItemClickListener
    ) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition)
            }
            itemView.setOnLongClickListener{
                listener.onItemLongClick(bindingAdapterPosition)
                true
            }
        }

        fun vincula(filme: Filme, posicao: Int) {
            val imgVposter = itemView.findViewById<ImageView>(R.id.imageview_poster)
            Glide.with(itemView).load(filme.concatPoster()).into(imgVposter)

            val imagemBolinha = itemView.findViewById<ImageView>(R.id.ImageVBolinha)
            imagemBolinha.isVisible = filme.selected
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