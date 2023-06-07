package com.santiagotorres.clothify.ui.carrito

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.santiagotorres.clothify.data.ResourceRemote
import com.santiagotorres.clothify.data.RopaRepository
import com.santiagotorres.clothify.databinding.FragmentCarritoBinding
import com.santiagotorres.clothify.model.CartItem
import kotlinx.coroutines.launch

class CarritoFragment : Fragment() {
    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!

    private lateinit var carritoAdapter: CarritoAdapter
    private val carritoList: ArrayList<CartItem> = ArrayList()

    private val carritoViewModel: CarritoViewModel by lazy {
        ViewModelProvider(this)[CarritoViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        carritoAdapter = CarritoAdapter(carritoList)

        binding.carritoRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carritoAdapter
        }

        carritoViewModel.loadCarrito()

        carritoViewModel.carritoList.observe(viewLifecycleOwner) { carritoItems ->
            carritoList.clear()
            carritoItems?.let { carritoList.addAll(it) }
            carritoAdapter.notifyDataSetChanged()
        }

        binding.button.setOnClickListener {
            Toast.makeText(requireContext(), "Compra exitosa", Toast.LENGTH_SHORT).show()

            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                val ropaRepository = RopaRepository()
                lifecycleScope.launch {
                    when (val deleteResult = ropaRepository.deleteAllCarritoItems(userId)) {
                        is ResourceRemote.Success -> {
                            Log.d("CarritoFragment", "All carrito items deleted successfully")
                            // Obtener una nueva lista de elementos del carrito vacía
                            val newCartItemList = mutableListOf<CartItem>()
                            // Actualizar la lista de elementos del carrito en el adaptador
                            carritoAdapter.updateCartItems(newCartItemList)
                            // Realizar otras acciones relacionadas con la eliminación de elementos
                        }
                        is ResourceRemote.Error -> {
                            Log.e("CarritoFragment", "Error deleting carrito items: ${deleteResult.message}")
                            // Manejar el caso de error si es necesario
                        }
                        else -> {}
                    }
                }
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}