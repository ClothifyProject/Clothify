package com.santiagotorres.clothify.ui.recovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santiagotorres.clothify.data.ResourceRemote
import com.santiagotorres.clothify.data.UserRepository
import kotlinx.coroutines.launch
//a
class RecoveryViewModel : ViewModel() {


    private val userRepository = UserRepository()


    private val _errorMsg: MutableLiveData<String> = MutableLiveData()
    val errorMsg: LiveData<String> = _errorMsg

    private val _itscorrect: MutableLiveData <Boolean> = MutableLiveData()
    val isCorrect: LiveData <Boolean> = _itscorrect

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+[.]+[a-z]+"

    fun validateFields(email: String) {
        if (email.isEmpty()){
            _errorMsg.value = "Digite su correo"
        }
        else{
            if (!email.matches(emailPattern.toRegex())){
                _errorMsg.value = "Ingrese un correo válido"
            }
            else{
                //_itscorrect.value = true

                viewModelScope.launch {
                    when (val resourceRemote = userRepository.recoveryUser(email)){
                        is ResourceRemote.Success ->{
                            _itscorrect.postValue(true)
                        }
                        is ResourceRemote.Error -> {
                            var msg = resourceRemote.message
                            when (resourceRemote.message){
                                "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> msg = "Revise su conexión de internet"
                                "The password is invalid or the user does not have a password." -> msg = "Correo electrónico o contraseña inválida"
                                "There is no user record corresponding to this identifier. The user may have been deleted." -> msg = "No existe una cuenta asociada a este correo electrónico"
                            }
                            _errorMsg.postValue(msg!!)
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }
}

