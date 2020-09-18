package com.example.navdrawer.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navdrawer.R
import com.example.navdrawer.adapters.CategoriasAdapter
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.Categorias


class CategoriasFragment : Fragment() {
    companion object{
        const val volver = "VolverAlInicio"
    }

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    var layoutManager:RecyclerView.LayoutManager? = null
    var categoriasAdapter:CategoriasAdapter? = null
    var recyclerCategorias:RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categorias, container, false)

        recyclerCategorias = view.findViewById(R.id.recycler_categorias)
        layoutManager = GridLayoutManager(activity, 2)
        recyclerCategorias?.layoutManager = layoutManager
        recyclerCategorias?.setHasFixedSize(true)
        categoriasAdapter = CategoriasAdapter(arrayListOf(), context as FragmentActivity)
        recyclerCategorias?.adapter = categoriasAdapter


        observerDataCategorias()
        return view
    }

    private fun observerDataCategorias(){
        
        viewModel.fetchUserDataCategorias().observe(this.viewLifecycleOwner, Observer {
            categoriasAdapter?.arrayCategorias = it as ArrayList<Categorias>
            categoriasAdapter?.notifyDataSetChanged()

        } )
    }






}