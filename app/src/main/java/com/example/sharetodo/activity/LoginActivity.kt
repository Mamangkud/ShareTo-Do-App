package com.example.sharetodo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.sharetodo.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.usernameoremailInput
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_registration.*

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
            finish()
        }
        login()
    }

    private fun login(){
        loginButton.setOnClickListener{

            if(TextUtils.isEmpty(usernameoremailInput.text.toString())){
                usernameoremailInput.error = "Please enter Email!"
                return@setOnClickListener
            }else if(TextUtils.isEmpty(passwordLogin.text.toString())) {
                passwordLogin.error = "Please enter Email!"
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(usernameoremailInput.text.toString(), passwordLogin.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                       startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()

                    }else{
                        Toast.makeText(this@LoginActivity, "Login Failed, please try again!", Toast.LENGTH_LONG).show()

                    }
                }
        }

        registerText.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }

        tv_forgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPassword::class.java))
        }
    }
}