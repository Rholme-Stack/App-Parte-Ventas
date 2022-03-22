package com.example.appparteventas

class Marcas {


    var marca: String="" ;
    var formaDepago: String ="";
    var descuento: Int = 0;
    var cometariosMarcas: String= "";


    constructor(){

    }

    constructor(marca: String, formaDepago: String, descuento: Int, cometariosMarcas: String) {
        this.marca = marca
        this.formaDepago = formaDepago
        this.descuento = descuento
        this.cometariosMarcas = cometariosMarcas
    }
}