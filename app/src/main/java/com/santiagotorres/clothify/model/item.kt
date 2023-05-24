package com.santiagotorres.clothify.model

data class item(
    val articulo: String?,
    val precio: String?,
    val urlPicture: String?,
    var cantidad: Int = 0
)