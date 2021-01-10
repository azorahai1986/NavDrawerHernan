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
import com.example.navdrawer.adapters.RecyclerSubcate
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.SubCategorias


class SubCateFragment : Fragment() {

    var categoriaRecibida: String? = null



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

        recyclerSub = view.findViewById(R.id.recy_subcate)
        layoutManager = GridLayoutManager(activity, 1)
        recyclerSub?.layoutManager = layoutManager
        recyclerSub?.setHasFixedSize(true)
        adapterSub = RecyclerSubcate(arrayListOf(), activity as FragmentActivity)
        recyclerSub?.adapter = adapterSub
        observeDataSub()
        Log.e("fragmentoCate", categoriaRecibida!!)
        return view
    }

    private fun observeDataSub(){
        viewModel.fetchUserDataSubcate(categoriaRecibida).observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            adapterSub!!.mutableListSub = it as ArrayList<SubCategorias>
            adapterSub!!.notifyDataSetChanged()
            Log.e("datos IT", it.toString())
        })

    }

}