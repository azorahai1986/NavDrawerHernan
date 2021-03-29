package com.example.navdrawer.fragmentos

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.navdrawer.R
import com.example.navdrawer.actividades.MainActivity
import com.example.navdrawer.adapters.PagerSimilaresAdapter
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.PagerSimilares
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.dialog_editar.view.*
import kotlinx.android.synthetic.main.dialog_editar_imagen.view.*
import kotlinx.android.synthetic.main.dialog_editar_precio.view.*
import kotlinx.android.synthetic.main.fragment_ver_imagen.*
import java.io.IOException
import java.util.*


class VerImagenFragment : Fragment(), View.OnClickListener {

    // variables para los datos recibidos desde el adapterRecyclerPrincipal............................
    var recibirImagen: String? = null
    var recibirNombre: String? = null
    var recibirMarca: String? = null
    var recibirPrecio: String? = null
    var recibirId: String? = null
    var imageVer:ImageView?= null
    private val PICK_IMAGE_REQUEST = 1234
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    lateinit var storage: FirebaseStorage

// variable para recuperar el usuario
    private lateinit var auth: FirebaseAuth
    var mailRecuperado:String? = null

    companion object {
        const val IMAGENRECIBIDA = "imagenRecibida" //para cuando vuelvo atras recordar donde estaba ubicado
        const val VOLVERBUSQUEDA = "imagenRecibida" //para cuando vuelvo atras recordar donde estaba ubicado
        private const val IM_RECIBIDA = "IM_RECIBIDA" //para recibir la imagen del adapter
        private const val NOMBRE_RECIBIDO = "NOMBRE_RECIBIDO" //para recibir el nombre del adapter
        private const val MARCA_RECIBIDA = "MARCA_RECIBIDA" //para recibir la descripcion del adapter
        private const val PRECIO_RECIBIDO = "PRECIO_RECIBIDO" //para recibir el precio del adapter
        private const val ID_RECIBIDO = "ID_RECIBIDO" //para recibir el precio del adapter y plasmarlo en el viewPager

        fun newInstance(
            recibirImagen: String,
            recibirNombre: String,
            recibirMarca: String,
            precioRecibido: String,
            recibirId: String
        ): VerImagenFragment {

            val bundle = Bundle()
            bundle.putString(IM_RECIBIDA, recibirImagen)
            bundle.putString(NOMBRE_RECIBIDO, recibirNombre)
            bundle.putString(MARCA_RECIBIDA, recibirMarca)
            bundle.putString(PRECIO_RECIBIDO, precioRecibido)
            bundle.putString(ID_RECIBIDO, recibirId)
            val fragment = VerImagenFragment()
            fragment.arguments = bundle


            return fragment

        }

    }

    var adapterSimilar: PagerSimilaresAdapter? = null
    var viewPagerSimilar: ViewPager2? = null
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }

    // variables para los elementos que usarè para eliminar o editar
    var imagenDelAdapter: ImageView? = null
    var nombre: TextView? = null
    var marca: TextView? = null
    var precio: TextView? = null
    var tvTolocaEdit: TextView? = null
    var btEliminar: FloatingActionButton? = null
    var cardImagen:CardView? = null
    var cardProducto:CardView? = null
    var cardPrecio:CardView? = null
    var anim1:LottieAnimationView? = null
    var anim2:LottieAnimationView? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_ver_imagen, container, false)
//  del recycler principal
        storage = Firebase.storage

        // dare init a firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        recibirImagen = arguments?.getString(IM_RECIBIDA)
        recibirNombre = arguments?.getString(NOMBRE_RECIBIDO)
        recibirMarca = arguments?.getString(MARCA_RECIBIDA)
        recibirPrecio = arguments?.getString(PRECIO_RECIBIDO)
        recibirId = arguments?.getString(ID_RECIBIDO)

        imagenDelAdapter = view.findViewById<ImageView>(R.id.imageView_ver_imagen)
        nombre = view.findViewById<TextView>(R.id.textViewNombre)
        marca = view.findViewById<TextView>(R.id.textview_marca)
        precio = view.findViewById<TextView>(R.id.textViewPrecio)
        btEliminar = view.findViewById(R.id.floatEliminarProducto)
        cardImagen = view.findViewById(R.id.card_imagen)
        cardProducto = view.findViewById(R.id.card_producto)
        cardPrecio = view.findViewById(R.id.card_precio)
        var tvElimProducto = view.findViewById<TextView>(R.id.tv_elim_producto)
        imageVer = view.findViewById(R.id.imageView_ver_imagen)
        anim1 = view.findViewById(R.id.animacion2)
        anim2 = view.findViewById(R.id.animacion3)
        // botones del dialog

        imageVer?.setOnClickListener(this)

        Glide.with(requireContext().applicationContext).load(recibirImagen!!).into(imagenDelAdapter!!)
        nombre?.text = recibirNombre
        marca?.text = recibirMarca
        precio?.text = recibirPrecio





        // inflar viewPager......................................

        viewPagerSimilar = view.findViewById(R.id.viewPager_similares) as ViewPager2
        adapterSimilar = PagerSimilaresAdapter(arrayListOf(), context as FragmentActivity)
        viewPagerSimilar?.adapter = adapterSimilar
        observerDataSimil()

        //recuperar usuario..............................................
        auth = Firebase.auth
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            mailRecuperado = user.email

            Log.e("EmailVerIamagen", mailRecuperado.toString())
        }
        if (mailRecuperado.isNullOrEmpty()){
            btEliminar?.visibility = View.GONE
            tvElimProducto.visibility = View.GONE
            tvTolocaEdit?.visibility = View.GONE
            anim1?.visibility = View.GONE
            anim2?.visibility = View.GONE
        }else{

            /*val dilatar = AnimationUtils.loadAnimation(context, R.anim.dilatar)
            val dilatar2 = AnimationUtils.loadAnimation(context, R.anim.abrir2)
            dilatar.interpolator
            dilatar.repeatMode = Animation.REVERSE
            cardImagen?.startAnimation(dilatar)
            nombre?.startAnimation(dilatar)
            cardProducto?.startAnimation(dilatar)
            precio?.startAnimation(dilatar)
            cardPrecio?.startAnimation(dilatar)

            //dilatar2.repeatMode = Animation.REVERSE
            tvTolocaEdit?.startAnimation(dilatar2)*/
        }
        //dar funciones a los botones  textViews...............................
        btEliminar?.setOnClickListener { eliminarProducto() }
        nombre?.setOnClickListener {
            if (mailRecuperado != null){
                dialogEditarNombre()
            }
        }
        precio?.setOnClickListener {
            if (mailRecuperado != null){
                dialogEditarPrecio()
            }
        }
        if (mailRecuperado == null){
            imageVer?.isClickable = false
        }



        return view
    }

    //  funcion para inflar el viewPager
    private fun observerDataSimil() {
        viewModel.fetchUserDataSimilares(recibirMarca).observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
                adapterSimilar!!.similaresArray = it as ArrayList<PagerSimilares>
                adapterSimilar!!.notifyDataSetChanged()
            Log.e("RecibMarca", recibirMarca.toString())
            })

    }

    fun eliminarProducto() {
        FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
            .document(recibirId.toString())
            .delete().addOnSuccessListener {
                Toast.makeText(context, "Archivo Eliminado", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Falló", Toast.LENGTH_SHORT).show()

            }

        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)

    }

    fun editarNombre(etnom: EditText) {

        var nombre = etnom.text.toString()

        if (nombre.isNullOrEmpty()){
            Toast.makeText(context, "Cambia el nombre de tu producto", Toast.LENGTH_SHORT) .show()

        }else{
            var map = mutableMapOf<String, Any>()
            map["nombre"] = nombre
            val editar = FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
                .document(recibirId.toString())
            editar.update(map)
                .addOnSuccessListener {
                    Toast.makeText(context, "Producto Modificado con exito", Toast.LENGTH_SHORT) .show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Falló Modificación", Toast.LENGTH_SHORT).show()

                }
        }



    }
    fun editarPrecio(edPrecio:EditText){
        var precio = edPrecio.text.toString()

        if (precio.isNullOrEmpty()){
            Toast.makeText(context, "Cambia el nombre de tu producto", Toast.LENGTH_SHORT) .show()

        }else{
            var map = mutableMapOf<String, Any>()
            map["precio"] = precio
            val editar = FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
                .document(recibirId.toString())
            editar.update(map)
                .addOnSuccessListener {
                    Toast.makeText(context, "Producto Modificado con exito", Toast.LENGTH_SHORT) .show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Falló Modificación", Toast.LENGTH_SHORT).show()

                }
        }
    }

    fun dialogEditarNombre() {


        val dialogEdiNombre = LayoutInflater.from(context).inflate(R.layout.dialog_editar, null)
        val dialogConstructor = AlertDialog.Builder(context)
            .setView(dialogEdiNombre)

        val etnom = dialogEdiNombre.et_nombre
        Log.e("EtNom", etnom.text.toString())
        editarNombre(etnom)

        //mostrar el dialog
        val alertDialog = dialogConstructor.show()
        dialogEdiNombre.bt_cancelar.setOnClickListener {
            alertDialog.dismiss()

        }
        dialogEdiNombre.bt_aceptar.setOnClickListener {

            if (etnom.text.isNullOrEmpty()){
                Toast.makeText(context, "Agrega un nombre ó cancela", Toast.LENGTH_SHORT)

            }else{
                editarNombre(etnom)
                alertDialog.dismiss()
            }

        }
    }
    fun dialogEditarPrecio() {


        val dialogEditPrecio = LayoutInflater.from(context).inflate(R.layout.dialog_editar_precio, null)
        val dialogConstructor = AlertDialog.Builder(context).setView(dialogEditPrecio)

        val edPrecio = dialogEditPrecio.et_precio
        Log.e("EdPrecio", edPrecio.text.toString())
        editarPrecio(edPrecio)

        //mostrar el dialog
        val alertDialog = dialogConstructor.show()
        dialogEditPrecio.bt_cancelar_precio.setOnClickListener {
            alertDialog.dismiss()

        }
        dialogEditPrecio.bt_aceptar_precio.setOnClickListener {

            if (edPrecio.text.isNullOrEmpty()){
                Toast.makeText(context, "Agrega un Precio ó cancela", Toast.LENGTH_SHORT)

            }else{
                editarPrecio(edPrecio)
                alertDialog.dismiss()
            }

        }
    }

    fun dialogEditarImagen(){
        val dialogEdit = LayoutInflater.from(context).inflate(R.layout.dialog_editar_imagen, null)
        val dialogConstructor = AlertDialog.Builder(context).setView(dialogEdit)

        val alertDialog = dialogConstructor.show()
        dialogEdit.bt_cancelar_imagen.setOnClickListener {
            alertDialog.dismiss()

        }

            dialogEdit.bt_aceptar_imagen.setOnClickListener {
                uploadFile()
               alertDialog.dismiss()
            }
       

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
                imageVer?.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (filePath != null){dialogEditarImagen()}
    }

    private fun uploadFile() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(requireContext())
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


                    var imagen = downloadUri.toString()

                    var map = mutableMapOf<String, Any>()
                    map["imagen"] = imagen

                    val edit = FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
                        .document(recibirId.toString())
                    edit.update(map)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Producto Modificado con exito", Toast.LENGTH_SHORT) .show()
                            progressDialog.dismiss()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Falló Modificación", Toast.LENGTH_SHORT).show()

                        }


                } else {
                    // Handle failures
                    // ...
                }
            }

        }



    }


    private fun showFilerChooser() {
        //Log.e("URL iMAGEN", imagen.toString())

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)




    }
    override fun onClick(v: View?) {
        val imagen = ""
        if (v === imageView_ver_imagen)
            showFilerChooser()



    }
}