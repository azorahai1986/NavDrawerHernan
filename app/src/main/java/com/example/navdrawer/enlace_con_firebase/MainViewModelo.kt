package com.example.navdrawer.enlace_con_firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navdrawer.modelos_de_datos.ModeloDeIndumentaria

class MainViewModelo: ViewModel() {
    val repo = Repo()

    fun fetchUserData(): LiveData<MutableList<ModeloDeIndumentaria>> {
        val mutableData = MutableLiveData<MutableList<ModeloDeIndumentaria>>()
        repo.getUserData().observeForever {
            mutableData.value = it

        }
        return mutableData

    }
}