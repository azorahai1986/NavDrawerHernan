package com.example.navdrawer.fragmentos

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.example.navdrawer.R
import com.example.navdrawer.adapters.PagerPrincipalAdapter
import com.example.navdrawer.adapters.RecyclerUnoAdapter
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.CartelPrincipal
import com.example.navdrawer.modelos_de_datos.ModeloDeIndumentaria
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

enum class ProviderType {
    BIENVENIDO
}
class HomeFragment : Fragment() {
    private var adapter:RecyclerUnoAdapter? = null
    private var adapterCartelPrincipal:PagerPrincipalAdapter? = null
    private var layoutManager:RecyclerView.LayoutManager? = null
    private var recyclerView:RecyclerView? = null
    private var viewPagerCartelPrincipal:ViewPager2? = null
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }

    // para darle movimiento automatico al viewPager
    private var indicator:DotsIndicator? = null //indicador para el viewPager
    private var animationCartel:LottieAnimationView? = null
    private var animationRecycler:LottieAnimationView? = null
    private val handler = Handler()
    private  val runnable = Runnable {
        if (adapterCartelPrincipal?.itemCartel?.size != 0){
            viewPagerCartelPrincipal?.currentItem = if (viewPagerCartelPrincipal!!.currentItem == adapterCartelPrincipal!!.itemCartel.size-1) 0
            else viewPagerCartelPrincipal!!.currentItem+1

        }
    }
    lateinit var categoriasFragment:CategoriasFragment
    //para el bundle
    var tokenrecibido:String? = null

    companion object {
        private const val TOKEN_RECIBIDO = "TOKEN_RECIBIDO"
        fun newInstance(token: String):HomeFragment {
            val bundle = Bundle()
            bundle.putString(TOKEN_RECIBIDO, token)
            val fragment = HomeFragment()
            fragment.arguments = bundle

            return fragment

        }


    }

    //........ token y datos desde fragmentAcceder
    /*val bundle = intent.extras
    val email = bundle?.getString("email")
    val provider = bundle?.getString("provider")
    val token = bundle?.getString("idToken")
    setup(email?: "", provider?:"", token?:"")*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        newInstance(tokenrecibido?:"")
        Log.e("tok", tokenrecibido)
        tokenrecibido = arguments?.getString(TOKEN_RECIBIDO)


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
        indicator = view.findViewById(R.id.indicator)
        animationCartel = view.findViewById(R.id.animacion2)
        animationRecycler = view.findViewById(R.id.animacionRecycler)

        adapterCartelPrincipal = PagerPrincipalAdapter(arrayListOf(), context as FragmentActivity)
        viewPagerCartelPrincipal!!.adapter = adapterCartelPrincipal
        indicator?.setViewPager2(viewPagerCartelPrincipal!!)// poner los puntitos delante del viewPager
        //darle el tempo para que corra el viewPager automaticamente

        viewPagerCartelPrincipal?.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 5000)
            }
        })

        // dar funcion a los textViews del final de lista
        val irCategorias = view.findViewById<TextView>(R.id.textview_ir_categorias)
        val quienesSomos = view.findViewById<TextView>(R.id.textview_quienes_somos)
        irCategorias.setOnClickListener {
            irACategorias()
        }
        quienesSomos.setOnClickListener { dialQuienSomos() }

        //........ token y datos desde accederFragment...............
        /*if (token.isNullOrEmpty()){
            //Toast.makeText(this, "no hay nada", Toast.LENGTH_SHORT).show()
        }else{
           // Toast.makeText(this, "Contiene datos", Toast.LENGTH_SHORT).show()
            Log.e("TokenMainAct", token.withIndex().toString())
            //ibCerrarSesion.visibility = View.VISIBLE

        }*/

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
            // para inflar y desaparecer las animaciones de carga

            if (adapterCartelPrincipal !=null){
                animationCartel?.visibility = View.GONE
                animationRecycler?.visibility = View.GONE
            }
        })
    }

    fun irACategorias(){
        categoriasFragment = CategoriasFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.frame_layout, categoriasFragment)?.addToBackStack(CategoriasFragment.volver)
            ?.commit()
    }

    fun dialQuienSomos(){
        val dialogQuien =LayoutInflater.from(activity).inflate(R.layout.dialog_quienes_somos, null)
        val constructorDialog = AlertDialog.Builder(activity).setView(dialogQuien).setTitle("Quienes somos")
        // mostrar dialog.
        constructorDialog.show()
    }

    fun datosDeAcceder(){

    }

}