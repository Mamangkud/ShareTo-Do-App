
package com.example.sharetodo.activity

import Database.MyTask
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.sharetodo.R
import com.example.sharetodo.entity.ItemLIst
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_addtask.*
import kotlinx.android.synthetic.main.activity_detail_task.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddTaskActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database: FirebaseDatabase? = null
    var layoutList: LinearLayout? = null

    private var lisItem: ArrayList<ItemLIst> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addtask)
        layoutList = findViewById(R.id.layout_list_upd)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        if(currentUser != null) {
            bt_addlist_upd.setOnClickListener{
                addList()
            }
            bt_mytask.setOnClickListener{
                checkAndRead()
                saveData("Private")
                finish()
                val intent = Intent(this@AddTaskActivity, MyTaskActivity::class.java)
                startActivity(intent)

            }
            bt_Everyone.setOnClickListener{
                checkAndRead()
                saveData("Public")
                finish()
                val intent = Intent(this@AddTaskActivity, MainActivity::class.java)
                startActivity(intent)
            }

            bt_back1.setOnClickListener {
                val intent = Intent(this@AddTaskActivity, MyTaskActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    private fun addList(){
        val v: View = layoutInflater.inflate(R.layout.card_layout, null, false)

        val listItem = v.findViewById(R.id.edt_list) as EditText
        val deleteList = v.findViewById(R.id.bt_deletelist) as ImageView

        deleteList.setOnClickListener { removeView(v) }
        layoutList!!.addView(v)
    }

    private fun removeView(view: View) {
        layoutList!!.removeView(view)
    }

    private fun checkAndRead(): Boolean {
        lisItem.clear()
        var result = true
        for (i in 0 until layoutList!!.childCount) {

            val listItem = layoutList!!.getChildAt(i)

            val edtList = listItem.findViewById(R.id.edt_list) as EditText
            val il = ItemLIst()

            if(edtList.text.toString() != "") {
                il.setItem(edtList.text.toString())
            } else {
                result = false
                break
            }

            lisItem.add(il)
        }

        if (lisItem.size == 0) {
            Toast.makeText(this, "Add Item List First!", Toast.LENGTH_SHORT).show()
        } else if (!result) {
            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show()
        }

        return true
    }

    private fun saveData(akses: String){
        val judul = edt_update_judul.text.toString().trim()
        val ref = FirebaseDatabase.getInstance().reference
        val myTaskId = ref.push().key
        val userID = auth.uid.toString()
        val sdf = SimpleDateFormat("HH:mm a")
        val cal = Calendar.getInstance()
        val waktu = sdf.format(cal.time)
        val myTask = MyTask(userID, myTaskId, judul, waktu, lisItem)

        if(myTaskId != null){
            ref.child("Tasks").child(myTaskId).setValue(myTask).addOnCompleteListener{
                Toast.makeText(applicationContext, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
            ref.child("MyTask").child(userID).child(myTaskId).setValue(true)
            if(akses == "Public"){
                ref.child("PublicTask").child(myTaskId).setValue(userID)
            }
        }
    }
}



