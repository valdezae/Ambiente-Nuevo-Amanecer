package com.example.reto.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.reto.FirestoreManager
import com.example.reto.R
import com.example.reto.databinding.FragmentAdminPanelBinding
import com.example.reto.databinding.FragmentUpdateAlumnoBinding
import com.example.reto.localdatabase.UsuarioDatabase
import com.example.reto.model.ChildDetails
import com.example.reto.model.Usuario
import com.example.reto.repository.UsuarioRepository
import com.example.reto.usuarioadapter.AlumnoUpdateAdapter
import com.example.reto.usuarioadapter.UsuarioAdapter2
import com.example.reto.viewmodel.UsuarioViewModel
import com.example.reto.viewmodel.UsuarioViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UpdateAlumnoFragment : Fragment() {

    private lateinit var binding: FragmentUpdateAlumnoBinding
    private val alumnoUpdateAdapter = AlumnoUpdateAdapter()
    private lateinit var usuarioViewModel: UsuarioViewModel
    private lateinit var mView: View
    private lateinit var storageRef: StorageReference // Declare storageRef at the class level

    companion object {
        private const val PICK_IMAGE_REQUEST = 1 // You can use any integer value
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_update_alumno, container, false)

        binding.childUserRecyclerViewUpdate.apply {
            adapter = alumnoUpdateAdapter
        }

        binding.btnUpdateAlumnoRegresar.setOnClickListener{
            it.findNavController().navigate(R.id.action_updateAlumnoFragment_to_adminPanelFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        // Fetch and display user data from Firestore
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            // Fetch and display user data from Firestore
            FirebaseFirestore.getInstance().collection("alumnos")
                .addSnapshotListener { querySnapshot, exception ->
                    if (exception != null) {
                        // Handle error
                        return@addSnapshotListener
                    }

                    val childUsers = mutableListOf<DocumentSnapshot>()
                    querySnapshot?.documents?.let {
                        childUsers.addAll(it)
                    }

                    alumnoUpdateAdapter.differ.submitList(childUsers)
                }
        }
    }

}