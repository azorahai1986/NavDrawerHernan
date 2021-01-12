package com.example.navdrawer.actividades

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    private lateinit var auth:FirebaseAuth // para saber si hay existe un email
    lateinit var inicioFragment: HomeFragment
    lateinit var workFragment: WorkFragment
    lateinit var settingFragment: SettingFragment
    lateinit var subCateFragment: SubCateFragment
    lateinit var timelineFragment: TimelineFragment
    lateinit var accederFragment: AccederFragment
    lateinit var categoriasFragment: CategoriasFragment
    var uidRecuperado:String? = null
    var mailRecuperado:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        Log.e("UIDRECUPER", uidRecuperado.toString())

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = "Drawer Hernan"

        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, (
                R.string.open), (R.string.close)){

        }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        inicioFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment.newInstance(mailRecuperado.toString()))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

        // recuperar los datos del login o acceso de usuario...............
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        obtenerEmail(email ?: "", provider ?: "")

        Log.e("emailEnActivityMain", email.toString())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.inicio -> {
                inicioFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout, inicioFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.categorias -> {
                categoriasFragment = CategoriasFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout, categoriasFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(CategoriasFragment.volver).commit()
                //en este caso para volver desde un fragmenr al inicio debo colocar aquí el addtoBackStack
            }

            R.id.work -> {
                workFragment = WorkFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout, workFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.categorias -> {
                timelineFragment = TimelineFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout, timelineFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }

            R.id.acceder -> {
                accederFragment = AccederFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout, accederFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(
                        AccederFragment.VOLVER).commit()
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }

    }
    fun obtenerEmail(email:String, provider:String){
        title = "Inicio"

    }

}