package com.example.navdrawer.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.navdrawer.R

class VerImagenFragment : Fragment() {

    var recibirImagen:String? = null

    companion object{
        const val IMAGENRECIBIDA = "imagenRecibida"
        private const val IM_RECIBIDA = "IM_RECIBIDA"

        fun newInstance(recibirImagen:String):VerImagenFragment{

            val bundle = Bundle()
            bundle.putString(IM_RECIBIDA, recibirImagen)
            val fragment = VerImagenFragment()
            fragment.arguments = bundle

            return fragment

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_ver_imagen, container, false)

        recibirImagen = arguments?.getString(IM_RECIBIDA)
        val imagenDelAdapter = view.findViewById<ImageView>(R.id.imageView_ver_imagen)

        Glide.with(context!!.applicationContext).load(recibirImagen!!).into(imagenDelAdapter)

        return view
    }


}