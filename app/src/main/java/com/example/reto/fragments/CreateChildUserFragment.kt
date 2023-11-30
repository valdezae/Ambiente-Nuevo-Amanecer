package com.example.reto.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.reto.FirestoreManager
import com.example.reto.MainActivity
import com.example.reto.R
import com.example.reto.databinding.FragmentCreateChildUserBinding
import com.example.reto.databinding.FragmentSignUpBinding
import com.example.reto.localdatabase.UsuarioDAO
import com.example.reto.model.ChildDetails
import com.example.reto.model.UserType
import com.example.reto.model.Usuario
import com.example.reto.usuarioadapter.UsuarioAdapter
import com.example.reto.viewmodel.UsuarioViewModel
import com.google.firebase.firestore.FirebaseFirestore


class CreateChildUserFragment : Fragment(R.layout.fragment_create_child_user) {

    private var _binding: FragmentCreateChildUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var usuarioViewModel: UsuarioViewModel
    private lateinit var usuarioAdapter: UsuarioAdapter
    private lateinit var mView : View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateChildUserBinding.inflate(inflater, container, false)
        // Sample user types


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usuarioViewModel = (activity as MainActivity).usuarioViewModel
        mView = view
        binding.btnCcuRegistrar.setOnClickListener{
            saveChildUsuarioFirebase(it)
        }
    }

    private fun saveChildUsuarioFirebase(view: View) {
        val nombre = binding.ccuNombre.text.toString().trim()
        val apellido = binding.ccuApellido.text.toString().trim()
        val edad = binding.ccuEdad.text.toString().trim().toIntOrNull() ?: -1
        val nivelCog = binding.ccuNivelCog.text.toString().trim().toIntOrNull() ?: -1
        val nivelMotriz = binding.ccuNivelMotriz.text.toString().trim().toIntOrNull() ?: -1

        if (nombre.isNotEmpty() && apellido.isNotEmpty() && edad != -1 && nivelCog != -1 && nivelMotriz != -1) {
            val alumno = hashMapOf(
                "nombre" to nombre,
                "apellido" to apellido,
                "edad" to edad,
                "nivel_cognitivo" to nivelCog,
                "nivel_motriz" to nivelMotriz,
                "nivel_actual_minijuego" to 0
            )

            // Assuming you have a Firestore collection named "childUsers"
            FirestoreManager.firestore.collection("alumnos")
                .add(alumno)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(mView.context, "Alumno guardado con éxito", Toast.LENGTH_LONG).show()
                    view.findNavController().navigate(R.id.action_createChildUserFragment_to_adminPanelFragment)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(mView.context, "Error al guardar al alumno: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(mView.context, "No puede haber campos vacíos", Toast.LENGTH_LONG).show()
        }
    }


    private fun saveChildUsuarioLocal(view: View) {
        val nombre = binding.ccuNombre.text.toString().trim()
        val apellido = binding.ccuApellido.text.toString().trim()

        val edad = if (binding.ccuEdad.text.toString().trim().isNotEmpty()) {
            binding.ccuEdad.text.toString().trim().toInt()
        } else {
            -1
        }
        val nivelCog = if (binding.ccuNivelCog.text.toString().trim().isNotEmpty()) {
            binding.ccuNivelCog.text.toString().trim().toInt()
        } else {
            -1
        }
        val nivelMotriz = if (binding.ccuNivelMotriz.text.toString().trim().isNotEmpty()) {
            binding.ccuNivelMotriz.text.toString().trim().toInt()
        } else {
            -1
        }

        if (nombre.isNotEmpty() && apellido.isNotEmpty() && edad != -1 && nivelCog != -1 && nivelMotriz != -1) {
            val childUsuario = ChildDetails(0, nombre, apellido, edad, nivelCog, nivelMotriz, 0)
            usuarioViewModel.insertChildDetails(childUsuario)
            Toast.makeText(mView.context, "Usuario guardado con exito", Toast.LENGTH_LONG).show()
            view.findNavController().navigate(R.id.action_createChildUserFragment_to_adminPanelFragment)
        } else {
            Toast.makeText(mView.context, "No puede haber campos vacios", Toast.LENGTH_LONG).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}