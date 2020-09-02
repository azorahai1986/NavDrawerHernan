package com.example.navdrawer.fragmentos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.navdrawer.R
import com.example.navdrawer.adapters.PagerSimilaresAdapter
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.PagerSimilares


class VerImagenFragment : Fragment() {

    // variables para los datos recibidos desde el adapter............................
    var recibirImagen:String? = null
    var recibirNombre:String? = null
    var recibirDescripcion:String? = null
    var recibirPrecio:String? = null
    var recibirId:String? = null

    companion object{
        const val IMAGENRECIBIDA = "imagenRecibida" //para cuando vuelvo atras recordar donde estaba ubicado
        private const val IM_RECIBIDA = "IM_RECIBIDA" //para recibir la imagen del adapter
        private const val NOMBRE_RECIBIDO = "NOMBRE_RECIBIDO" //para recibir el nombre del adapter
        private const val DESC_RECIBIDA = "DESC_RECIBIDA" //para recibir la descripcion del adapter
        private const val PRECIO_RECIBIDO = "PRECIO_RECIBIDO" //para recibir el precio del adapter
        private const val ID_RECIBIDO = "ID_RECIBIDO" //para recibir el precio del adapter y plasmarlo en el viewPager

        fun newInstance(recibirImagen:String, recibirNombre:String, recibirDesc:String, precioRecibido:String, recibirId:String):VerImagenFragment{

            val bundle = Bundle()
            bundle.putString(IM_RECIBIDA, recibirImagen)
            bundle.putString(NOMBRE_RECIBIDO, recibirNombre)
            bundle.putString(DESC_RECIBIDA, recibirDesc)
            bundle.putString(PRECIO_RECIBIDO, precioRecibido)
            bundle.putString(ID_RECIBIDO, recibirId)
            val fragment = VerImagenFragment()
            fragment.arguments = bundle


            return fragment

        }

    }
    var adapterSimilar:PagerSimilaresAdapter? = null
    var viewPagerSimilar:ViewPager2? = null
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_ver_imagen, container, false)

        recibirImagen = arguments?.getString(IM_RECIBIDA)
        recibirNombre = arguments?.getString(NOMBRE_RECIBIDO)
        recibirDescripcion = arguments?.getString(DESC_RECIBIDA)
        recibirPrecio = arguments?.getString(PRECIO_RECIBIDO)
        recibirId = arguments?.getString(ID_RECIBIDO)
        val imagenDelAdapter = view.findViewById<ImageView>(R.id.imageView_ver_imagen)
        val nombre = view.findViewById<TextView>(R.id.textViewNombre)
        val desc = view.findViewById<TextView>(R.id.textViewDesc)
        val precio = view.findViewById<TextView>(R.id.textViewPrecio)

        Glide.with(context!!.applicationContext).load(recibirImagen!!).into(imagenDelAdapter)
        nombre.text = recibirNombre
        desc.text = recibirDescripcion
        precio.text = recibirPrecio

        // inflar viewPager......................................

        viewPagerSimilar = view.findViewById(R.id.viewPager_similares) as ViewPager2
        adapterSimilar = PagerSimilaresAdapter(arrayListOf(), context as FragmentActivity)
        viewPagerSimilar?.adapter = adapterSimilar

        Log.e("idRecibido", recibirId!!)
        observerDataSimil()
        return view
    }
    //  funcion para inflar el viewPager
    private fun observerDataSimil(){
        viewModel.fetchUserDataSimilares(recibirDescripcion).observe(this.viewLifecycleOwner, androidx.lifecycle.Observer{
            adapterSimilar!!.similaresArray = it as ArrayList<PagerSimilares>
            adapterSimilar!!.notifyDataSetChanged()

        })

    }

}