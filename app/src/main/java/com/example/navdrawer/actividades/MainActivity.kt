package com.example.navdrawer.actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.navdrawer.*
import com.example.navdrawer.fragmentos.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class MainActivity : AppCompatActivity(), HomeFragment.FragmentoEnActivity, NavigationView.OnNavigationItemSelectedListener {

    lateinit var homeFragment: HomeFragment
    lateinit var workFragment: WorkFragment
    lateinit var settingFragment: SettingFragment
    lateinit var logoutFragment: LogoutFragment
    lateinit var timelineFragment: TimelineFragment
    lateinit var schoolFragment: SchoolFragment

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

        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                homeFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.school -> {
                schoolFragment = SchoolFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout, schoolFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }

            R.id.work -> {
                workFragment = WorkFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_layout, workFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.school -> {
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