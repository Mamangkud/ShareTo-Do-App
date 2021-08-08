package com.example.sharetodo.activity

import Database.MyTask
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.sharetodo.R
import com.example.sharetodo.entity.ItemLIst
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail_mytask.*
import kotlinx.android.synthetic.main.activity_detail_task.*
import java.text.SimpleDateFormat
import java.util.*

class DetailTaskActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database : DatabaseReference
    lateinit var judul : TextView
    lateinit var itemList : TextView
    private var lisItem: ArrayList<ItemLIst> = ArrayList()
    var layoutList : LinearLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)
        layoutList = findViewById(R.id.layout_list_dta)

        getAndSetData()

        auth = FirebaseAuth.getInstance()
        bt_clone.setOnClickListener{
            checkAndReadList()
            cloneData()
            val intent = Intent(this@DetailTaskActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this,"Task berhasil di clone ke My Task",Toast.LENGTH_SHORT).show()
            finish()
        }

        bt_back4.setOnClickListener {
            val intent = Intent(this@DetailTaskActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getAndSetData() {
        judul = findViewById(R.id.tvd_judul)
        if (intent.extras != null) {
            judul.text = intent.getStringExtra("Judul")
            for (i in (intent.extras!!.getSerializable("Itemlist") as java.util.ArrayList<ItemLIst>)){
                val v: View = layoutInflater.inflate(R.layout.card_layout_dta, null, false)
                itemList = v.findViewById(R.id.tv_list_dta)
                itemList.text = i.getItem()
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
            val tvList = listItem.findViewById<View>(R.id.tv_list_dta) as TextView
            val il = ItemLIst()

            if (tvList.text.toString() != "") {
                il.setItem(tvList.text.toString())
            } else {
                result = false
                break
            }
            lisItem.add(il)
        }
        return result
    }

    private fun cloneData(){
            val ref = FirebaseDatabase.getInstance().reference
            val myTaskId = ref.push().key
            val userID = intent.getStringExtra("Author").toString()
            val sdf = SimpleDateFormat("HH:mm a")
            val cal = Calendar.getInstance()
            val waktu = sdf.format(cal.time)
            val myTask = MyTask(userID, myTaskId, judul.text.toString(), waktu, lisItem)

            if(myTaskId != null){
                ref.child("Tasks").child(myTaskId).setValue(myTask).addOnCompleteListener{
                    Toast.makeText(applicationContext, "Data berhasil diclone", Toast.LENGTH_SHORT).show()
                }
                ref.child("MyTask").child(auth.uid.toString()).child(myTaskId).setValue(true)
            }
    }
}