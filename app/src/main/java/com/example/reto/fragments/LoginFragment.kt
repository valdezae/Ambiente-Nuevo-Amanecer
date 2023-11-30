package com.example.reto.fragments

import android.content.Context
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
import com.example.reto.MainActivity
import com.example.reto.R
import com.example.reto.databinding.FragmentLoginBinding
import com.example.reto.databinding.FragmentSignUpBinding
import com.example.reto.localdatabase.UsuarioDAO
import com.example.reto.model.UserType
import com.example.reto.model.Usuario
import com.example.reto.usuarioadapter.UsuarioAdapter
import com.example.reto.viewmodel.UsuarioViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var usuarioViewModel: UsuarioViewModel
    private lateinit var usuarioAdapter: UsuarioAdapter
    private lateinit var mView : View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        // Sample user types
        val userTypes = arrayOf("ADMIN", "PARENT", "CHILD")

// Initialize the spinner

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usuarioViewModel = (activity as MainActivity).usuarioViewModel
        mView = view


        binding.btnIngresar.setOnClickListener{
            authUserFirebase()
        }

        binding.btnLoginRegresar.setOnClickListener{
            it.findNavController().navigate(R.id.action_loginFragment2_to_homeFragment)
        }

    }

    private fun validateUser(view: View){
        val userName = binding.editTextUserName.text.toString().trim()
        val userPassword = binding.editTextPassword.text.toString().trim()


        if(userName.isNotEmpty() && userPassword.isNotEmpty()){
            usuarioViewModel.searchUsuario(userName,userPassword).observe(viewLifecycleOwner, { users ->
                if(users.isNotEmpty()){
                    val user = users[0]
                    Toast.makeText(mView.context,"Login Exitoso", Toast.LENGTH_LONG).show()
                    usuarioViewModel.userType.value = user.userType.toString()
                    view.findNavController().navigate(R.id.action_loginFragment2_to_loginFragment)
                }
                else{
                    Toast.makeText(mView.context, "Credenciales invalidas", Toast.LENGTH_LONG).show()
                }
            })
        }
        else{
            Toast.makeText(mView.context, "Nombre de usuario o contraseña vacios", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun authUserFirebase(){
        val userName = binding.editTextUserName.text.toString().trim()
        val userPassword = binding.editTextPassword.text.toString().trim()
        if(userName.isNotEmpty() && userPassword.isNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword("$userName@nuevoAmanecer.com", userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User is logged in successfully
                        Toast.makeText(mView.context, "Autenticación Exitosa.", Toast.LENGTH_LONG).show()
                        mView.findNavController().navigate(R.id.action_loginFragment2_to_loginFragment)

                    } else {
                        Toast.makeText(mView.context, "Credenciales invalidas.", Toast.LENGTH_LONG).show()
                    }
                }
        }
        else{
            Toast.makeText(mView.context, "Ningun campo puede estar vacío.", Toast.LENGTH_LONG).show()
        }

    }

}

