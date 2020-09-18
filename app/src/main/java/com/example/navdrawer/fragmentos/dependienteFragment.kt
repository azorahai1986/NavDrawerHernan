package com.example.navdrawer.fragmentos

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
import com.example.navdrawer.adapters.RecyDependienteAdapter
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.Dependiente

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val RECIBIRMARCA = "recibir_marca"
private const val ARG_PARAM2 = "param2"


class dependienteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var recibirMarca: String? = null

    companion object {

        const val VOLVER = "volver"
        fun newInstance(marcaRecibida: String) =
            dependienteFragment().apply {
                arguments = Bundle().apply {
                    putString(RECIBIRMARCA, marcaRecibida)
                }
            }
    }
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    var recyDependiente:RecyclerView? = null
    var adapterDependiente:RecyDependienteAdapter? = null
    var layoutManager:RecyclerView.LayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            recibirMarca = it.getString(RECIBIRMARCA)
        }
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dependiente, container, false)

        recyDependiente = view.findViewById(R.id.recycler_dependiente)
        layoutManager = GridLayoutManager(activity, 2)
        recyDependiente?.layoutManager = layoutManager
        recyDependiente?.setHasFixedSize(true)
        adapterDependiente = RecyDependienteAdapter(arrayListOf(), context as FragmentActivity)
        recyDependiente?.adapter = adapterDependiente

        Log.e("En el fragmento", recibirMarca!!)
        observeDataDependiente()

        return view
    }

    private fun observeDataDependiente(){
        viewModel.fetchUserDataDependiente(recibirMarca).observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            adapterDependiente!!.arrayDependiente = it as ArrayList<Dependiente>
            adapterDependiente!!.notifyDataSetChanged()

        })

    }

}