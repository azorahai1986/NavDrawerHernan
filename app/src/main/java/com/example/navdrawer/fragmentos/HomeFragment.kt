package com.example.navdrawer.fragmentos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.navdrawer.R
import com.example.navdrawer.adapters.PagerPrincipalAdapter
import com.example.navdrawer.adapters.RecyclerUnoAdapter
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.CartelPrincipal
import com.example.navdrawer.modelos_de_datos.ModeloDeIndumentaria



class HomeFragment : Fragment() {
    var listener:FragmentoEnActivity? = null  // del fragmento
    private var adapter:RecyclerUnoAdapter? = null
    private var adapterCartelPrincipal:PagerPrincipalAdapter? = null
    private var layoutManager:RecyclerView.LayoutManager? = null
    private var recyclerView:RecyclerView? = null
    private var viewPagerCartelPrincipal:ViewPager2? = null
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recycler_productos)
        layoutManager = GridLayoutManager(activity, 2)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        adapter = RecyclerUnoAdapter(arrayListOf(), context as FragmentActivity)
        recyclerView?.adapter = adapter

        // inflar ViewPager Principal
        viewPagerCartelPrincipal = view.findViewById(R.id.viewpager_cartel)

        adapterCartelPrincipal = PagerPrincipalAdapter(arrayListOf(), context as FragmentActivity)
        viewPagerCartelPrincipal?.adapter = adapterCartelPrincipal


        observeData()
        cargarPagerCartelPrincipal()
        return view
    }

    private fun observeData(){
        viewModel.fetchUserData().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter!!.mutableListModel = it as ArrayList<ModeloDeIndumentaria>
            adapter!!.notifyDataSetChanged()
        })

    }

    fun cargarPagerCartelPrincipal(){
        viewModel.fetchUserDataOfertas().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
        adapterCartelPrincipal!!.itemCartel = it as ArrayList<CartelPrincipal>
        adapterCartelPrincipal!!.notifyDataSetChanged()
        })
    }
    interface FragmentoEnActivity{



    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // el linstener será el que envie o me deje acceder a la actividad. su contexto es la interface
        // lo hare mediante un try catch. en el caso que haya un error el classCastExeption me mostrará el error
        try {
            listener = context as FragmentoEnActivity

        }catch (e: ClassCastException){
            throw ClassCastException(context.toString() + "debes implementar interfaz") // en el main debo llamar a la interface
        }
    }

}