package com.santiagotorres.clothify.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.santiagotorres.clothify.model.CartItem
import com.santiagotorres.clothify.model.item
import kotlinx.coroutines.tasks.await

class RopaRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun loadRopa(categoria: String): ResourceRemote<List<item>> {
        return try {
            val userId = auth.currentUser?.uid
            val querySnapshot = userId?.let { db.collection(categoria).get().await() }
            val ropaList = mutableListOf<item>()

            querySnapshot?.let { snapshot ->
                for (document in snapshot.documents) {
                    val articulo = document.getString("articulo")
                    val precio = document.getString("precio")
                    val urlPicture = document.getString("urlPicture")

                    if (articulo != null && precio != null && urlPicture != null) {
                        val prenda = item(articulo, precio, urlPicture)
                        ropaList.add(prenda)
                    }
                }
            }

            ResourceRemote.Success(ropaList)
        } catch (e: Exception) {
            ResourceRemote.Error(e.message ?: "Error al cargar la ropa")
        }
    }

    suspend fun loadCarrito(userId: String): ResourceRemote<List<CartItem>> {
        return try {
            val querySnapshot = db.collection("users")
                .document(userId)
                .collection("carrito")
                .get()
                .await()

            val cartItemsMap = mutableMapOf<String, CartItem>() // Map to store cart items by ID

            querySnapshot.documents.forEach { document ->
                val itemId = document.id
                val articulo = document.getString("articulo")
                val urlPicture = document.getString("urlPicture")
                val cantidad = document.getLong("cantidad")?.toInt()
                val precio = document.getString("precio")?.toDouble()

                if (articulo != null && urlPicture != null && cantidad != null && precio != null && cantidad > 0) {
                    val existingItem = cartItemsMap.values.find { it.articulo == articulo }
                    if (existingItem != null) {
                        existingItem.cantidad += cantidad // Sum the quantities
                    } else {
                        cartItemsMap[itemId] = CartItem(itemId, articulo, urlPicture, cantidad, precio)
                    }
                }
            }

            val cartItems = cartItemsMap.values.toList()

            ResourceRemote.Success(cartItems)
        } catch (e: FirebaseFirestoreException) {
            Log.e("RopaRepository", "Error al cargar el carrito: ${e.message}")
            ResourceRemote.Error(e.message ?: "Error al cargar el carrito")
        }
    }

    fun saveCarrito(userId: String, articulo: String?, precio: String?, urlPicture: String?, cantidad: Int) {
        val carritoData = hashMapOf(
            "articulo" to articulo,
            "precio" to precio,
            "urlPicture" to urlPicture,
            "cantidad" to cantidad
        )

        db.collection("users")
            .document(userId)
            .collection("carrito")
            .add(carritoData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    suspend fun deleteAllCarritoItems(userId: String): ResourceRemote<Unit> {
        return try {
            val carritoCollectionRef = db.collection("users")
                .document(userId)
                .collection("carrito")

            val querySnapshot = carritoCollectionRef.get().await()

            val batch = db.batch()
            querySnapshot.documents.forEach { document ->
                batch.delete(document.reference)
            }

            batch.commit().await()

            ResourceRemote.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting carrito items: ${e.message}")
            ResourceRemote.Error(e.message ?: "Error deleting carrito items")
        }
    }

    companion object {
        private const val TAG = "RopaRepository"
    }

    suspend fun deleteCarritoItem(userId: String, itemId: String): ResourceRemote<Unit> {
        return try {
            val carritoItemRef = db.collection("users")
                .document(userId)
                .collection("carrito")
                .document(itemId)

            val documentSnapshot = carritoItemRef.get().await()

            if (documentSnapshot.exists()) {
                carritoItemRef.delete().await()
                ResourceRemote.Success(Unit)
            } else {
                ResourceRemote.Error("El artículo del carrito no existe")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al eliminar el artículo del carrito: ${e.message}")
            ResourceRemote.Error(e.message ?: "Error al eliminar el artículo del carrito")
        }
    }
}