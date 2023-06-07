package com.santiagotorres.clothify.ui.ninos

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.santiagotorres.clothify.databinding.FragmentRopaNinosBinding
import com.santiagotorres.clothify.model.item
import com.santiagotorres.clothify.ui.main.ItemAdapter

class RopaNinosFragment : Fragment() {

    private var _binding: FragmentRopaNinosBinding? = null
    private val binding get() = _binding!!

    private lateinit var ninosAdapter: ItemAdapter
    private val ninosList: ArrayList<item> = ArrayList()

    private val ninosViewModel: RopaNinosViewModel by lazy {
        ViewModelProvider(this)[RopaNinosViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRopaNinosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ninosAdapter = ItemAdapter(ninosList) { ninos ->
            Log.d("articulo", ninos.articulo!!)
        }

        binding.ninosRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ninosAdapter
            setHasFixedSize(false)
        }

        ninosViewModel.loadPrendas()

        ninosViewModel.errorMsg.observe(viewLifecycleOwner) { errorMsg ->
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
        }

        ninosViewModel.seriesList.observe(viewLifecycleOwner) { ninosList ->
            ninosAdapter.appendItems(ninosList as ArrayList<item>)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}