package com.santiagotorres.clothify.ui.carrito

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.santiagotorres.clothify.data.ResourceRemote
import com.santiagotorres.clothify.data.RopaRepository
import com.santiagotorres.clothify.model.CartItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarritoViewModel : ViewModel() {
    private val repository: RopaRepository = RopaRepository()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _carritoList: MutableLiveData<List<CartItem>?> = MutableLiveData()
    val carritoList: MutableLiveData<List<CartItem>?> get() = _carritoList

    fun loadCarrito() {
        viewModelScope.launch {
            val currentUser = auth.currentUser
            val userId = currentUser?.uid ?: ""

            if (userId.isNotEmpty()) {
                val resource = repository.loadCarrito(userId)
                if (resource is ResourceRemote.Success) {
                    _carritoList.value = resource.data
                } else if (resource is ResourceRemote.Error) {
                    Log.e("CarritoViewModel", "Error al cargar el carrito: ${resource.message}")
                    _carritoList.value = null
                }
            } else {
                Log.e("CarritoViewModel", "El usuario actual no está autenticado")
                _carritoList.value = null
            }
        }
    }

}