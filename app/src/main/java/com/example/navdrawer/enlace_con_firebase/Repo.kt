package com.example.navdrawer.enlace_con_firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.navdrawer.modelos_de_datos.*
import com.google.firebase.firestore.FirebaseFirestore

class Repo {
    fun getUserData(): LiveData<MutableList<ModeloDeIndumentaria>> {

        val mutableData = MutableLiveData<MutableList<ModeloDeIndumentaria>>()
        FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
            .get().addOnSuccessListener {

                val listData = mutableListOf<ModeloDeIndumentaria>()

                for (obtenerFireBase in it.documents){
                    val indument = obtenerFireBase.toObject(ModeloDeIndumentaria::class.java)
                    indument?.id = obtenerFireBase.id
                    if (indument != null)

                        listData.add(indument)

                }
                mutableData.value = listData

            }.addOnFailureListener {
                Log.e("ErrorMODELO", it.toString())
                //Esto lo hice para probar si llega internet a la app.
            }
        return mutableData
    }

    fun getOfertas():LiveData<MutableList<CartelPrincipal>>{
        val mutableDatabaseOfertas = MutableLiveData<MutableList<CartelPrincipal>>()
        FirebaseFirestore.getInstance().collection("NuevoModelo").get()
            .addOnSuccessListener {
                val listDataOfertas = mutableListOf<CartelPrincipal>()

                for (ofertas in it.documents){
                    val o = ofertas.toObject(CartelPrincipal::class.java)
                    o?.id = ofertas.id
                    if (o != null){
                        listDataOfertas.add(o)
                    }
                }
                mutableDatabaseOfertas.value = listDataOfertas
            }.addOnFailureListener { Log.e("ErrorMODELO", it.toString()) }
        return mutableDatabaseOfertas
    }

    fun getUserDataSimilares(recibirDescripcion: String?): LiveData<MutableList<PagerSimilares>> {
        //Log.e("DescripcionRepo", recibirDescripcion)
        val mutableData = MutableLiveData<MutableList<PagerSimilares>>()
        FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria").whereEqualTo("sub", recibirDescripcion)
            .get().addOnSuccessListener {

                val listData = mutableListOf<PagerSimilares>()
                //Log.e("Datos del repo1", it.toString())
                //Log.e("Datos del repo2", listData.toString())

                for (obtenerFireBase in it.documents){
                    val indument = obtenerFireBase.toObject(PagerSimilares::class.java)
                    indument?.id = obtenerFireBase.id
                    if (indument != null)
                        listData.add(indument)

                }
                //Log.e("Datos del repo3", listData.toString())

                mutableData.value = listData

            }.addOnFailureListener {
                Log.e("ErrorMODELO", it.toString())
                //Esto lo hice para probar si llega internet a la app.
            }
        return mutableData
    }

    fun getUserDataCategorias(): LiveData<MutableList<Categorias>>{
        val mutableCategorias = MutableLiveData<MutableList<Categorias>>()
        FirebaseFirestore.getInstance().collection("Categoria").get().addOnSuccessListener {
            // variable para el for
            val listDataCate = mutableListOf<Categorias>()

            for (datosCate in it.documents){
                val cat = datosCate.toObject(Categorias::class.java)
                cat?.id = datosCate.id
                if (cat != null)
                    listDataCate.add(cat)
            }
            mutableCategorias.value = listDataCate
            //Log.e("Datos del repo2", listDataCate.toString())
        }.addOnFailureListener {
            Log.e("errorCategorias", it.toString())
        }
        return mutableCategorias

    }

    fun getUserDataSubcate(categoriaRecibida: String?): LiveData<MutableList<SubCategorias>> {

        val mutableData = MutableLiveData<MutableList<SubCategorias>>()
        FirebaseFirestore.getInstance().collection("Subcatergoria").whereEqualTo("cate", categoriaRecibida)
            .get().addOnSuccessListener {
                Log.e("En el repo", categoriaRecibida)
                val listData = mutableListOf<SubCategorias>()
                for (obtenerFireBase in it.documents){
                    val indument = obtenerFireBase.toObject(SubCategorias::class.java)
                    indument?.id = obtenerFireBase.id
                    if (indument != null)

                        listData.add(indument)
                    Log.e("listData", it.documents.toString())

                }
                mutableData.value = listData
                Log.e("Datos del repo3", listData.toString())
            }.addOnFailureListener {
                Log.e("ErrorMODELO", it.toString())
                //Esto lo hice para probar si llega internet a la app.
            }
        return mutableData
    }

    fun getUserDataDependiente(marcaRecibida:String?): LiveData<MutableList<Dependiente>> {

        val mutableData = MutableLiveData<MutableList<Dependiente>>()
        FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria").whereEqualTo("sub", marcaRecibida)
            .get().addOnSuccessListener {

                val listData = mutableListOf<Dependiente>()

                for (obtenerFireBase in it.documents){
                    val indument = obtenerFireBase.toObject(Dependiente::class.java)
                    indument?.id = obtenerFireBase.id
                    if (indument != null)

                        listData.add(indument)

                }
                mutableData.value = listData

            }.addOnFailureListener {
                Log.e("ErrorMODELO", it.toString())
                //Esto lo hice para probar si llega internet a la app.
            }
        return mutableData
    }
}