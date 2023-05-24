package com.santiagotorres.clothify.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.santiagotorres.clothify.R
import com.santiagotorres.clothify.databinding.ActivityAcercaBinding

class AcercaActivity : AppCompatActivity() {
    private lateinit var acercaBinding : ActivityAcercaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        acercaBinding =ActivityAcercaBinding.inflate(layoutInflater)
        val view = acercaBinding.root
        setContentView(view)

        // Agrega la flecha de devolver en el ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Agrega un método que maneje la acción de la flecha de devolver
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}