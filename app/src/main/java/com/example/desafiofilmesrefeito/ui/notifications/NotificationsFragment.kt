package com.example.desafiofilmesrefeito.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiofilmes.activity.ListaFilmesActivity
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.R
import com.example.desafiofilmesrefeito.activity.DescricaoFilme
import com.example.desafiofilmesrefeito.adapter.ListaFavoritosAdapter
import com.example.desafiofilmesrefeito.database.FilmeDatabase
import com.example.desafiofilmesrefeito.databinding.FragmentNotificationsBinding
import com.example.desafiofilmesrefeito.repository.FilmeFavoritoRepository
import com.example.desafiofilmesrefeito.viewModel.FilmesViewModel
import com.example.desafiofilmesrefeito.viewModel.factory.FilmesViewModelFactory

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var listaSelecionados = mutableListOf<Filme>()
    private lateinit var listaFilmes: List<Filme>
    private lateinit var adapter: ListaFavoritosAdapter
    private lateinit var manager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var estado = 0

    private val viewModel by lazy {
        val repository =
            FilmeFavoritoRepository(FilmeDatabase.getInstance(requireActivity()).getFilmeFavoritoDao())
        val factory = FilmesViewModelFactory(repository)
        ViewModelProviders.of(this, factory).get(FilmesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inicializaItensLista()

        populaListaAssincrona()

        configuraLongListener()

        configuraAppBar()
    }


    private fun inicializaItensLista() {
        recyclerView = binding.recyclerViewFavoritos
        adapter = ListaFavoritosAdapter()
        manager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
    }

    private fun populaListaAssincrona() {
        viewModel.filmesFavoritos.observe(requireActivity()) { listaAtualizada ->
            listaFilmes = listaAtualizada
            adapter.populaAdapter(listaAtualizada)
            if (listaAtualizada.isEmpty()) {
                val listaVazia = binding.TextVListaVazia
                listaVazia.isVisible = true
            }
        }
    }

    private fun configuraLongListener() {
        adapter.setOnItemClickListener(object : ListaFavoritosAdapter.onItemClickListener {
            override fun onItemClick(posicao: Int) {

                if (!listaFilmes[posicao].selected) {
                    val filmeSendoEnviado = listaFilmes[posicao]
//                    val intent = Intent(requireActivity(), DescricaoFilme::class.java)
//                    intent.putExtra("filmeEnviado", filmeSendoEnviado)
//                    startActivity(intent)
                } else {
                    listaFilmes[posicao].selected = false
                    estado--
                    listaSelecionados.remove(listaFilmes[posicao])
                    adapter.notifyDataSetChanged()
                    configuraAppBar()
                }
            }

            override fun onItemLongClick(posicao: Int) {
                if (listaFilmes[posicao].selected == false) {
                    listaFilmes[posicao].selected = true
                    estado++
                    listaSelecionados.add(listaFilmes[posicao])
                    adapter.notifyDataSetChanged()
                    configuraAppBar()
                } else {
                    listaFilmes[posicao].selected = false
                    estado--
                    listaSelecionados.remove(listaFilmes[posicao])
                    adapter.notifyDataSetChanged()
                    configuraAppBar()
                }
            }
        })
    }

    private fun configuraAppBar() {
        criaBotaoVoltarAppBar()
        ocultaBotaoFavoritos()
        var iconeFavoritar = binding.appbar.ImageVIconeFavoritar

        if (estado > 0) {
            iconeFavoritar.isVisible = true

            iconeFavoritar.setOnClickListener {
                for (filme in listaSelecionados) {
                    viewModel.remove(filme.id)
                    iconeFavoritar.isVisible = false
                    filme.selected = false
                    adapter.notifyDataSetChanged()
                    estado = 0
                }
            }
        } else {
            iconeFavoritar.isVisible = false
        }
    }

    private fun criaBotaoVoltarAppBar() {
        val voltar = binding.appbar.ImageVFlecha
        voltar.setOnClickListener {
//            val intent = Intent(this, ListaFilmesActivity::class.java)
//            startActivity(intent)
        }
    }

    private fun ocultaBotaoFavoritos() {
        val favoritos = binding.appbar.TextVFavoritos
        favoritos.isVisible = false
    }
}
