package com.example.sharetodo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sharetodo.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_registration.*

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgotpasswordButton.setOnClickListener {
            val email: String = et_forgotPassword.text.toString().trim() {it <= ' '}
            if (email.isEmpty()){
                Toast.makeText(this@ForgotPassword, "Please enter your email address", Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            Toast.makeText(this@ForgotPassword, "Email sent successfully to reset your password", Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(this@ForgotPassword, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        iv_backf.setOnClickListener {
            startActivity(Intent(this@ForgotPassword, LoginActivity::class.java))
        }
    }

}