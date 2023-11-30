package com.example.reto.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.media.FaceDetector.Face
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.reto.FirestoreManager
import com.example.reto.R
import com.example.reto.databinding.FragmentHomeBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mGoogleSingInClient : GoogleSignInClient
    private val GOOGLE_SIGN_IN = 100
    private val callbackManager = CallbackManager.Factory.create()


    companion object {
        private const val RC_SIGN_IN = 123
        private const val TAG = "HomeFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.btnHomeLogin.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_loginFragment2)
            //if(!TextUtils.isEmpty(binding.editText2.text.toString())){
               //val bundle = bundleOf("name" to binding.editText2.text.toString())

                //it.findNavController().navigate(R.id.action_homeFragment_to_loginFragment, bundle)
            //}
            //else{
                //Toast.makeText(activity, "Enter your name", Toast.LENGTH_LONG).show()
            //}

        }

        binding.btnHomeSignIn.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_signUpFragment)
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnHomeFacebook.setOnClickListener{

            LoginManager.getInstance().logInWithReadPermissions(this,listOf("email"))

            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        result?.let{
                            val token = it.accessToken
                            val credential = FacebookAuthProvider.getCredential(token.token)
                            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                                if(it.isSuccessful){
                                    val user = FirebaseAuth.getInstance().currentUser
                                    Toast.makeText(context, "Ingresado exitoso con Facebook ", Toast.LENGTH_SHORT).show()
                                    saveUserToFirestore(user)
                                }
                                else{
                                    Toast.makeText(context, "Algo salio mal ", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    override fun onCancel() {
                        Toast.makeText(context, "Algo salio mal ", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: FacebookException) {
                        Toast.makeText(context, "Algo salio mal ", Toast.LENGTH_SHORT).show()
                    }
                })
        }



        binding.btnHomeGogle.setOnClickListener{
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(requireContext(),googleConf)

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(requestCode, resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)

                if(account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                        if(it.isSuccessful){
                            val user = FirebaseAuth.getInstance().currentUser
                            saveUserToFirestore(user)
                        }else{

                        }
                    }
                }
            }catch(e: ApiException) {

            }

        }
    }



    private fun saveUserToFirestore(user: FirebaseUser?){
        if(user != null){
            val userMap = hashMapOf(
                "userName" to user.displayName,
                "userPassword" to 0,
                "userType" to "PARENT"
            )

            FirestoreManager.firestore.collection("usuarios")
                .document(user.uid)
                .set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(context, "Autenticacion con google exitosa", Toast.LENGTH_SHORT).show()
                    view?.findNavController()?.navigate(R.id.action_homeFragment_to_loginFragment)
                }
                .addOnFailureListener{
                    Toast.makeText(context, "Algo salio mal ", Toast.LENGTH_SHORT).show()
                }
        }
    }
}