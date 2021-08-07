
package com.example.sharetodo.adapter

import Database.MyTask
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.sharetodo.R
import com.example.sharetodo.activity.DetailMyTaskActivity
import com.example.sharetodo.activity.ProfileActivity
import com.example.sharetodo.activity.UpdateTaskActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileAdapter (val mCtx: Context, val LayoutResid: Int, var myTaskList: List<MyTask>) :
    ArrayAdapter<MyTask>(mCtx, LayoutResid, myTaskList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(LayoutResid, null)
        var auth = FirebaseAuth.getInstance()
        val saveProfile: ImageView = view.findViewById(R.id.bt_save)

        val myTask = myTaskList[position]

        updateTask(myTask)

        return view
    }

    private fun updateTask(mt: MyTask) {
        val intent = Intent(mCtx, ProfileActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("Id", mt.id)
        bundle.putSerializable("Author", mt.author)
        intent.putExtras(bundle)
        mCtx.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

}

