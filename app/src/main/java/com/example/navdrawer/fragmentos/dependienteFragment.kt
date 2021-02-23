package com.example.navdrawer.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navdrawer.R
import com.example.navdrawer.actividades.ActividadAgregarProducto
import com.example.navdrawer.adapters.RecyDependienteAdapter
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.Dependiente
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val RECIBIRMARCA = "recibir_marca"
private const val RECIIBIR_ID = "recibir_id"


class dependienteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var recibirMarca: String? = null
    private var IdRecibido: String? = null

    companion object {

        const val VOLVER = "volver"
        fun newInstance(marcaRecibida: String, id:String) =
            dependienteFragment().apply {
                arguments = Bundle().apply {
                    putString(RECIBIRMARCA, marcaRecibida)
                    putString(RECIIBIR_ID, id)
                }
            }
    }
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    var recyDependiente:RecyclerView? = null
    var adapterDependiente:RecyDependienteAdapter? = null
    var layoutManager:RecyclerView.LayoutManager? = null
    var btMostrarProdu:FloatingActionButton? = null
    var btAgregarProdu:FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        recibirMarca = arguments?.getString(RECIBIRMARCA)
        IdRecibido = arguments?.getString(RECIIBIR_ID)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dependiente, container, false)

        recyDependiente = view.findViewById(R.id.recycler_dependiente)
        layoutManager = GridLayoutManager(activity, 2)
        recyDependiente?.layoutManager = layoutManager
        recyDependiente?.setHasFixedSize(true)
        adapterDependiente = RecyDependienteAdapter(arrayListOf(), context as FragmentActivity)
        recyDependiente?.adapter = adapterDependiente

        observeDataDependiente()

        btMostrarProdu = view.findViewById(R.id.flot_bt_mostrar_btprodu)
        btAgregarProdu = view.findViewById(R.id.btCargar_produ)
        btMostrarProdu?.setOnClickListener {
            btAgregarProdu?.visibility = View.VISIBLE
        }

        btMostrarProdu?.setOnClickListener { irAcitividadAgregarProducto() }

        return view
    }

    private fun observeDataDependiente(){
        viewModel.fetchUserDataDependiente(recibirMarca).observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            adapterDependiente!!.arrayDependiente = it as ArrayList<Dependiente>
            adapterDependiente!!.notifyDataSetChanged()

        })


    }
    fun irAcitividadAgregarProducto(){
        val intent = Intent(context, ActividadAgregarProducto::class.java)
        intent.putExtra("id", IdRecibido)
        intent.putExtra("marca", recibirMarca)
        startActivity(intent)
    }

}