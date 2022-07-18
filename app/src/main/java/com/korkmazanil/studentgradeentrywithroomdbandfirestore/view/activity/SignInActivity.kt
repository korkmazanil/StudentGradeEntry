package com.korkmazanil.studentgradeentrywithroomdbandfirestore.view.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.FirestoreClass
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.R
import com.korkmazanil.studentgradeentrywithroomdbandfirestore.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity() {

    private var _binding : ActivitySignInBinding? = null
    private val binding : ActivitySignInBinding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var signInOkSharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setProgressBar(binding.progressBarSignIn)
        auth = Firebase.auth

        //Shared Preferences Initialize
        signInOkSharedPreferences = getSharedPreferences(
            "com.korkmazanil.studentgradeentrywithroomdbandfirestore.view.activity",
            MODE_PRIVATE)

        binding.signInToSignUp.setOnClickListener {
            startActivity(Intent(this@SignInActivity,SignUpActivity::class.java))
            finish()
        }

        binding.signInBtn.setOnClickListener {
            val emailAddress = binding.emailAddressEtSignIn.text.toString()
            val password = binding.passwordEtSignIn.text.toString()
            signIn(emailAddress,password)
        }
    }

    fun signInSuccess() {

        val inputText = getString(R.string.wellcome)
        Toast.makeText(this@SignInActivity,
            inputText,
            Toast.LENGTH_LONG).show()

        startActivity(Intent(this@SignInActivity,MainActivity::class.java))
        finish()
        hideProgressBar()
    }
    private fun signIn(emailAddress: String, password: String) {

        showProgressBar()

        if (emailAddress.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(emailAddress,password)
                .addOnCompleteListener(this){task->
                    if (task.isSuccessful){
                        signInOkSharedPreferences.edit().putBoolean("signInOK",true).apply()
                        FirestoreClass().signInUser(this)
                    }
                }
                .addOnFailureListener {
                    hideProgressBar()
                    Toast.makeText(this, getString(R.string.password_and_email_incorrrect), Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        val signInOK = signInOkSharedPreferences.getBoolean("signInOK",false)
        if (currentUser != null && signInOK ){
            startActivity(Intent(this@SignInActivity,MainActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}