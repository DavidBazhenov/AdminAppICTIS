package com.example.adminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminapp.databinding.ActivityLogInBinding
import com.example.adminapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), MainMenuAdapter.Listener {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val adapter = MainMenuAdapter(this)
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.InfoBlocks->replaceFragmentM()
                R.id.Map->replaceFragmentB()
                R.id.Profile->newstat()
                else->{
                }
            }
            true
        }
        init()
        readData()
    }
    fun replaceFragmentB(){
        val myIntent = Intent(this@MainActivity,MainActivity::class.java)
        //myIntent.putExtra("key", android.R.attr.value) //Optional parameters
        this@MainActivity.startActivity(myIntent)
    }
    fun newstat(){
        val myIntent = Intent(this@MainActivity,NewStatActivity::class.java)
        //myIntent.putExtra("key", android.R.attr.value) //Optional parameters
        this@MainActivity.startActivity(myIntent)
    }
    fun replaceFragmentM(){
        val parentLayout = findViewById<View>(android.R.id.content)
        val snack = Snackbar.make(findViewById(R.id.BAZA), "Shure?", Snackbar.LENGTH_SHORT)
        snack.setAction("Yess"){
            auth.signOut()
            finish()
            val myIntent = Intent(this@MainActivity,LogIn::class.java)
            //myIntent.putExtra("mode", 1) //Optional parameters
            this@MainActivity.startActivity(myIntent)
        }.setActionTextColor(getResources().getColor(android.R.color.holo_red_light )).show();

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
                var p = h
                Firebase.database.getReference("Article").child("Stati").child("s"+h.toString()).child("name").get().addOnSuccessListener {
                    adapter.addstat(Stat(it.value.toString(), "s"+p.toString()))
                    println("s"+p.toString()+ " " + it.value.toString())
                }
                h+=1
            }
        }
    }

    override fun onClick(stat: Stat) {
        //Toast.makeText(this, "--> ${stat.title}", Toast.LENGTH_LONG).show()
        val myIntent = Intent(this@MainActivity, StatBlock::class.java)
        myIntent.putExtra("name", stat.id)
        myIntent.putExtra("mode", "1")
        this@MainActivity.startActivity(myIntent)
    }
}