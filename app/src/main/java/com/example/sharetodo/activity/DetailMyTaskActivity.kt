package com.example.sharetodo.activity

import Database.MyTask
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sharetodo.R
import com.example.sharetodo.entity.ItemLIst
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_detail_mytask.*
import java.text.SimpleDateFormat
import java.util.*


class DetailMyTaskActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var database : DatabaseReference
    lateinit var judul : TextView
    lateinit var itemList : TextView
    lateinit var check: CheckBox
    private var lisItem: ArrayList<ItemLIst> = ArrayList()
    var layoutList : LinearLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mytask)
        layoutList = findViewById(R.id.layout_list_dmta)

        getAndSetData()

        auth = FirebaseAuth.getInstance()
        bt_submit_task.setOnClickListener{
            checkAndReadList()
            submitData()
            finish()
            val intent = Intent(this@DetailMyTaskActivity, MyTaskActivity::class.java)
            startActivity(intent)
        }

        bt_back3.setOnClickListener {
            val intent = Intent(this@DetailMyTaskActivity, MyTaskActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getAndSetData() {
        judul = findViewById(R.id.tvd_judul_dmta)
        if (intent.extras != null) {
            judul.text = intent.getStringExtra("Judul")
            for (i in (intent.extras!!.getSerializable("Itemlist") as java.util.ArrayList<ItemLIst>)){
                val v: View = layoutInflater.inflate(R.layout.card_layout_dmta, null, false)
                itemList = v.findViewById(R.id.tv_list_dmta)
                check = v.findViewById(R.id.cb_list_dmta) as CheckBox
                itemList.text = i.getItem()
                if (i.checklist == true){
                    check.isChecked = true
                }
                layoutList!!.addView(v)
            }
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun checkAndReadList(): Boolean {
        lisItem.clear()
        var result = true
        for (i in 0 until layoutList!!.childCount) {

            val listItem = layoutList!!.getChildAt(i)
            val checkList = listItem.findViewById(R.id.cb_list_dmta) as CheckBox
            val tvList = listItem.findViewById(R.id.tv_list_dmta) as TextView

            val il = ItemLIst()
             if (tvList.text.toString() != "") {
                il.setItem(tvList.text.toString())
            } else {
                result = false
                break
            }

            if(checkList.isClickable){
                il.checklist = checkList.isChecked
            } else{
                il.checklist = false
            }

            lisItem.add(il)
        }

        if (lisItem.size == 0) {
            Toast.makeText(this, "Add Item List First!", Toast.LENGTH_SHORT).show()
        } else if (!result) {
            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show()
        }
        return result
    }

    private fun submitData() {
        val ref = FirebaseDatabase.getInstance().reference
        val myTaskId = intent.getStringExtra("Id")
        val userID = auth.uid.toString()
        val sdf = SimpleDateFormat("HH:mm a")
        val cal = Calendar.getInstance()
        val waktu = sdf.format(cal.time)
        val myTask = MyTask(userID, myTaskId, judul.text.toString(), waktu, lisItem)

        if (myTaskId != null) {
            ref.child("Tasks").child(myTaskId).setValue(myTask).addOnCompleteListener {
                Toast.makeText(applicationContext, "Data berhasil di update ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
