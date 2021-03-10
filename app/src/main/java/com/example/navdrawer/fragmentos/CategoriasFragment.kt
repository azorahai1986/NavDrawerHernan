package com.example.navdrawer.fragmentos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navdrawer.R
import com.example.navdrawer.actividades.ActividadAgregar
import com.example.navdrawer.adapters.CategoriasAdapter
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.Categorias
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class CategoriasFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var uidRecuperado:String? = null
    private var mailRecuperado:String? = null
    companion object{
        const val volver = "VolverAlInicio"
    }

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    var layoutManager:RecyclerView.LayoutManager? = null
    var categoriasAdapter:CategoriasAdapter? = null
    var recyclerCategorias:RecyclerView? = null

    var btAgregarCate:FloatingActionButton?= null
    private var textViewC:TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categorias, container, false)

        btAgregarCate = view.findViewById(R.id.fl_Bt_AgregarCate)
        textViewC = view.findViewById(R.id.textView_c)
        recyclerCategorias = view.findViewById(R.id.recycler_categorias)

        layoutManager = GridLayoutManager(activity, 2)
        recyclerCategorias?.layoutManager = layoutManager
        recyclerCategorias?.setHasFixedSize(true)
        categoriasAdapter = CategoriasAdapter(arrayListOf(), context as FragmentActivity)
        recyclerCategorias?.adapter = categoriasAdapter

        // dar funcion a los botones.........................................
        auth = Firebase.auth
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            mailRecuperado = user.email

            Log.e("EmailHome", mailRecuperado.toString())

            uidRecuperado = user.uid

        }

        if (!(mailRecuperado.isNullOrEmpty())){
            btAgregarCate?.visibility = View.VISIBLE
            textViewC?.visibility = View.VISIBLE
        }
        btAgregarCate?.setOnClickListener { irAgregarActivity() }


        observerDataCategorias()
        return view
    }

    private fun observerDataCategorias(){
        
        viewModel.fetchUserDataCategorias().observe(this.viewLifecycleOwner, Observer {
            categoriasAdapter?.arrayCategorias = it as ArrayList<Categorias>
            categoriasAdapter?.notifyDataSetChanged()

        } )
    }
    fun irAgregarActivity(){

        val intent = Intent(context, ActividadAgregar::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        observerDataCategorias()
    }






}