package com.example.appparteventas

class Marcas {


    var marca1: String = ""
        get() = field
        set(value) { field= value}

    var formaPago1: String = ""
        get() = field
        set(value) { field= value}

    var dto1: String = ""
        get() = field
        set(value) { field= value}

    var comentarios1: String = ""
        get() = field
        set(value) { field= value}

    var marca2: String = ""
        get() = field
        set(value) { field= value}

    var formaPago2: String = ""
        get() = field
        set(value) { field= value}

    var dto2: String = ""
        get() = field
        set(value) { field= value}

    var comentarios2: String = ""
        get() = field
        set(value) { field= value}

    var marca3: String = ""
        get() = field
        set(value) { field= value}

    var formaPago3: String = ""
        get() = field
        set(value) { field= value}

    var dto3: String = ""
        get() = field
        set(value) { field= value}

    var comentarios3: String = ""
        get() = field
        set(value) { field= value}



    constructor(){

    }




    constructor(
        marca1: String,
        formaPago1: String,
        dto1: String,
        comentarios1: String,
        marca2: String,
        formaPago2: String,
        dto2: String,
        comentarios2: String,
        marca3: String,
        formaPago3: String,
        dto3: String,
        comentarios3: String
    ) {
        this.marca1 = marca1
        this.formaPago1 = formaPago1
        this.dto1 = dto1
        this.comentarios1 = comentarios1
        this.marca2 = marca2
        this.formaPago2 = formaPago2
        this.dto2 = dto2
        this.comentarios2 = comentarios2
        this.marca3 = marca3
        this.formaPago3 = formaPago3
        this.dto3 = dto3
        this.comentarios3 = comentarios3
    }


}