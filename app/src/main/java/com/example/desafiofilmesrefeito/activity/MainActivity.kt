package com.example.desafiofilmes.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.desafiofilmes.adapter.ListaFilmesAdapter
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.RetrofitInicializador
import com.example.desafiofilmesrefeito.databinding.ActivityMainBinding
import com.example.desafiofilmesrefeito.model.FilmeResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val listaFilmes: ArrayList<Filme> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            try {
                RetrofitInicializador().filmeService.buscaTodas()
                    .enqueue(object : Callback<FilmeResponse> {
                        override fun onResponse(
                            call: Call<FilmeResponse>,
                            response: Response<FilmeResponse>
                        ) {
                            if (response.isSuccessful) {
                                listaFilmes.addAll(response.body()!!.results)
                            } else {
                            }
                        }
                        override fun onFailure(call: Call<FilmeResponse>, t: Throwable) {
                        }
                    })
            }catch (e: HttpException){
            }
            val adapter = ListaFilmesAdapter(listaFilmes, this@MainActivity)
            val recyclerView = binding.recyclerView
            recyclerView.adapter = adapter
            Log.i("Listade Notas", "onCreate: $listaFilmes")
        }
        setContentView(binding.root)
    }
}