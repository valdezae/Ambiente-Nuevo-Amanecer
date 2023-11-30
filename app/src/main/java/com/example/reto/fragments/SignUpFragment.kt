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
import com.example.reto.databinding.FragmentSignUpBinding
import com.example.reto.localdatabase.UsuarioDAO
import com.example.reto.model.UserType
import com.example.reto.model.Usuario
import com.example.reto.usuarioadapter.UsuarioAdapter
import com.example.reto.viewmodel.UsuarioViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var usuarioViewModel: UsuarioViewModel
    private lateinit var usuarioAdapter: UsuarioAdapter
    private lateinit var mView : View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        // Sample user types
        val userTypes = arrayOf("ADMIN", "PARENT")

// Initialize the spinner
        val spinner: Spinner = binding.spinnerUserType
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.prompt = "Selecciona un tipo de usuario"
        spinner.adapter = adapter


// Handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedUserType = userTypes[position]
                // Handle the selected user type
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing here
            }
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usuarioViewModel = (activity as MainActivity).usuarioViewModel
        mView = view
        binding.btnRegistrarse.setOnClickListener{
            signUpFirebase()

        }

        binding.btnSignupRegresar.setOnClickListener{
            it.findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
        }
    }

    private fun saveUsuarioLocal(view: View){
        val userName = binding.editTextUserName.text.toString().trim()
        val userPassword = binding.editTextPassword.text.toString().trim()
        val userTypeString = binding.spinnerUserType.selectedItem.toString()

        val userType = when (userTypeString){
            "ADMIN" -> UserType.ADMIN
            "PARENT" -> UserType.PARENT
            //"CHILD" -> UserType.CHILD
            else -> UserType.PARENT
        }

        if(userName.isNotEmpty() && userPassword.isNotEmpty()){
            val usuario = Usuario(0,userName,userPassword,userType)
            usuarioViewModel.addUsuario(usuario)
            Toast.makeText(mView.context,"Usuario guardado con exito", Toast.LENGTH_LONG).show()
            view.findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
        }
        else{
            Toast.makeText(mView.context, "Algo salio mal", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun signUpFirebase() {
        val userName = binding.editTextUserName.text.toString().trim()
        val userPassword = binding.editTextPassword.text.toString().trim()
        val userTypeString = binding.spinnerUserType.selectedItem.toString()
        if(userName.isNotEmpty() && userPassword.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword("$userName@nuevoAmanecer.com", userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        saveUserDataToFirestore(user?.uid, userName, userPassword, userTypeString)
                    } else {
                        Toast.makeText(
                            requireContext(), "Fallo de autenticación.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        else{
            Toast.makeText(mView.context, "Ningun campo puede estar vacío.", Toast.LENGTH_LONG).show()
        }
    }

    fun saveUserDataToFirestore(userId: String?, userName: String, userPassword: String, userType: String){
        val usuario = hashMapOf(
            "userName" to userName,
            "userPassword" to userPassword,
            "userType" to userType
        )

        if(userId != null){
            FirestoreManager.firestore.collection("usuarios").document(userId)
                .set(usuario)
                .addOnSuccessListener { Toast.makeText(mView.context,"Usuario registrado con exito.", Toast.LENGTH_LONG).show() }
                .addOnFailureListener{ Toast.makeText(mView.context,"No se pudo registrar al usuario en firestore.", Toast.LENGTH_LONG).show()}
                mView.findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
        }
    }
}

