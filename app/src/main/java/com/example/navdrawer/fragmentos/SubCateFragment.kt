package com.example.navdrawer.fragmentos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navdrawer.R
import com.example.navdrawer.actividades.ActividadAgregarSubCat
import com.example.navdrawer.adapters.RecyclerSubcate
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.SubCategorias
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SubCateFragment : Fragment() {

    var categoriaRecibida: String? = null
    var btMostrarbt:FloatingActionButton? = null
    var btAgregarSub:FloatingActionButton? = null


    companion object {
        private const val CATE_RECIBIDA = "cate_recibida"
        const val VOLVER = "volver"
        fun newInstance(categoriaRecibidas: String): SubCateFragment{
            val bundle = Bundle()
            bundle.putString(CATE_RECIBIDA, categoriaRecibidas)

            val fragment = SubCateFragment()
            fragment.arguments = bundle
            return fragment

        }

    }
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    var recyclerSub: RecyclerView? = null
    var adapterSub:RecyclerSubcate? = null
    var layoutManager:RecyclerView.LayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sub_cate, container, false)
        categoriaRecibida = arguments?.getString(CATE_RECIBIDA)

        btMostrarbt = view.findViewById(R.id.flot_bt_mostrar_sub)
        btAgregarSub = view.findViewById(R.id.fl_Bt_AgregarSub)
        recyclerSub = view.findViewById(R.id.recy_subcate)

        layoutManager = GridLayoutManager(activity, 2)
        recyclerSub?.layoutManager = layoutManager
        recyclerSub?.setHasFixedSize(true)
        adapterSub = RecyclerSubcate(arrayListOf(), activity as FragmentActivity)
        recyclerSub?.adapter = adapterSub
        observeDataSub()

        btMostrarbt?.setOnClickListener {
            btAgregarSub?.visibility = View.VISIBLE
        }
        btAgregarSub?.setOnClickListener { irAAgregarSub() }
        return view
    }

    private fun observeDataSub(){
        viewModel.fetchUserDataSubcate(categoriaRecibida).observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            adapterSub!!.mutableListSub = it as ArrayList<SubCategorias>
            adapterSub!!.notifyDataSetChanged()
            Log.e("datos IT", it.toString())
        })

    }

    fun irAAgregarSub(){
        val intent = Intent(context, ActividadAgregarSubCat::class.java)
        intent.putExtra("id", categoriaRecibida)
        startActivity(intent)
    }

}