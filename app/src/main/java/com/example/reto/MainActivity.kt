package com.example.reto

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.reto.databinding.ActivityMainBinding
import com.example.reto.localdatabase.UsuarioDatabase
import com.example.reto.repository.UsuarioRepository
import com.example.reto.viewmodel.UsuarioViewModel
import com.example.reto.viewmodel.UsuarioViewModelFactory
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var usuarioViewModel: UsuarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        FirebaseApp.initializeApp(this)









    }
    private fun setUpViewModel(){
        val usuarioRepository = UsuarioRepository(UsuarioDatabase(this))

        val viewModelProviderFactory = UsuarioViewModelFactory(application, usuarioRepository)

        usuarioViewModel = ViewModelProvider(this, viewModelProviderFactory).get(UsuarioViewModel::class.java)
    }
}