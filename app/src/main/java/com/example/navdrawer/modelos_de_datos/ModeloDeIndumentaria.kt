package com.example.navdrawer.modelos_de_datos

class ModeloDeIndumentaria(
    var id: String,
    val cate: String,
    val sub: String,
    val imagen: String,
    val nombre: String,
    val precio: String){
    constructor():this("", "","","","","")
}