package com.example.adminapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adminapp.databinding.ActivityStatBlockBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class StatBlock : AppCompatActivity(), StatblockAdapter.Listener, StatsetAdapter.Listener {
    lateinit var binding: ActivityStatBlockBinding
    var nameblock: String?=null
    var idstat: String?=null
    private lateinit var auth: FirebaseAuth
    private val adapter1 = StatblockAdapter(this)
    private val adapter2 = StatsetAdapter(this)
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        binding = ActivityStatBlockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idstat = intent.getStringExtra("name").toString()
        val buttonback = findViewById(R.id.BackMain) as ImageButton
        buttonback.setOnClickListener {

            val myIntent = Intent(this@StatBlock, MainActivity::class.java)
            this@StatBlock.startActivity(myIntent)
        }
        binding.imageButton.setOnClickListener{
            binding.rcView.removeAllViews()
            Toast.makeText(this, "tap!", Toast.LENGTH_LONG).show()
            readData2()
            initre2()
        }
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
        readData()
        init()
    }
    fun replaceFragmentB(){
        val myIntent = Intent(this@StatBlock,MainActivity::class.java)
        //myIntent.putExtra("key", android.R.attr.value) //Optional parameters
        this@StatBlock.startActivity(myIntent)
    }
    fun newstat(){
        val myIntent = Intent(this@StatBlock,NewStatActivity::class.java)
        //myIntent.putExtra("key", android.R.attr.value) //Optional parameters
        this@StatBlock.startActivity(myIntent)
    }
    fun replaceFragmentM() {
        val parentLayout = findViewById<View>(android.R.id.content)
        val snack = Snackbar.make(findViewById(R.id.BAZA), "Shure?", Snackbar.LENGTH_SHORT)
        snack.setAction("Yess"){
            auth.signOut()
            finish()
            val myIntent = Intent(this@StatBlock,LogIn::class.java)
            //myIntent.putExtra("mode", 1) //Optional parameters
            this@StatBlock.startActivity(myIntent)
        }.setActionTextColor(getResources().getColor(android.R.color.holo_red_light )).show();

    }
    private fun init(){
        binding.apply {
            rcView.layoutManager = GridLayoutManager(this@StatBlock, 1)
            rcView.adapter = adapter1
            textView.text = nameblock
        }
    }
    private fun initre2(){
        binding.apply {
            rcView.layoutManager = GridLayoutManager(this@StatBlock, 1)
            rcView.adapter = adapter2
            textView.text = nameblock
        }
    }
    private fun readData(){
        database =  Firebase.database.getReference("Article")
        println(idstat.toString())
        Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("CountBlock").get().addOnSuccessListener {
            var t=it.value.toString().toInt()
            println(it.value.toString()+"<---- !!")
            Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("name").get().addOnSuccessListener {
                nameblock = it.value.toString()
            }
            var h = 1
            while(h <= t){
                Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("Textes").child("tx"+h.toString()).get().addOnSuccessListener {
                    adapter1.addblock(SatBlockData(it.value.toString()))
                    println(it.value.toString())
                }
                h+=1
            }
        }
    }

    private fun readData2(){
        database =  Firebase.database.getReference("Article")
        println(idstat.toString())
        Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("CountBlock").get().addOnSuccessListener {
            var t=it.value.toString().toInt()
            println(it.value.toString()+"<---- !!")
            Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("name").get().addOnSuccessListener {
                nameblock = it.value.toString()
            }
            var h = 1
            while(h <= t){
                Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("Textes").child("tx"+h.toString()).get().addOnSuccessListener {
                    adapter2.addblock(StatyaSet(it.value.toString()))
                    println(it.value.toString())
                }
                h+=1
            }
        }
    }
    override fun onClick(statBlockData: SatBlockData) {
        //Toast.makeText(this, "--> ${statBlockData.title}", Toast.LENGTH_LONG).show()
        """val myIntent = Intent(this@MainActivity, StatBlock::class.java)
        myIntent.putExtra("name", stat.title)
        this@MainActivity.startActivity(myIntent)"""
    }

    override fun onClick(statyaSet: StatyaSet) {
        TODO("Not yet implemented")
    }
}