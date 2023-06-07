package com.santiagotorres.clothify.ui.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.santiagotorres.clothify.R
import com.santiagotorres.clothify.databinding.FragmentPerfilBinding


class PerfilFragment : Fragment() {

    private lateinit var perfilBinding: FragmentPerfilBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val PerfilViewModel = ViewModelProvider(this)[PerfilViewModel::class.java]
        perfilBinding=FragmentPerfilBinding.inflate(inflater,container,false)
        val view=perfilBinding.root

        PerfilViewModel.loadUserInfo()

        PerfilViewModel.errorMsg.observe(viewLifecycleOwner){errorMsg ->
            Toast.makeText(requireActivity(), errorMsg, Toast.LENGTH_LONG).show()
        }

        PerfilViewModel.userLoaded.observe(viewLifecycleOwner){user ->
            with(perfilBinding){
                textViewName.text = user?.name
                textViewEmail.text = user?.email

            }
        }

        perfilBinding.imagenPerfil.setImageResource(R.drawable.imagen_perfil)


        return view
    }

}