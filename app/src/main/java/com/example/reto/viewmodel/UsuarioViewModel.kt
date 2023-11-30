package com.example.reto.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reto.model.ChildDetails
import com.example.reto.model.Usuario
import com.example.reto.repository.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(app: Application, private val  usuarioRepository: UsuarioRepository) : AndroidViewModel(app){
    val userType = MutableLiveData<String>()

    fun addUsuario(usuario: Usuario) =
        viewModelScope.launch {
            usuarioRepository.insertUsuario(usuario)
        }

    fun deleteUsuario(usuario: Usuario){
        viewModelScope.launch {
            usuarioRepository.deleteUsuario(usuario)
        }
    }

    fun updateUsuario(usuario: Usuario){
        viewModelScope.launch {
            usuarioRepository.updateUsuario(usuario)
        }
    }

    fun getAllUsuarios() = usuarioRepository.getAllUsuarios()

    fun searchUsuario(username: String, password: String) = usuarioRepository.searchUsuario(username,password)


    fun insertChildDetails(childDetails: ChildDetails) =
        viewModelScope.launch {
            usuarioRepository.insertChildDetails(childDetails)
        }

    fun updateChildDetails(childDetails: ChildDetails) =
        viewModelScope.launch {
            usuarioRepository.updateChildDetails(childDetails)
        }

    fun deleteChildDetails(childDetails: ChildDetails) =
        viewModelScope.launch {
            usuarioRepository.deleteChildDetails(childDetails)
        }

    fun getAllChildDetails() = usuarioRepository.getAllChildDetails()



    private val _userTypeLiveData = MutableLiveData<String>()
    val userTypeLiveData: LiveData<String>
        get() = _userTypeLiveData

    fun getUserType(userName: String) {
        viewModelScope.launch {
            val userType = usuarioRepository.getUserType(userName) ?: "PARENT"
            _userTypeLiveData.postValue(userType)
        }
    }
}