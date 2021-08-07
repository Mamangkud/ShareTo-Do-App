package com.example.sharetodo.adapter

import Database.MyTask
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.sharetodo.R
import com.example.sharetodo.activity.DetailTaskActivity
import com.example.sharetodo.activity.MainActivity
import com.google.firebase.database.*

class PublicTaskAdapter(val mCtx : Context, val LayoutResid : Int, val publicTaskList : List<MyTask>) : ArrayAdapter<MyTask>(mCtx,LayoutResid,publicTaskList){

    lateinit var database : DatabaseReference

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(LayoutResid,null)

        val tvpJudul : TextView = view.findViewById(R.id.tvp_judultask)
        val tvpAuthor : TextView = view.findViewById(R.id.tvp_author)
        val tvpWaktu : TextView = view.findViewById(R.id.tvp_waktu)

        val MyTask = publicTaskList[position]

        tvpJudul.text = MyTask.judul
        loadUsername(MyTask.author, tvpAuthor)
        tvpWaktu.text = MyTask.waktu
        view.setOnClickListener {
            val intent = Intent(mCtx, DetailTaskActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("Id", MyTask.id)
            bundle.putSerializable("Judul", MyTask.judul)
            bundle.putSerializable("Itemlist", MyTask.listItem)
            bundle.putSerializable("Author", MyTask.author)
            intent.putExtras(bundle)
            mCtx.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

        return view
    }

    private fun loadUsername(uID : String, tvAuthor: TextView){
        database = FirebaseDatabase.getInstance().getReference("profile")
        database.child(uID).child("Username").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    tvAuthor.text = snapshot.value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}