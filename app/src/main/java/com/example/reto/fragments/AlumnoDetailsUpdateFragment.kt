package com.example.reto.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.reto.FirestoreManager
import com.example.reto.R
import com.example.reto.databinding.FragmentAlumnoDetailsUpdateBinding
import com.google.firebase.firestore.FirebaseFirestore

class AlumnoDetailsUpdateFragment : Fragment(R.layout.fragment_alumno_details_update) {

    private var _binding: FragmentAlumnoDetailsUpdateBinding? = null
    private val binding get() = _binding!!


    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlumnoDetailsUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        // Get the arguments passed from the previous fragment
        val args = arguments
        if (args != null && args.containsKey("childId")) {
            val childId = args.getString("childId")
            if (childId != null) {
                // Fetch child details from Firestore based on childId
                fetchChildDetails(childId)
                binding.btnUUpate.setOnClickListener {
                    updateChildUsuarioFirebase(childId)
                }
                binding.btnURegresar.setOnClickListener{
                    it.findNavController().navigate(R.id.action_alumnoDetailsUpdateFragment_to_updateAlumnoFragment)
                }
            }
        }


    }

    private fun fetchChildDetails(childId: String){
        // Fetch existing child details from Firestore
        FirestoreManager.firestore.collection("alumnos").document(childId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val nombre = documentSnapshot.getString("nombre")
                    val apellido = documentSnapshot.getString("apellido")
                    val edad = documentSnapshot.getLong("edad")?.toString()
                    val nivelCog = documentSnapshot.getLong("nivel_cognitivo")?.toString()
                    val nivelMotriz = documentSnapshot.getLong("nivel_motriz")?.toString()

                    // Populate EditTexts with current values
                    binding.uNombre.setText(nombre)
                    binding.uApellido.setText(apellido)
                    binding.uEdad.setText(edad)
                    binding.uNivelCog.setText(nivelCog)
                    binding.uNivelMotriz.setText(nivelMotriz)
                } else {
                    Toast.makeText(
                        mView.context,
                        "No se encontró el alumno con ID: $childId",
                        Toast.LENGTH_LONG
                    ).show()
                    view?.findNavController()?.popBackStack()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    mView.context,
                    "Error al obtener datos del alumno: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                view?.findNavController()?.popBackStack()
            }

    }

    private fun updateChildUsuarioFirebase(childId: String) {
        // Retrieve updated values from EditTexts
        val nombre = binding.uNombre.text.toString().trim()
        val apellido = binding.uApellido.text.toString().trim()
        val edad = binding.uEdad.text.toString().trim().toIntOrNull() ?: -1
        val nivelCog = binding.uNivelCog.text.toString().trim().toIntOrNull() ?: -1
        val nivelMotriz = binding.uNivelMotriz.text.toString().trim().toIntOrNull() ?: -1

        if (nombre.isNotEmpty() && apellido.isNotEmpty() && edad != -1 && nivelCog != -1 && nivelMotriz != -1) {
            val updatedAlumno = hashMapOf(
                "nombre" to nombre,
                "apellido" to apellido,
                "edad" to edad,
                "nivel_cognitivo" to nivelCog,
                "nivel_motriz" to nivelMotriz,
                "nivel_actual_minijuego" to 0
            )

            // Update existing child user in Firestore
            FirestoreManager.firestore.collection("alumnos").document(childId)
                .set(updatedAlumno)
                .addOnSuccessListener {
                    Toast.makeText(
                        mView.context,
                        "Alumno actualizado con éxito",
                        Toast.LENGTH_LONG
                    ).show()
                    mView.findNavController().popBackStack()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        mView.context,
                        "Error al actualizar al alumno: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        } else {
            Toast.makeText(mView.context, "No puede haber campos vacíos", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
