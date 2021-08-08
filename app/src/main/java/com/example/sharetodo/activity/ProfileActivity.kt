package com.example.sharetodo.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.sharetodo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_registration.*
import java.io.ByteArrayOutputStream
import java.net.URI
import java.util.*

class ProfileActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database: FirebaseDatabase? = null
    private lateinit var ref : DatabaseReference
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var usernamedd : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        ref = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")


        loadProfile()

        profile_image.setOnClickListener {
            bt_savepicture.visibility = View.VISIBLE

        }


        usernamedd.setOnClickListener{
            bt_save.visibility = View.VISIBLE
        }

        bt_save.setOnClickListener {
            saveData()
        }

      /*  iv_backp.setOnClickListener {

        }*/

        toggle = ActionBarDrawerToggle(this, drawerLayout3, R.string.open, R.string.close)
        drawerLayout3.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView3.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Item1 -> startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                R.id.Item2 -> startActivity(Intent(this@ProfileActivity, MyTaskActivity::class.java))
                R.id.Item3 -> startActivity(Intent(this@ProfileActivity, ProfileActivity::class.java))
                R.id.Item4 -> startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
            }
            true
        }

    }

   private fun saveData(){
       val userID = auth.uid.toString()
       var username = usernamedd.text.toString()
       ref.child("profile").child(userID).child("Username").setValue(username).addOnCompleteListener{
               Toast.makeText(applicationContext, "Data berhasil di update ", Toast.LENGTH_SHORT).show()
       }
   }

    private fun loadProfile(){
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)
        usernamedd = findViewById(R.id.usernamedd)

        email.text = "Email : "+ user?.email

        userReference?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                usernamedd.setText(snapshot.child("Username").value.toString())
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
            finish()
        }

        /*iv_backp.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
        }*/
    }
}

