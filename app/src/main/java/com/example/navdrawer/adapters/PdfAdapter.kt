package com.example.navdrawer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.navdrawer.R
import com.example.navdrawer.modelos_de_datos.ModeloPdf
import kotlinx.android.synthetic.main.item_pdf.view.*

class PdfAdapter(var arraydatos:ArrayList<ModeloPdf>): RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    inner class PdfViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfAdapter.PdfViewHolder =
        PdfViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pdf, parent, false))

    override fun getItemCount(): Int = arraydatos.size

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val pdfLista = arraydatos[position]
        holder.itemView.tv_tem_pdf_cantidad.text = pdfLista.cantidad.toString()
        holder.itemView.tv_tem_pdf_producto.text = pdfLista.producto
        holder.itemView.tv_tem_pdf_precio.text = pdfLista.precio.toString()
        holder.itemView.tv_tem_pdf_subtotal.text = pdfLista.subTotal.toString()
    }
}