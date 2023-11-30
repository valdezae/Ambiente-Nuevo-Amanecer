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
import com.example.reto.localdatabase.UsuarioDatabase
import com.example.reto.model.ChildDetails
import com.example.reto.model.Usuario
import com.example.reto.repository.UsuarioRepository
import com.example.reto.usuarioadapter.UsuarioAdapter2
import com.example.reto.viewmodel.UsuarioViewModel
import com.example.reto.viewmodel.UsuarioViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AdminPanelFragment : Fragment() {

    private lateinit var binding: FragmentAdminPanelBinding
    private val usuarioAdapter2 = UsuarioAdapter2()
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_admin_panel, container, false)

        binding.childUserRecyclerView.apply {
            adapter = usuarioAdapter2
        }

        val storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        binding.btnAdminPanel.setOnClickListener {
            it.findNavController().navigate(R.id.action_adminPanelFragment_to_loginFragment)
        }
        binding.btnAdminPanelCrearUsuario.setOnClickListener {
            it.findNavController().navigate(R.id.action_adminPanelFragment_to_createChildUserFragment)
        }
        binding.btnAdminPanelAgregarMedia.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"  // Specify the type of media you want to allow
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        binding.btnAdminPanelBorrarCuenta.setOnClickListener {
            borrarCuentaAuth()
        }
        binding.btnAdminPanelUpdateCuenta.setOnClickListener {
            it.findNavController().navigate(R.id.action_adminPanelFragment_to_updateFragment)
        }
        binding.btnAdminPanelCerrarSesion.setOnClickListener{
            it.findNavController().navigate(R.id.action_adminPanelFragment_to_homeFragment2)
        }
        binding.btnAdminPanelUpateAlumno.setOnClickListener{
            it.findNavController().navigate(R.id.action_adminPanelFragment_to_updateAlumnoFragment)
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

                    usuarioAdapter2.differ.submitList(childUsers)
                }
        }


    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = data?.data
                // Handle the selected media URI
                uploadMedia(selectedImageUri)
            }
        }

    private fun uploadMedia(uri: Uri?) {
        if (uri != null) {
            // Create a unique filename (e.g., using timestamp)
            val filename = "media_${System.currentTimeMillis()}"
            val mediaRef = storageRef.child("media/$filename")

            // Upload the file
            mediaRef.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    // File uploaded successfully
                    Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show()
                    // You can get the download URL if needed
                    mediaRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val downloadUrl = downloadUri.toString()
                        // Now you can save the download URL or use it as needed
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle unsuccessful uploads
                    Toast.makeText(context, "Upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }



    private fun borrarCuentaAuth(){
        val user = FirebaseAuth.getInstance().currentUser

        user?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User deleted successfully
                    // Now, proceed to delete user from Firestore
                    borrarCuentaFirestore(user.uid)
                } else {
                    // Handle failure to delete user
                    Toast.makeText(context, "Failed to delete user: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun borrarCuentaFirestore(userId: String) {
        val db = FirestoreManager.firestore
        val userRef = db.collection("usuarios").document(userId)

        userRef.delete()
            .addOnSuccessListener {
                // User document deleted successfully
                Toast.makeText(context, "User deleted from Firestore", Toast.LENGTH_SHORT).show()
                mView.findNavController().navigate(R.id.action_adminPanelFragment_to_homeFragment)
            }
            .addOnFailureListener { exception ->
                // Handle failure to delete user document
                Toast.makeText(context, "Failed to delete user from Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


}