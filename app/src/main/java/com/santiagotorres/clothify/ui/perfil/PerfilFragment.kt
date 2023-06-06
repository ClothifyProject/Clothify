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
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView



class PerfilFragment : Fragment() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageView: ImageView


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

        val buttonOpenGallery: Button = view.findViewById(R.id.buttonOpenGallery)
        buttonOpenGallery.setOnClickListener {
            openGallery()
        }

        perfilBinding.imagenPerfil.setImageResource(R.drawable.imagen_perfil)


        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data ?: return
            //imageView.setImageURI(selectedImageUri)
            perfilBinding.imagenPerfil.setImageURI(selectedImageUri)

        }
    }

}