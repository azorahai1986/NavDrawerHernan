package com.example.navdrawer.fragmentos

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.example.navdrawer.R
import com.example.navdrawer.actividades.CartelActivity
import com.example.navdrawer.adapters.AdapterRecyclerPrincipal
import com.example.navdrawer.adapters.PagerPrincipalAdapter
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.CartelPrincipal
import com.example.navdrawer.modelos_de_datos.ModeloDeIndumentaria
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

enum class ProviderType {
    BIENVENIDO
}
class HomeFragment : Fragment() {

    //para recuperar y verificar que esta registrado y logeado el cliente
    private lateinit var auth:FirebaseAuth
    var uidRecuperado:String? = null
    var mailRecuperado:String? = null
    var tvCartel:TextView? = null


    var isOpen = true // para las animcaiones de los botones

    private var adapterRecyclerPrincipal:AdapterRecyclerPrincipal? = null
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
    lateinit var homeFragment: HomeFragment
    //para el bundle
    var tokenrecibido:String? = null
    var idRecibid:String? = null

    companion object {
        private const val TOKEN_RECIBIDO = "TOKEN_RECIBIDO"
        private const val ID_RECIBIDO = "ID_RECIBIDO"
        fun newInstance(token: String, idProd:String? = null):HomeFragment {
            val bundle = Bundle()
            bundle.putString(TOKEN_RECIBIDO, token)
            bundle.putString(ID_RECIBIDO, idProd)
            val fragment = HomeFragment()
            fragment.arguments = bundle
            Log.e("newInstance", token)

            return fragment

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        tokenrecibido = arguments?.getString(TOKEN_RECIBIDO)
        idRecibid = arguments?.getString(ID_RECIBIDO)
        Log.e("TokenRecibid", tokenrecibido.toString())



        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        recyclerView = view.findViewById(R.id.recycler_productos)
        layoutManager = GridLayoutManager(activity, 1)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        adapterRecyclerPrincipal = AdapterRecyclerPrincipal(arrayListOf(), context as FragmentActivity)
        recyclerView?.adapter = adapterRecyclerPrincipal

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

        auth = Firebase.auth
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            mailRecuperado = user.email

            Log.e("EmailHome", mailRecuperado.toString())

            uidRecuperado = user.uid
            Log.e("UIDHome", uidRecuperado)

        }
            //animar botones...................................................................
        val rotate = AnimationUtils.loadAnimation(context, R.anim.rotar)
        val abrire = AnimationUtils.loadAnimation(context, R.anim.abrir)
        val cerrar = AnimationUtils.loadAnimation(context, R.anim.cerrar)
        // dar funcion a los textViews del final de lista
        val quienesSomos = view.findViewById<TextView>(R.id.textview_quienes_somos)
        val irAlPrincio = view.findViewById<TextView>(R.id.tv_volver_inicio)
        irAlPrincio.setOnClickListener { irAlInicio() }
        quienesSomos.setOnClickListener { dialQuienSomos() }

        // ...... asignar variable a los botones........
        var btAgCartel = view.findViewById<FloatingActionButton>(R.id.flot_btAgregar)
        //........ token y datos desde accederFragment.....................................................

        tvCartel = view.findViewById(R.id.textOfertas)


        when {
            mailRecuperado != null -> {
                btAgCartel.visibility = View.VISIBLE
                tvCartel?.visibility = View.VISIBLE
                isOpen = if (isOpen){
                    btAgCartel.startAnimation(abrire)
                    tvCartel?.startAnimation(abrire)
                    //flot_btAgregar.startAnimation(rotate)
                    false
                }else{
                    btAgCartel.startAnimation(cerrar)
                    tvCartel?.startAnimation(cerrar)
                    true
                }

                Toast.makeText(context, "bienvenido $mailRecuperado", Toast.LENGTH_LONG).show()

            }
            mailRecuperado == null -> {
                Toast.makeText(context, "bienvenido visitante", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(context, "bienvenido visitante", Toast.LENGTH_LONG).show()

            }
        }


        // dar funcines a los botones............................
        btAgCartel.setOnClickListener { irCartelActivity()}

        var swipe = view.findViewById<SwipeRefreshLayout>(R.id.swiperefreshlayout)
        swipe?.setOnRefreshListener {
            //cargar datos de firebase
            observeData()
            swipe.isRefreshing = false
        }
        // dar funcion al editTextSearch para filtrar el recycler:::::::::::::::::::::.....

        observeData()
        cargarPagerCartelPrincipal()

        return view
    }

    fun observeData(){
        viewModel.fetchUserData().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            adapterRecyclerPrincipal!!.setData(it as ArrayList<ModeloDeIndumentaria>)
            adapterRecyclerPrincipal!!.notifyDataSetChanged()

            if (idRecibid != null){
                val i = busqueda()
                if (i>-1){
                    recyclerView!!.layoutManager!!.scrollToPosition(i)
                }
            }

        })

    }

    fun cargarPagerCartelPrincipal(){
        viewModel.fetchUserDataOfertas().observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
        adapterCartelPrincipal!!.itemCartel = it as ArrayList<CartelPrincipal>
        adapterCartelPrincipal!!.notifyDataSetChanged()
            // para inflar y desaparecer las animaciones de carga
            val abrire = AnimationUtils.loadAnimation(context, R.anim.abrir2)

            if (adapterCartelPrincipal !=null){
                animationCartel?.visibility = View.GONE
                animationRecycler?.visibility = View.GONE
                viewPagerCartelPrincipal?.startAnimation(abrire)
            }
        })
    }

    fun irAlInicio(){
        homeFragment = HomeFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.frame_layout, homeFragment)
            ?.commit()
    }

    fun dialQuienSomos(){
        val dialogQuien =LayoutInflater.from(activity).inflate(R.layout.dialog_quienes_somos, null)
        val constructorDialog = AlertDialog.Builder(activity).setView(dialogQuien).setTitle("   Mercado y Comercio")
        // mostrar dialog.
        constructorDialog.show()
    }

    fun irCartelActivity(){
        val intent = Intent(context, CartelActivity::class.java)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.expandir_lateral_derecho, R.anim.contraer_lateral_derecho)

    }

    override fun onResume() {
        super.onResume()
        observeData()
        cargarPagerCartelPrincipal()

    }

    fun busqueda():Int{
        for (i in 0 until adapterRecyclerPrincipal!!.mutableListModel.size){
            if (adapterRecyclerPrincipal!!.mutableListModel[i].id == idRecibid){
                return i
            }
        }
        return -1
    }


}