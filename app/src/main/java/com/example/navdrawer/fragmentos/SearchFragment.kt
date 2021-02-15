package com.example.navdrawer.fragmentos

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navdrawer.R
import com.example.navdrawer.adapters.AdapterBusqueda
import com.example.navdrawer.enlace_con_firebase.MainViewModelo
import com.example.navdrawer.modelos_de_datos.ModeloDeIndumentaria
import com.example.navdrawer.modelos_de_datos.ModeloPdf
import kotlinx.android.synthetic.main.activity_actividad_busqueda.*
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {
    var etSearch: EditText? = null
    var adapter: AdapterBusqueda? = null
    private var pdfdatosArray: ArrayList<ModeloDeIndumentaria>?= null
    private var pdfPreciosArray: ArrayList<Double>?= null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var recyclerView: RecyclerView? = null
    var btEnviarPdf:ImageButton? = null
    var tvCantidadSearch:TextView? = null
    var tvTotalPrecio:TextView? = null


    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModelo::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        tvCantidadSearch = view.findViewById(R.id.tvCantidad)
        tvTotalPrecio = view.findViewById(R.id.tvTotPrecio)

        val intent = Intent()
        val bundle = intent.extras
        val arrayCantidades =bundle?.getIntegerArrayList("arrayCant")
        val arrayPrecios =bundle?.get("arrayPrec")
        obtenerDatosAdapter(arrayCantidades!!, arrayListOf())
        Log.e("Sumatoria Cantidad", arrayCantidades.toString())

        // inflar recycler.......................
        recyclerView = view.findViewById(R.id.recycler_busqueda)
        layoutManager = GridLayoutManager(context, 1)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        adapter = AdapterBusqueda(arrayListOf(), context as FragmentActivity, SearchFragment())
        recyclerView?.adapter = adapter

        // pasaré datos al pdf.......................................................

        btEnviarPdf = view.findViewById(R.id.ibEnviarAPdf)
        btEnviarPdf?.setOnClickListener {
            if (tvTotPrecio.text.isNullOrEmpty()){
                Toast.makeText(context, "Agrega productos a la lista", Toast.LENGTH_SHORT).show()
            }else{
                // igualaré el array a la los datos que estan en el recycler promociones para enviar esos datos al recyclerPdf
                pdfdatosArray = adapter?.arrayModelBudqueda
                val enviarDatosAlPdf = arrayListOf<ModeloPdf>()
                if (pdfdatosArray != null){
                    for (index in 0 until pdfdatosArray!!.size) {
                        if(pdfPreciosArray!![index] > 0){
                            val productosSeleccionados = ModeloPdf()
                            productosSeleccionados.producto = pdfdatosArray!![index].nombre
                            productosSeleccionados.precio = pdfdatosArray!![index].precio.toDouble()
                            productosSeleccionados.cantidad = ((pdfPreciosArray!![index] / productosSeleccionados.precio).toInt())
                            productosSeleccionados.subTotal = pdfPreciosArray!![index]
                            enviarDatosAlPdf.add(productosSeleccionados)
                        }
                    }

                    /*val intent = Intent(this, Pdf::class.java)
                    intent.putExtra(Pdf.PROD_SELECT, enviarDatosAlPdf)
                    intent.putExtra("Total Precios", tvTotPrecio.text.toString())
                    startActivity(intent)*/
                }
            }
        }

        etSearch = view.findViewById(R.id.edt_actSearch)
        requireActivity().edt_actSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.e("dentro del editText", s.toString())
                adapter?.filtrado(s.toString())


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        observeData()
        return view
    }
    fun observeData(){
        viewModel.fetchUserData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter!!.setData(it as ArrayList<ModeloDeIndumentaria>)
            adapter!!.notifyDataSetChanged()

        })

    }


    fun obtenerDatosAdapter(arrayCant: ArrayList<Int>, arrayPrec:ArrayList<Double>) {
        var sum = 0
        for (i in arrayCant){
            sum += i
            //Log.e("Sumatoria Cantidad", arrayCant.toString())

        }

        var sumPrecio = 0.0
        for (p in arrayPrec){
            sumPrecio += p
            //Log.e("Sumatoria Precio", arrayPrec.toString())

        }



    }

}