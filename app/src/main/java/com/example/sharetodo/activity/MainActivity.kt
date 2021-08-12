package com.example.sharetodo.activity

import Database.MyTask
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.sharetodo.R
import com.example.sharetodo.adapter.MyTaskAdapter
import com.example.sharetodo.adapter.PublicTaskAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mytask.*

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var publicTaskList: MutableList<MyTask>
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ref = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        publicTaskList = mutableListOf()
        ref.child("PublicTask").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    publicTaskList.clear()
                    for (h in snapshot.children) {
                        var publicTask = MyTask()
                        publicTask.id = h.key
                        publicTaskList.add(publicTask)
                        loadTasks(publicTask)
                    }
                    val adapter =
                        PublicTaskAdapter(
                            applicationContext,
                            R.layout.item_publictask,
                            publicTaskList
                        )
                    lvp_myTask.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        bt_toggle.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Item1 -> startActivity(Intent(this@MainActivity, MainActivity::class.java))
                R.id.Item2 -> startActivity(Intent(this@MainActivity, MyTaskActivity::class.java))
                R.id.Item3 -> startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                R.id.Item4 -> logout()
            }
            true
        }
    }

    private fun loadTasks(publicTask: MyTask) {
        ref.child("Tasks").child(publicTask.id.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val task = snapshot.getValue(MyTask::class.java)
                        if (task != null) {
                            publicTask.judul = task.judul
                            publicTask.author = task.author
                            publicTask.listItem = task.listItem
                            publicTask.waktu = task.waktu
                        }
                        val adapter = PublicTaskAdapter(
                            applicationContext,
                            R.layout.item_publictask,
                            publicTaskList
                        )
                        lvp_myTask.adapter = adapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun logout(){
        auth.signOut()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}