package com.santiagotorres.clothify.ui.recovery

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.santiagotorres.clothify.R
import com.santiagotorres.clothify.databinding.ActivityRecoveryBinding

class RecoveryActivity : AppCompatActivity() {


    private lateinit var recoveryBinding: ActivityRecoveryBinding
    private lateinit var recoveryViewModel: RecoveryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recoveryBinding = ActivityRecoveryBinding.inflate(layoutInflater)
        recoveryViewModel = ViewModelProvider(this)[RecoveryViewModel::class.java]

        val view = recoveryBinding.root
        setContentView(view)


        recoveryViewModel.errorMsg.observe(this){errorMsg ->
            Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_LONG).show()
        }


        recoveryViewModel.isCorrect.observe(this){


            Firebase.auth.sendPasswordResetEmail(recoveryBinding.emailEditText.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val dialog = AlertDialog.Builder(this)
                            .setTitle(R.string.dialog_one_style_title_recovery)
                            .setMessage(R.string.dialog_one_style_message_recovery)
                            .setPositiveButton(R.string.dialog_one_style_positive_btn_recovery) { _, _ ->
                                //val intent = Intent(this, SignInActivity::class.java)
                                //startActivity(intent)
                                //view.dismiss()
                                //recoveryBinding.emailEditText.setText("")
                                onBackPressedDispatcher.onBackPressed()
                            }
                            .setCancelable(false)
                            .create()

                        dialog.show()
                    }
                }

        }


        recoveryBinding.sendButton.setOnClickListener(){
            val email = recoveryBinding.emailEditText.text.toString()

            recoveryViewModel.validateFields(email)

        }
    }
}


