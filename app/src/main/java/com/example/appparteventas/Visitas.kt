package com.example.appparteventas

class Visitas {

    var cliente: String = ""
        get() = field
        set(value) { field= value}

    var comentarios: String = ""
        get() = field
        set(value) { field= value}


    constructor(){

    }

    constructor(cliente: String, comentarios: String) {
        this.cliente = cliente
        this.comentarios = comentarios
    }





    override fun toString(): String {
        return super.toString()
    }
}