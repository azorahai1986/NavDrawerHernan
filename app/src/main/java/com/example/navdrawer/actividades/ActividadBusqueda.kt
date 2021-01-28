package com.example.navdrawer.actividades

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navdrawer.R
import com.example.navdrawer.adapters.AdapterBusqueda
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.ModeloDeIndumentaria


class ActividadBusqueda : AppCompatActivity() {
    var etSearch:EditText? = null
    var adapter:AdapterBusqueda? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var recyclerView: RecyclerView? = null

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_busqueda)

        // inflar recycler.......................
        recyclerView = findViewById(R.id.recycler_busqueda)
        layoutManager = GridLayoutManager(this, 1)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        adapter = AdapterBusqueda(arrayListOf(), this)
        recyclerView?.adapter = adapter


        etSearch = findViewById(R.id.edt_actSearch)
        etSearch?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.e("dentro del editText", s.toString())

                adapter?.filtrado(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        observeData()
    }

    fun observeData(){
        viewModel.fetchUserData().observe(this, androidx.lifecycle.Observer {
            adapter!!.setData(it as ArrayList<ModeloDeIndumentaria>)
            adapter!!.notifyDataSetChanged()

        })

    }
}