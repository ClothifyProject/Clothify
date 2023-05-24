package com.santiagotorres.clothify.ui.women

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.santiagotorres.clothify.R
import com.santiagotorres.clothify.databinding.FragmentRopaHombresBinding
import com.santiagotorres.clothify.databinding.FragmentRopaMujeresBinding
import com.santiagotorres.clothify.model.item
import com.santiagotorres.clothify.ui.main.ItemAdapter
import com.santiagotorres.clothify.ui.men.RopaHombresViewModel

class RopaMujeresFragment : Fragment() {

        private var _binding: FragmentRopaMujeresBinding? = null
        private val binding get() = _binding!!

        private lateinit var mujeresAdapter: ItemAdapter
        private val mujeresList: ArrayList<item> = ArrayList()

        private val mujeresViewModel: RopaMujeresViewModel by lazy {
            ViewModelProvider(this).get(RopaMujeresViewModel::class.java)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentRopaMujeresBinding.inflate(inflater, container, false)
            val root: View = binding.root

            mujeresAdapter = ItemAdapter(mujeresList) { ninos ->
                Log.d("articulo", ninos.articulo!!)
            }

            binding.mujeresRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = mujeresAdapter
                setHasFixedSize(false)
            }

            mujeresViewModel.loadPrendas()

            mujeresViewModel.errorMsg.observe(viewLifecycleOwner) { errorMsg ->
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
            }

            mujeresViewModel.seriesList.observe(viewLifecycleOwner) { hombresList ->
                mujeresAdapter.appendItems(hombresList as ArrayList<item>)
            }

            return root
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }


}