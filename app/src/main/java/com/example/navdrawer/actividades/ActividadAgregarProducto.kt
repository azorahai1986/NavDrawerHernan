package com.example.navdrawer.actividades

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.navdrawer.R
import com.example.navdrawer.adapters.RecyclerUnoAdapter
import com.example.navdrawer.clases_push.NotificationData
import com.example.navdrawer.clases_push.PushNotification
import com.example.navdrawer.clases_push.Retrofitinstance
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.ModeloDeIndumentaria
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_actividad_agregar_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class ActividadAgregarProducto : AppCompatActivity(), View.OnClickListener {

    val TAG = "ActividadAgregar"
    var tvSwitch: TextView? = null
    var btCargarProdu: Button? = null
    var tvMarca: TextView? = null
    var open:Boolean = false

    private val PICK_IMAGE_REQUEST = 1234
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    private var adapter: RecyclerUnoAdapter? = null

    // Método para subir imagenes al firebase storage
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    lateinit var imagenCargada:ImageView


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView_produ!!.setImageBitmap(bitmap)


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    private fun uploadFile() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Cargando...")
            progressDialog.show()

            // para modificar los datos de una lista usando firestore..........................

            val imageRef = storageReference!!.child("images/" + UUID.randomUUID().toString())

            var uploadTask = imageRef.putFile(filePath!!)
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    // para editar los datos de la lista...........................................

                    val bundle = intent.extras
                    val idRecuperado = bundle?.getString("id")
                    var marca = bundle?.getString("marca")
                    //var cate = tvCategoria.text.toString()
                    var producto = tv_produ.text.toString()
                    var imagen = downloadUri.toString()
                    var precio = etPrecioProdu.text.toString()

                    var map = mutableMapOf<String, Any>()
                     map["cate"] = idRecuperado.toString()
                     map["marca"] = marca.toString()
                    map["precio"] = precio
                    map["nombre"] = producto
                    map["imagen"] = imagen

                    when {
                        open -> {FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
                            .add(map).addOnSuccessListener {
                                lanzarPush(it.id)

                            }

                    }
                        !open ->{
                            FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
                                .document().set(map)
                            finish()
                        }

                    }


                    /*FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
                        .document().set(map)
                    finish()*/



                } else {
                    // Handle failures
                    // ...
                }
            }

        }



    }


    private fun showFilerChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)
    }
    lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_agregar_producto)

        storage = Firebase.storage

        // dare init a firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        val bundle = intent.extras
        var marca = bundle?.getString("marca")

        tvMarca = findViewById(R.id.tv_marca)
        tvSwitch = findViewById(R.id.tv_switch)
        tvMarca?.text = marca

        btCargarProdu = findViewById(R.id.btCargar_produ)
        imageView_produ.setOnClickListener(this)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        val dilatar = AnimationUtils.loadAnimation(this, R.anim.dilatar)
        // dar funcion al swith..........................................
        switch_push.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                tvSwitch?.setTextColor(getColor(R.color.WhiteColor))
                dilatar.repeatMode = Animation.REVERSE
                tvSwitch?.startAnimation(dilatar)
                image_tilde.visibility = View.VISIBLE
                open = true


            } else {
                tvSwitch?.setTextColor(getColor(R.color.amarillo))
                image_tilde.visibility = View.INVISIBLE

                open = false
                Log.e("openFalse", open.toString())

            }
        }


        btCargarProdu?.setOnClickListener { uploadFile() }

        exTraerDatos()

    }

    fun exTraerDatos() {
        viewModel.fetchUserData().observe(this, androidx.lifecycle.Observer {
            adapter?.mutableListModel = it as ArrayList<ModeloDeIndumentaria>
            adapter?.notifyDataSetChanged()
            val autocompletar = mutableListOf<String>()
            val autocompletarMarca = mutableListOf<String>()
            val autocompletarCate = mutableListOf<String>()

            for (x in it) {
                autocompletar.add(x.nombre)
                autocompletarMarca.add(x.marca)
                autocompletarCate.add(x.cate)

            }
            val adapterAuto = ArrayAdapter(this, android.R.layout.simple_list_item_1, autocompletar)
            tv_produ.threshold = 0
            tv_produ.setAdapter(adapterAuto)
            tv_produ.setOnFocusChangeListener { view, b ->
                if (b) tv_produ.showDropDown()
            }

        })

    }

    // para enviar los push a los usuarios..............................
    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = Retrofitinstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.e(TAG, "response: ${Gson().toJson(response)} ")
                } else {
                    Log.e(TAG, response.errorBody().toString())

                }

                withContext(Dispatchers.Main){
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    finish()
                }
            }


        }

    fun lanzarPush(id:String) {
        val title = tv_produ.text.toString()
        val message = tvMarca?.text.toString() + " $ " + etPrecioProdu.text.toString()
        if (title.isNotEmpty() && message.isNotEmpty()) {
            PushNotification(
                NotificationData(title, message, id),
                TOPIC
            ).also {
                sendNotification(it)

            }
        }
        
    }

    override fun onClick(v: View?) {
        if (v === imageView_produ)
            showFilerChooser()



    }
}