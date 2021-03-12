package com.example.navdrawer.fragmentos

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navdrawer.R
import com.example.navdrawer.adapters.PdfAdapter
import com.example.navdrawer.modelos_de_datos.ModeloPdf
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class PdfFragment : Fragment() {
    val TAG = "ActividadAgregar"

    private var precio: String? = null
    private var datosRecibidos:ArrayList<ModeloPdf>? = null
    var adaptadorPdf:PdfAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var tvTotalPresup:TextView? = null
    var tvEspacio:TextView? = null
    var etNombrePdf:EditText? = null
    var etDireccionPdf:EditText? = null
    private var recyclerViewPdf: RecyclerView? = null
    var guardarPdf:Button? = null
    var mostrarrPdf:Button? = null
    private val PICK_IMAGE_REQUEST = 1234

    private val STORAGE_CODE: Int = 100
    private var storageReference: StorageReference? = null
    private var filePath: Uri? = null
    lateinit var storage: FirebaseStorage


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_pdf, container, false)

        precio = arguments?.getString(PRECIOS_TOTALES)
        datosRecibidos = arguments?.getSerializable(PROD_SELECT) as ArrayList<ModeloPdf>

        tvTotalPresup = view.findViewById(R.id.tvTotalPresupuesto)
        recyclerViewPdf = view.findViewById(R.id.recyclerPdf)
        guardarPdf = view.findViewById(R.id.guardar_Pdf)
        mostrarrPdf = view.findViewById(R.id.mostrar_Pdf)
        etNombrePdf = view.findViewById(R.id.etNombre_Pdf)
        etDireccionPdf = view.findViewById(R.id.etDireccion_Pdf)
        tvEspacio = view.findViewById(R.id.tv_Espacio)

        storage = Firebase.storage

        // dare init a firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference


        tvTotalPresup?.text = "Total: $precio"
        // inflaré el recyclerPdf
        recyclerViewPdf?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerViewPdf?.layoutManager =layoutManager
        adaptadorPdf = PdfAdapter(datosRecibidos!!)
        recyclerViewPdf?.adapter = adaptadorPdf

        guardarPdf?.setOnClickListener {
            //necesitamos manejar permisos de tiempo de ejecución para dispositivos con marshmallow  y superior
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                //sistema operativo> = marshMallow (6.0), verifique que el permiso esté habilitado o no
                if (checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED){
                    // no se otorgó permiso, solicítelo
                    val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, STORAGE_CODE)
                    Toast.makeText(context, "denegado", Toast.LENGTH_LONG).show()
                }
                else{
                    //permiso ya otorgado, llamar al método savepdf
                    savePdf()
                        //activity?.finish()
                    Toast.makeText(context, "archivado en el dispositivo", Toast.LENGTH_LONG).show()


                }
            }
            else{
                //permiso ya otorgado, llamar al método savepdf
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    savePdf()

                }
            }
        }


        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun savePdf(){
        // crear objeto de la clase document
        val mDoc = Document()
        // pdf. nombre del archivo
        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        Log.e("Nombre PDF", mFileName.toString())
        val mFilePath = Environment.getExternalStorageDirectory().absolutePath + "/" + mFileName + ".Pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()


            val mPrecioTotal = tvTotalPresup?.text.toString()
            val nombrePresu = etNombrePdf?.text
            val domicilioPresu = etDireccionPdf?.text
            val espacio = tvEspacio?.text.toString()




            mDoc.addAuthor("Hernan Torres")

            val table = PdfPTable(4)

            table.headerRows = 1
            table.addCell(
                PdfPCell(
                    Phrase(
                        "Producto", Font(
                            Font.FontFamily.HELVETICA, 16f,
                            Font.BOLD
                        )
                    )
                )
            )
            table.addCell(
                PdfPCell(
                    Phrase(
                        "Cantidad", Font(
                            Font.FontFamily.HELVETICA, 16f,
                            Font.BOLD
                        )
                    )
                )
            )
            table.addCell(
                PdfPCell(
                    Phrase(
                        "Precio", Font(
                            Font.FontFamily.HELVETICA, 16f,
                            Font.BOLD
                        )
                    )
                )
            )
            table.addCell(
                PdfPCell(
                    Phrase(
                        "Subtotal", Font(
                            Font.FontFamily.HELVETICA, 16f,
                            Font.BOLD
                        )
                    )
                )
            )

            mDoc.add(table)
            val nombrePdf = Paragraph(
                "                    Domicilio: $domicilioPresu", Font(
                    Font.FontFamily.HELVETICA,
                    12f,
                    Font.BOLD
                )
            )
            nombrePdf.alignment = Element.ALIGN_LEFT
            mDoc.add(nombrePdf)

            mDoc.add(table)
            val domicilioPdf = Paragraph(
                "                    Nombre Completo: $nombrePresu", Font(
                    Font.FontFamily.HELVETICA,
                    12f,
                    Font.BOLD
                )
            )
            domicilioPdf.alignment = Element.ALIGN_LEFT
            mDoc.add(domicilioPdf)

            mDoc.add(table)
            val espacioPdf = Paragraph(espacio, Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD))
            espacioPdf.alignment = Element.ALIGN_CENTER
            mDoc.add(espacioPdf)

            for (list in datosRecibidos!!){
                table.addCell(PdfPCell(Phrase(list.producto, Font(Font.FontFamily.HELVETICA, 12f))))
                table.addCell(
                    PdfPCell(
                        Phrase(
                            list.cantidad.toString(), Font(
                                Font.FontFamily.HELVETICA,
                                12f
                            )
                        )
                    )
                )
                table.addCell(
                    PdfPCell(
                        Phrase(
                            list.precio.toString(), Font(
                                Font.FontFamily.HELVETICA,
                                12f
                            )
                        )
                    )
                )
                table.addCell(
                    PdfPCell(
                        Phrase(
                            list.subTotal.toString(), Font(
                                Font.FontFamily.HELVETICA,
                                12f
                            )
                        )
                    )
                )



            }


            mDoc.add(table)
            val preT = Paragraph(mPrecioTotal, Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD))
            preT.alignment = Element.ALIGN_CENTER
            mDoc.add(preT)



            mDoc.close()
           // Log.e("PDF ARCHIVo", mDoc.htmlStyleClass)

            Toast.makeText(
                context,
                " $mFileName.pdf\nse guardó en \n$mFilePath",
                Toast.LENGTH_SHORT
            ).show()

           /* var url = "herny"

            var map = mutableMapOf<String, Any>()
            // map["id"] = id
            map["url"] = url

            FirebaseFirestore.getInstance().collection("Categoria")
                .document().set(map)*/

            val f = File(mFilePath)

            var file = Uri.fromFile(File(mFilePath))
            val riversRef = storageReference?.child("pdf/${file.lastPathSegment}")
            var uploadTask = riversRef?.putFile(file)
            Log.e("File", file.toString())
            Log.e("uploadTask", uploadTask.toString())

// Register observers to listen for when the download is done or if it fails
            uploadTask?.addOnFailureListener { task ->

            }?.addOnSuccessListener { taskSnapshot ->

                val task = taskSnapshot.metadata.toString()

            }
            val ref = storageReference?.child("pdf/$mFilePath")
            uploadTask = ref?.putFile(file)

            val urlTask = uploadTask?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it

                    }
                }
                ref?.downloadUrl

            }?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    enviarPdf(mFilePath)

                } else {
                    // Handle failures
                    // ...
                }
            }


        }
        catch (e: Exception){
        }



    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // se otorgó el permiso de la ventana emergente, llama a savePdf()
                    savePdf()
                } else {
                    // se denegó el permiso de la ventana emergente, muestra mensaje de error
                    Toast.makeText(context, "permiso denegado", Toast.LENGTH_SHORT).show()


                }
            }
        }
        // esto en el activity principal
    }

    companion object {
        const val PROD_SELECT = "ProductosSelect"
        const val PRECIOS_TOTALES = "PreciosTotales"


        const val volver = "volver"
        fun newInstancePdf(datosRecibidos: ArrayList<ModeloPdf>, preciosRecibidos: String): PdfFragment {
            val bundle = Bundle()
            bundle.putSerializable(PROD_SELECT, datosRecibidos)
            bundle.putString(PRECIOS_TOTALES, preciosRecibidos)

            val fragment = PdfFragment()
            fragment.arguments = bundle



            return fragment
        }


    }


    fun enviarPdf(mFilePath: String) {


        var file = File("$mFilePath")
        val nTel = "+541133545454"
        var pdfUri = Uri.fromFile(file)

        val intent = Intent()
        val uri = FileProvider.getUriForFile(
            requireContext(),
            requireActivity().packageName.toString() + ".provider",
            file
        )
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pdfUri = FileProvider.getUriForFile(
                requireContext(),
                requireActivity().packageName.toString() + ".provider", file)
            Log.e("URL1", pdfUri.toString())

        } else {
            pdfUri = Uri.fromFile(file)
            Log.e("URL2", pdfUri.toString())

        }
        val share = Intent()
        share.action = Intent.ACTION_SEND
        share.type = "application/pdf"
        share.putExtra(Intent.EXTRA_STREAM, pdfUri)
        startActivity(Intent.createChooser(share, "Share"))
    }
}