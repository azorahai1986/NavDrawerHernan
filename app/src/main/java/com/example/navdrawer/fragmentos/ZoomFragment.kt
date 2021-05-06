package com.example.navdrawer.fragmentos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.example.navdrawer.adapters.PagerZoomAdapter
import com.example.navdrawer.databinding.FragmentZoomBinding
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.ImagenesViewPager


class ZoomFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }

    private var imagenRecibida: String? = null
    private var idRecibido: String? = null
    private lateinit var binding:FragmentZoomBinding
    var adapter:PagerZoomAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentZoomBinding.inflate(inflater, container, false)
        imagenRecibida = arguments?.getString(IMAGEN_RECIBIDA)
        idRecibido = arguments?.getString(ID_RECIBIDO)
        Log.e("IMAGENZOOM", idRecibido.toString())

        binding.viewpagerZoom as ViewPager2
        adapter = PagerZoomAdapter(arrayListOf(), context as FragmentActivity)
        binding.viewpagerZoom.adapter = adapter

        observeDataZoom()
        return binding.root
    }

    companion object {
        private val IMAGEN_RECIBIDA = "IMAGENRECIBIDA"
        private val ID_RECIBIDO = "IDRECIBIDO"

        const val VOLVERZOOM = "VOLVER"

        fun newInstance(imagen: String, idRecibido: String?):ZoomFragment {

        val bundle = Bundle()
            bundle.putString(IMAGEN_RECIBIDA, imagen)
            bundle.putString(ID_RECIBIDO, idRecibido)

            val fragment = ZoomFragment()
            fragment.arguments = bundle


            return fragment




        }
    }

    fun observeDataZoom(){
        viewModel.fetchUserDataZoom(idRecibido).observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter?.arrayZoom = it as ArrayList<ImagenesViewPager>
            adapter?.notifyDataSetChanged()
        })
    }
}