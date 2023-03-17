package com.example.adminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adminapp.databinding.ActivityMainBinding
import com.example.adminapp.databinding.ActivityStatBlockBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class StatBlock : AppCompatActivity(), StatblockAdapter.Listener {
    lateinit var binding: ActivityStatBlockBinding
    var nameblock: String?=null
    private val adapter = StatblockAdapter(this)
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatBlockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        nameblock = intent.getStringExtra("name").toString()
        val buttonback = findViewById(R.id.BackMain) as ImageButton
        buttonback.setOnClickListener {
            val myIntent = Intent(this@StatBlock, MainActivity::class.java)
            this@StatBlock.startActivity(myIntent)
        }
        init()
        readData()
    }
    private fun init(){
        binding.apply {
            rcView.layoutManager = GridLayoutManager(this@StatBlock, 1)
            rcView.adapter = adapter
            textView.text = nameblock
        }
    }

    private fun readData(){
        database = FirebaseDatabase.getInstance().getReference("Article")
        Firebase.database.getReference("Article").child("CountS").get().addOnSuccessListener {
            var t=it.value.toString().toInt()
            println(it.value.toString()+"<----")
            var h=1
            while(h<=t){
                Firebase.database.getReference("Article").child("Stati").child("s"+h.toString()).child("name").get().addOnSuccessListener {
                    if (it.value.toString() == nameblock){
                        Firebase.database.getReference("Article").child("Stati").child("s"+h.toString()).child("Textes").child("tx1").get().addOnSuccessListener{
                            adapter.addblock(SatBlockData(it.value.toString()))
                            println(it.value.toString())
                        }
                    }

                }
                h+=1
            }
        }
    }

    override fun onClick(statBlockData: SatBlockData) {
        Toast.makeText(this, "--> ${statBlockData.title}", Toast.LENGTH_LONG).show()
        """val myIntent = Intent(this@MainActivity, StatBlock::class.java)
        myIntent.putExtra("name", stat.title)
        this@MainActivity.startActivity(myIntent)"""
    }
}