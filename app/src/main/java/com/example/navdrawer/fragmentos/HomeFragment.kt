package com.example.navdrawer.fragmentos

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navdrawer.R
import com.example.navdrawer.adapters.RecyclerUnoAdapter
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.ModeloDeIndumentaria
import java.lang.ClassCastException
import java.util.Observer



class HomeFragment : Fragment() {
    var listener:FragmentoEnActivity? = null  // del fragmento
    private var adapter:RecyclerUnoAdapter? = null
    private var layoutManager:RecyclerView.LayoutManager? = null
    private var recyclerView:RecyclerView? = null
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val productos = ArrayList<ModeloDeIndumentaria>()
        productos.add(ModeloDeIndumentaria("1", "ss", "aa", R.drawable.constantine, "mm", "20"))
        productos.add(ModeloDeIndumentaria("2", "ss", "aa", R.drawable.constantine, "mm", "20"))
        productos.add(ModeloDeIndumentaria("3", "ss", "aa", R.drawable.constantine, "mm", "20"))
        productos.add(ModeloDeIndumentaria("4", "ss", "aa", R.drawable.constantine, "mm", "20"))

        recyclerView = view.findViewById(R.id.recycler_productos)
        layoutManager = LinearLayoutManager(activity)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        adapter = RecyclerUnoAdapter(productos, context as FragmentActivity)
        recyclerView?.adapter = adapter


        return view
    }

    private fun observeData(){
        viewModel.fetchUserData().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter!!.mutableListModel = it as ArrayList<ModeloDeIndumentaria>
            adapter!!.notifyDataSetChanged()
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