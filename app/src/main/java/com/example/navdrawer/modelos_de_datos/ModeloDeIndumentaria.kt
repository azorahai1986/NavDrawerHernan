package com.example.navdrawer.modelos_de_datos

class ModeloDeIndumentaria(
    var id: String,
    val cate: String,
    val marca: String,
    val imagen: String,
    val nombre: String,
    val precio: String,
    val arrayImagen: String
){
    constructor():this("", "","","","","", "")
}