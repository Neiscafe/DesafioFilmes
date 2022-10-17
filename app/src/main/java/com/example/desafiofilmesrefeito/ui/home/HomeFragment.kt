package com.example.desafiofilmesrefeito.ui.home

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiofilmes.adapter.ListaFilmesAdapter
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.R
import com.example.desafiofilmesrefeito.activity.DescricaoFilme
import com.example.desafiofilmesrefeito.activity.FilmesFavoritosActivity
import com.example.desafiofilmesrefeito.database.FilmeDatabase
import com.example.desafiofilmesrefeito.databinding.ActivityListaFilmesBinding
import com.example.desafiofilmesrefeito.databinding.FragmentHomeBinding
import com.example.desafiofilmesrefeito.repository.FilmeFavoritoRepository
import com.example.desafiofilmesrefeito.viewModel.FilmesViewModel
import com.example.desafiofilmesrefeito.viewModel.factory.FilmesViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var listaSelecionados = mutableListOf<Filme>()
    private var listaFilmes = mutableListOf<Filme>()
    private var numeroItensSelecionados: Int = 0
    private lateinit var recyclerViewListener: RecyclerView.OnScrollListener
    private lateinit var voltar: ImageView
    private lateinit var iconeFavoritar: ImageView
    private lateinit var favoritos: TextView

    private val viewModel by lazy {
        val repository =
            FilmeFavoritoRepository(
                FilmeDatabase.getInstance(requireActivity()).getFilmeFavoritoDao()
            )
        val factory = FilmesViewModelFactory(repository)
        ViewModelProviders.of(this, factory).get(FilmesViewModel::class.java)
    }

    val mainAdapter by lazy {
        ListaFilmesAdapter()
    }

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: RecyclerView = binding.recyclerView
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pegaDadosApiAssincrono()
        configuraAppBar()
    }

    private fun pegaDadosApiAssincrono() {
        lifecycleScope.launchWhenStarted {
            configuraAdapter()
            populaLista()
        }
    }

    private fun configuraAdapter() {

        val recyclerView = binding.recyclerView
        val layoutManager = GridLayoutManager(activity, 4)

        recyclerView.adapter = mainAdapter
        recyclerView.layoutManager = layoutManager
    }

    private fun populaLista() {
        lifecycleScope.launch {
            val response = viewModel.populaLista()
            listaFilmes.addAll(response)
            mainAdapter.populaAdapter(listaFilmes)

            configuraAdapterListener()
            configuraClickListener()
        }
    }

    private fun configuraAdapterListener() {
        if (::recyclerViewListener.isInitialized) {
            binding.recyclerView.removeOnScrollListener(recyclerViewListener)
        }
        recyclerViewListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemVisible = visibleItemCount + pastVisibleItems
                    val totalItemCount = layoutManager.itemCount

                    if (totalItemVisible >= totalItemCount) {
                        binding.recyclerView.removeOnScrollListener(recyclerViewListener)
                        lifecycleScope.launch {
                            populaLista()
                        }
                    }
                }
            }
        }
        binding.recyclerView.addOnScrollListener(recyclerViewListener)
    }

    private fun configuraClickListener() {
        mainAdapter.setOnItemClickListener(object : ListaFilmesAdapter.onItemClickListener {
            override fun onItemClick(posicao: Int) {
                if (!listaFilmes[posicao].selected) {
                    val filmeSendoEnviado = listaFilmes[posicao]
                    val intent = Intent(requireActivity(), DescricaoFilme::class.java)
                    intent.putExtra("filmeEnviado", filmeSendoEnviado)
                    startActivity(intent)

                } else {
                    listaFilmes[posicao].selected = false
                    numeroItensSelecionados--
                    listaSelecionados.remove(listaFilmes[posicao])
                    atualizarListaSelecionados()
                }
            }

            override fun onItemLongClick(posicao: Int) {
                if (!listaFilmes[posicao].selected) {
                    listaFilmes[posicao].selected = true
                    numeroItensSelecionados++
                    listaSelecionados.add(listaFilmes[posicao])
                    atualizarListaSelecionados()
                } else {
                    listaFilmes[posicao].selected = false
                    numeroItensSelecionados--
                    listaSelecionados.remove(listaFilmes[posicao])
                    atualizarListaSelecionados()
                }
            }

            private fun atualizarListaSelecionados() {
                mainAdapter.notifyDataSetChanged()
                logicaFilmesSelecionados()
            }
        })
    }

    private fun configuraAppBar() {
        voltar = binding.appbar.ImageVFlecha
        iconeFavoritar = binding.appbar.ImageVIconeFavoritar
        favoritos = binding.appbar.TextVFavoritos

        voltar.isVisible = false

        favoritos.setOnClickListener {
//            val intent = Intent(this, FilmesFavoritosActivity::class.java)
//            startActivity(intent)
//            finish()
        }
    }

    private fun logicaFilmesSelecionados() {

        if (numeroItensSelecionados > 0) {
            iconeFavoritar.isVisible = true
            favoritos.isVisible = false

            iconeFavoritar.setOnClickListener {
                for (filme in listaSelecionados) {
                    filme.selected = false
                    viewModel.salva(filme)
                }
                it.isVisible = false
                favoritos.isVisible = true
                mainAdapter.notifyDataSetChanged()
            }
        } else {
            iconeFavoritar.isVisible = false
            favoritos.isVisible = true
        }

    }

    override fun onResume() {
        super.onResume()
        configuraAdapterListener()
    }

    override fun onPause() {
        super.onPause()
        binding.recyclerView.removeOnScrollListener(recyclerViewListener)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}