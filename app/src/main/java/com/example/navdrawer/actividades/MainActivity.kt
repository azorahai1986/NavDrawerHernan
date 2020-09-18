package com.example.navdrawer.actividades

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.navdrawer.R
import com.example.navdrawer.fragmentos.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var inicioFragment: HomeFragment
    lateinit var workFragment: WorkFragment
    lateinit var settingFragment: SettingFragment
    lateinit var subCateFragment: SubCateFragment
    lateinit var timelineFragment: TimelineFragment
    lateinit var categoriasFragment: CategoriasFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, inicioFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

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
}