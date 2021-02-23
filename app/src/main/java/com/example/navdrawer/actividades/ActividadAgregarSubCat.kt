package com.example.navdrawer.actividades

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.navdrawer.R
import com.example.navdrawer.adapters.RecyclerSubcate
import com.example.navdrawer.clases_push.PushNotification
import com.example.navdrawer.clases_push.Retrofitinstance
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.SubCategorias
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_actividad_agregar.imageView_sub
import kotlinx.android.synthetic.main.activity_actividad_agregar_sub_cat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class ActividadAgregarSubCat : AppCompatActivity(), View.OnClickListener {

    val TAG = "ActividadAgregar"
    var tvSwitch: TextView? = null
    var btCargarSub: ImageButton? = null
    var idRecibido:String? = null
    private val PICK_IMAGE_REQUEST = 1234
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }
    private var adapter: RecyclerSubcate? = null

    // Método para subir imagenes al firebase storage
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView_sub!!.setImageBitmap(bitmap)
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
                    idRecibido = bundle?.getString("id")
                    //var cate = tvCategoria.text.toString()
                    var sub = tv_subcate.text.toString()
                    var imagen = downloadUri.toString()

                    var map = mutableMapOf<String, Any>()
                     map["cate"] = idRecibido.toString()
                   // map["cate"] = cate
                    map["marca"] = sub
                    map["imagen"] = imagen




                    FirebaseFirestore.getInstance().collection("Subcatergoria")
                        .document().set(map)
                    finish()
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
        setContentView(R.layout.activity_actividad_agregar_sub_cat)

        storage = Firebase.storage

        // dare init a firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference


        btCargarSub = findViewById(R.id.btCargar_sub)
        imageView_sub.setOnClickListener(this)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        // dar funcion al swith..........................................
        /*switch_push.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                tvSwitch?.setTextColor(getColor(R.color.WhiteColor))
                btCargar.setOnClickListener {
                    uploadFile()
                }

            } else {
                tvSwitch?.setTextColor(getColor(R.color.amarillo))
                btCargar.setOnClickListener { uploadFile() }

            }
        }*/
        btCargarSub?.setOnClickListener { uploadFile() }

        exTraerDatos()

    }

    fun exTraerDatos() {
        viewModel.fetchUserDataSubcate(idRecibido).observe(this, androidx.lifecycle.Observer {
            adapter?.mutableListSub = it as ArrayList<SubCategorias>
            adapter?.notifyDataSetChanged()
            val autocompletar = mutableListOf<String>()
            val autocompletarMarca = mutableListOf<String>()
            val autocompletarCate = mutableListOf<String>()

            for (x in it) {
                //autocompletar.add(x.nombre)
                autocompletarMarca.add(x.marca)
                autocompletarCate.add(x.cate)

            }
            /*val adapterAuto = ArrayAdapter(this, android.R.layout.simple_list_item_1, autocompletar)
            tvNombre.threshold = 0
            tvNombre.setAdapter(adapterAuto)
            tvNombre.setOnFocusChangeListener { view, b ->
                if (b) tvNombre.showDropDown()
            }

            val adapterAutoMarca =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, autocompletarMarca)
            tvMarca.threshold = 0
            tvMarca.setAdapter(adapterAutoMarca)
            tvMarca.setOnFocusChangeListener { view, b ->
                if (b) tvMarca.showDropDown()
            }*/

            /*val adapterAutoCate =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, autocompletarCate)
            tvCategoria.threshold = 0
            tvCategoria.setAdapter(adapterAutoCate)
            tvCategoria.setOnFocusChangeListener { view, b ->
                if (b) tvCategoria.showDropDown()
            }*/
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

            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }


        }

    /*fun lanzarPush() {
        val title = tvNombre.text.toString()
        val message = tvMarca.text.toString() + "  " + tvCategoria.text.toString()
        if (title.isNotEmpty() && message.isNotEmpty()) {
            PushNotification(
                NotificationData(title, message),
                TOPIC
            ).also {
                sendNotification(it)
            }
        }
    }*/

    override fun onClick(v: View?) {
        if (v === imageView_sub)
            showFilerChooser()


    }
}