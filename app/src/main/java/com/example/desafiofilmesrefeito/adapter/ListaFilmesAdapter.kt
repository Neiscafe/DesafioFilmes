package com.example.desafiofilmes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.R

class ListaFilmesAdapter (listaFilmes: List<Filme>,
                          private val context: Context):
    RecyclerView.Adapter<ListaFilmesAdapter.ViewHolder>() {

    val listaFilmes = listaFilmes.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(context)
            .inflate(R.layout.item_lista, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filme = listaFilmes[position]
        holder.vincula(filme)
    }

    override fun getItemCount(): Int {
        return listaFilmes.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun vincula(filme: Filme) {
            val txtVnome = itemView.findViewById<TextView>(R.id.textview_nome)
            txtVnome.text = filme.nome
        }

    }
}