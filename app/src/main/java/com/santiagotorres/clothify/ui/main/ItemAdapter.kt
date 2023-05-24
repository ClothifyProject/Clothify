package com.santiagotorres.clothify.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.santiagotorres.clothify.R
import com.santiagotorres.clothify.data.RopaRepository
import com.santiagotorres.clothify.databinding.CardViewItemBinding
import com.santiagotorres.clothify.model.item
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ItemAdapter(
    private val itemList: ArrayList<item>,
    private val onItemClicked: (item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClicked(item) }
    }

    override fun getItemCount(): Int = itemList.size

    @SuppressLint("NotifyDataSetChanged")
    fun appendItems(newList: List<item>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = CardViewItemBinding.bind(itemView)
        private var isCartOpen = false
        private val repository = RopaRepository()

        @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
        fun bind(item: item) {
            with(binding) {
                itemTextView.text = item.articulo
                precioTextView.text = "Precio: $${item.precio}"
                cantidadTextView.text = item.cantidad.toString()

                // Cargar imagen utilizando Picasso
                Picasso.get()
                    .load(item.urlPicture)
                    .placeholder(R.drawable.carga) // Opcional: imagen de carga mientras se descarga la imagen real
                    .fit()
                    .centerCrop()
                    .into(pictureImageView)

                setCartImage()

                cartImageView.setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            isCartOpen = !isCartOpen
                            setCartImage()
                        }
                    }
                    false
                }

                addImageView.setOnClickListener {
                    item.cantidad++
                    cantidadTextView.text = item.cantidad.toString()
                }

                removeImageView.setOnClickListener {
                    if (item.cantidad > 0) {
                        item.cantidad--
                        cantidadTextView.text = item.cantidad.toString()
                    }
                }

                cartImageView.setOnClickListener {
                    // Lanzar un coroutine para llamar a saveCarrito
                    CoroutineScope(Dispatchers.Main).launch {
                        // Guardar en la colección "carrito" del usuario actual
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        if (userId != null) {
                            repository.saveCarrito(userId, item.articulo, item.precio, item.urlPicture, item.cantidad)
                        }
                        // Mostrar mensaje "Añadido al carrito"
                        showToast(itemView.context, "Añadido al carrito")
                    }
                }
            }
        }

        private fun setCartImage() {
            val image = if (isCartOpen) R.drawable.carrito_2 else R.drawable.carrito
            binding.cartImageView.setImageResource(image)
        }

        // Función para most-rar el Toast
        private fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
