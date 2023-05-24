package com.santiagotorres.clothify.ui.carrito

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.santiagotorres.clothify.R
import com.santiagotorres.clothify.data.ResourceRemote
import com.santiagotorres.clothify.data.RopaRepository
import com.santiagotorres.clothify.databinding.CardViewCartItemBinding
import com.santiagotorres.clothify.model.CartItem
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarritoAdapter(
    private var cartItemList: MutableList<CartItem>
) : RecyclerView.Adapter<CarritoAdapter.CartItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_cart_item, parent, false)
        return CartItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = cartItemList[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }

    fun updateCartItems(newCartItemList: MutableList<CartItem>) {
        cartItemList = newCartItemList
        notifyDataSetChanged()
    }

    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: CardViewCartItemBinding =
            CardViewCartItemBinding.bind(itemView)
//HOLA
        @SuppressLint("SetTextI18n")
        fun bind(cartItem: CartItem) {
            binding.articuloTextView.text = cartItem.articulo
            binding.cantidadTextView.text = "Cantidad: ${cartItem.cantidad.toString()}"
            binding.precioTextView.text = "Precio Total: $${cartItem.getPrecioTotal().toString()}"
//hola

            // Cargar la imagen de urlPicture utilizando Picasso
            Picasso.get().load(cartItem.urlPicture).into(binding.imageView)

            binding.deleteButton.setOnClickListener {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val ropaRepository = RopaRepository()

                if (userId != null) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val deleteResult = withContext(Dispatchers.IO) {
                            ropaRepository.deleteCarritoItem(userId, cartItem.id)
                        }

                        when (deleteResult) {
                            is ResourceRemote.Success -> {
                                // Eliminación exitosa
                                val position = adapterPosition
                                if (position != RecyclerView.NO_POSITION) {
                                    cartItemList.remove(cartItem) // Eliminar el elemento de la lista
                                    notifyItemRemoved(position) // Notificar al adaptador que se eliminó un elemento
                                }
                            }
                            is ResourceRemote.Error -> {
                                // Manejar el caso de error
                                Log.e("CarritoAdapter", "Error eliminando el artículo del carrito: ${deleteResult.message}")
                            }
                            else -> {}
                        }
                    }
                }
            }

        }


    }

}
