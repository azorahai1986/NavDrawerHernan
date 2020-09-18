package com.example.navdrawer.modelos_de_datos

class SubCategorias(var id: String,
                    val cate: String,
                    val sub: String,
                    val imagen: String,
                    val marca: String,
                    val precio: String) {
    constructor():this("", "", "", "", "", "")
}