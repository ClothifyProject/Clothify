package com.santiagotorres.clothify.ui.men

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.santiagotorres.clothify.databinding.FragmentRopaHombresBinding
import com.santiagotorres.clothify.model.item
import com.santiagotorres.clothify.ui.main.ItemAdapter

class RopaHombresFragment : Fragment() {

    private var _binding: FragmentRopaHombresBinding? = null
    private val binding get() = _binding!!

    private lateinit var hombresAdapter: ItemAdapter
    private val hombresList: ArrayList<item> = ArrayList()

    private val hombresViewModel: RopaHombresViewModel by lazy {
        ViewModelProvider(this)[RopaHombresViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRopaHombresBinding.inflate(inflater, container, false)
        val root: View = binding.root

        hombresAdapter = ItemAdapter(hombresList) { ninos ->
            Log.d("articulo", ninos.articulo!!)
        }

        binding.hombresRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hombresAdapter
            setHasFixedSize(false)
        }

        hombresViewModel.loadPrendas()

        hombresViewModel.errorMsg.observe(viewLifecycleOwner) { errorMsg ->
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
        }

        hombresViewModel.seriesList.observe(viewLifecycleOwner) { hombresList ->
            hombresAdapter.appendItems(hombresList as ArrayList<item>)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}