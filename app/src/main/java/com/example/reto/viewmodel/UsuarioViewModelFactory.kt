package com.example.reto.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.reto.repository.UsuarioRepository

class UsuarioViewModelFactory(val app: Application, private val usuarioRepository: UsuarioRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UsuarioViewModel(app,usuarioRepository) as T
    }
}