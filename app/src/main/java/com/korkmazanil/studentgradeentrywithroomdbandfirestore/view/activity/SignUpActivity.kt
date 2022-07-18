package com.korkmazanil.studentgradeentrywithroomdbandfirestore.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.FirestoreClass
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.R
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.databinding.ActivitySignUpBinding
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.model.Teacher
import java.util.*

class SignUpActivity : BaseActivity() {

    private var _binding : ActivitySignUpBinding? = null
    private val binding : ActivitySignUpBinding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setProgressBar(binding.progressBarSignUp)

        auth = Firebase.auth
        db = Firebase.firestore

        binding.signUpToSignIn.setOnClickListener {
            startActivity(Intent(this@SignUpActivity,SignInActivity::class.java))
            finish()
        }

        binding.signUpBtn.setOnClickListener {
            val emailAddress = binding.emailAddressEtSignUp.text.toString().trim()
            val password = binding.passwordEtSignUp.text.toString().trim()
            val userName = binding.userNameEtSignUp.text.toString().trim()
            signUp(emailAddress,password,userName)
        }
    }

    fun userRegisteredSuccess(){
        Toast.makeText(this@SignUpActivity,
            "you have successfully registered", Toast.LENGTH_LONG).show()

        hideProgressBar()
        startActivity(Intent(this@SignUpActivity,SignInActivity::class.java))
        finish()
    }

    private fun signUp(emailAddress: String, password: String, userName: String) {
        showProgressBar()

        if (emailAddress.isNotEmpty() && password.isNotEmpty() && userName.isNotEmpty()){
            auth.createUserWithEmailAndPassword(emailAddress,password)
                .addOnCompleteListener(this@SignUpActivity){task->
                    if (task.isSuccessful){

                       val firebaseUser : FirebaseUser = task.result!!.user!!
                       val registeredEmail = firebaseUser.email!!
                       val user = Teacher(firebaseUser.uid,userName,registeredEmail, Timestamp(Date()))
                       FirestoreClass().registerUser(this,user)
                    }
                    hideProgressBar()
                }
                .addOnFailureListener(this@SignUpActivity){ error->
                    Toast.makeText(this@SignUpActivity,"Error : ${error.localizedMessage}",Toast.LENGTH_LONG).show()
                }
        }else{
            hideProgressBar()
            Toast.makeText(this@SignUpActivity,getString(R.string.all_fields_filled),Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}