package com.santiagotorres.clothify.model

data class CartItem(
    val id: String,
    val articulo: String,
    val urlPicture: String,
    var cantidad: Int,
    val precio: Double
) {
    fun getPrecioTotal(): Double {
        return cantidad * precio
    }
}
//1