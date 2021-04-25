package com.example.navdrawer.actividades

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.example.navdrawer.R
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.fragmentos.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    private lateinit var auth:FirebaseAuth // para saber si hay existe un email
    lateinit var inicioFragment: HomeFragment
    lateinit var sumarPorcentajeFragment: PorcentajeFragment
    lateinit var timelineFragment: PdfFragment
    lateinit var accederFragment: AccederFragment
    lateinit var categoriasFragment: CategoriasFragment
    var uidRecuperado:String? = null
    var mailRecuperado:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pushGeneral()
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = ""




        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, drawerLayout, toolbar, (
                    com.example.navdrawer.R.string.open), (com.example.navdrawer.R.string.close)
        ){

        }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        mailRecuperado = "visitante"
        // recuperar los datos del login o acceso de usuario...............
        //para saber si existe un email................................
        auth = Firebase.auth
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val email = user.email

            //Log.e("EmailMainAct2", email.toString())

            val uid = user.uid
            //Log.e("UID", uid)
            uidRecuperado = user.uid
            mailRecuperado = email.toString()
        }
        val navigationView: NavigationView = findViewById<NavigationView>(R.id.nav_view)
        val nav_Menu: Menu = navigationView.menu
        if(mailRecuperado =="visitante"){
            nav_Menu.findItem(R.id.cerrar).isVisible = false
            nav_Menu.findItem(R.id.porcentajes).isVisible = false

        }

        val idP = intent.getStringExtra("idProd")

        inicioFragment = HomeFragment.newInstance(mailRecuperado!!, idP)
        supportFragmentManager.beginTransaction().replace(
            com.example.navdrawer.R.id.frame_layout,
            inicioFragment
        )
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()



    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.inicio -> {
                inicioFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(
                    com.example.navdrawer.R.id.frame_layout,
                    inicioFragment
                )
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.categorias -> {
                categoriasFragment = CategoriasFragment()
                supportFragmentManager.beginTransaction().replace(
                    com.example.navdrawer.R.id.frame_layout,
                    categoriasFragment
                )
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(
                        CategoriasFragment.volver
                    ).commit()
                //en este caso para volver desde un fragmenr al inicio debo colocar aquí el addtoBackStack
            }

            R.id.busqueda -> {
                val intent = Intent(this, ActividadBusqueda::class.java)
                startActivity(intent)

            }

            R.id.acceder -> {
                if (mailRecuperado != "visitante") {
                    Toast.makeText(this, "Ya estás registrado", Toast.LENGTH_LONG).show()
                } else {
                    accederFragment = AccederFragment()
                    supportFragmentManager.beginTransaction().replace(
                        com.example.navdrawer.R.id.frame_layout,
                        accederFragment
                    )
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(
                            AccederFragment.VOLVER
                        ).commit()
                }

            }
            R.id.cerrar -> {
                if (mailRecuperado == "visitante") {
                    Toast.makeText(this, "No estás registrado", Toast.LENGTH_LONG).show()

                } else {
                    cerrarSesion()
                }

            }
            R.id.porcentajes -> {
                sumarPorcentajeFragment = PorcentajeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout, sumarPorcentajeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(PorcentajeFragment.VOLVER).commit()
            }


        }


        drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }

    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)

        finish()

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()

        }

    }

    fun pushGeneral(){
        FirebaseMessaging.getInstance().subscribeToTopic("general")
    }


}