package com.example.adminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminapp.databinding.ActivityLogInBinding
import com.example.adminapp.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), MainMenuAdapter.Listener {
    lateinit var binding: ActivityMainBinding
    private val adapter = MainMenuAdapter(this)
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        readData()
    }
    private fun init(){
        binding.apply {
            rcView.layoutManager = GridLayoutManager(this@MainActivity, 1)
            rcView.adapter = adapter
        }
    }
    private fun readData(){
        println("Start  <----")
        database = FirebaseDatabase.getInstance().getReference("Article")
        Firebase.database.getReference("Article").child("CountS").get().addOnSuccessListener {
            var t=it.value.toString().toInt()
            println(it.value.toString()+"<----")
            var h=1
            while(h<=t){
                Firebase.database.getReference("Article").child("Stati").child("s"+h.toString()).child("name").get().addOnSuccessListener {
                    adapter.addstat(Stat(it.value.toString()))
                    println(it.value.toString())
                }
                h+=1
            }
        }
    }

    override fun onClick(stat: Stat) {
        Toast.makeText(this, "--> ${stat.title}", Toast.LENGTH_LONG).show()
        val myIntent = Intent(this@MainActivity, StatBlock::class.java)
        myIntent.putExtra("name", stat.title)
        this@MainActivity.startActivity(myIntent)
    }
}