package com.example.navdrawer.actividades

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.navdrawer.R
import com.example.navdrawer.adapters.AdapterBusqueda
import com.example.navdrawer.fragmentos.SearchFragment


class ActividadBusqueda : AppCompatActivity() {

    var adapter: AdapterBusqueda? = null

    lateinit var busquedaFragment: SearchFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_busqueda)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


         busquedaFragment = SearchFragment()
        supportFragmentManager.beginTransaction().replace(R.id.cordinat, busquedaFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()


    }


}