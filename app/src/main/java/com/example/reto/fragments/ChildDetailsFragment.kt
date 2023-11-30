package com.example.reto.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.reto.FirestoreManager
import com.example.reto.R
import com.example.reto.databinding.FragmentChildDetailsBinding
import com.example.reto.model.ChildDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChildDetailsFragment : Fragment() {

    private lateinit var binding: FragmentChildDetailsBinding
    private val firestore = FirestoreManager.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_child_details, container, false)
        binding.btnCdRegresar.setOnClickListener {
            it.findNavController().navigate(R.id.action_childDetailsFragment_to_adminPanelFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        if (args != null && args.containsKey("childId")) {
            val childId = args.getString("childId")
            if (childId != null) {
                // Fetch child details from Firestore based on childId
                fetchChildDetails(childId)
            }
        }
    }

    private fun fetchChildDetails(childId: String) {
        firestore.collection("alumnos").document(childId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val childDetails = document.data
                    if (childDetails != null) {
                        // Determine user type (ADMIN or PARENT) and update UI accordingly
                        val userId = FirebaseAuth.getInstance().currentUser?.uid

                        if(userId != null){
                            FirebaseFirestore.getInstance().collection("usuarios").document(userId)
                                .get()
                                .addOnSuccessListener { documentSnapshot ->
                                    if(documentSnapshot.exists()){
                                        val userType = documentSnapshot.getString("userType")
                                        if (userType == "ADMIN") {
                                            updateAdminUI(childDetails)
                                        } else if (userType == "PARENT") {
                                            updateParentUI(childDetails)
                                        }
                                    }
                                }
                        }
                    }
                } else {
                    // Handle case where the document does not exist
                }
            }
            .addOnFailureListener { exception ->
                // Handle failures in fetching data
                // Log the exception or show an error message
            }
    }

    private fun updateAdminUI(childDetails: Map<String, Any>) {
        binding.tvCdNombre.text = "Nombre: ${childDetails["nombre"]}"
        binding.tvCdApellido.text = "Apellido: ${childDetails["apellido"]}"
        binding.tvCdEdad.text = "Edad: ${childDetails["edad"].toString()}"
        binding.tvCdNivelCog.text = "Nivel Cognitivo: ${childDetails["nivel_cognitivo"].toString()}"
        binding.tvCdNivelMotriz.text = "Nivel Motriz: ${childDetails["nivel_motriz"].toString()}"
        binding.tvCdNivelMinijuego.text = "Nivel Actual Minijuego: ${childDetails["nivel_actual_minijuego"].toString()}"
    }

    private fun updateParentUI(childDetails: Map<String, Any>) {
        binding.tvCdNombre.text = "Nombre: ${childDetails["nombre"]}"
        binding.tvCdApellido.text = "Apellido: ${childDetails["apellido"]}"
        binding.tvCdEdad.text = "Edad: ${childDetails["edad"].toString()}"
        binding.tvCdNivelCog.text = ""
        binding.tvCdNivelMotriz.text = ""
        binding.tvCdNivelMinijuego.text = "Nivel Actual Minijuego: ${childDetails["nivel_actual_minijuego"].toString()}"
    }
}
