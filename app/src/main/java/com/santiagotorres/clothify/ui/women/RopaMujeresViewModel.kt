package com.santiagotorres.clothify.ui.women

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santiagotorres.clothify.data.ResourceRemote
import com.santiagotorres.clothify.data.RopaRepository
import com.santiagotorres.clothify.model.item
import kotlinx.coroutines.launch

class RopaMujeresViewModel : ViewModel() {
    private val repository = RopaRepository()

    private val _List: MutableLiveData<List<item>?> = MutableLiveData()
    val seriesList: MutableLiveData<List<item>?> = _List

    private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    fun loadPrendas() {
        viewModelScope.launch {
            val result = repository.loadRopa("women")
            when (result) {
                is ResourceRemote.Success -> {
                    _List.postValue(result.data)
                }
                is ResourceRemote.Error -> {
                    _errorMsg.postValue(result.message)
                }
                else -> {}
            }
        }
    }
}