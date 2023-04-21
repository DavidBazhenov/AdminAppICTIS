package com.example.adminapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adminapp.databinding.ActivityStatBlockBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class StatBlock : AppCompatActivity(), StatblockAdapter.Listener, StatsetAdapter.Listener {
    lateinit var binding: ActivityStatBlockBinding
    var nameblock: String?=null
    var idstat: String?=null
    var mode: String?=null
    var Numb:Int = 0
    private lateinit var auth: FirebaseAuth
    private val adapter1 = StatblockAdapter(this)
    private var adapter2 = StatsetAdapter(this)
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {

        println("start Statblock")
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        binding = ActivityStatBlockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idstat = intent.getStringExtra("name").toString()
        mode = intent.getStringExtra("mode").toString()
        println("idstat = "+idstat+"mode = "+mode)

        Firebase.database.getReference("Article").child("CountS").get().addOnSuccessListener {
            var t=it.value.toString().toInt()
            Numb = t
            if (Numb!=0){
                Numb = Numb - 1
            }
            println("1 Numb == "+ Numb)
        }


        val buttonback = findViewById(R.id.BackMain) as ImageButton
        buttonback.setOnClickListener {

            val myIntent = Intent(this@StatBlock, MainActivity::class.java)
            this@StatBlock.startActivity(myIntent)
        }
        binding.imageButton2.setOnClickListener{
            val snack = Snackbar.make(findViewById(R.id.BAZA), "Shure?", Snackbar.LENGTH_SHORT)
            snack.setAction("Yess"){
                removestat()
            }.setActionTextColor(getResources().getColor(android.R.color.holo_red_light )).show();

        }

        binding.imageButton.setOnClickListener{
            initback()
        }

        binding.applybutton.setOnClickListener{
            val myIntent = Intent(this@StatBlock, StatBlock::class.java)
            myIntent.putExtra("name", idstat)
            myIntent.putExtra("mode", "1")
            println(":start:")
            this@StatBlock.startActivity(myIntent)
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
        if(mode == "2"){
            println("mode :: 2")
            initback()
        }
        else{
            println("mode :: 1")
            readData()
            println(nameblock+"<--")
            init()
        }
    }
    fun initback(){
        binding.rcView.removeAllViews()
        adapter2 = StatsetAdapter(this)
        readData2()
        initre2()
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
        binding.rcView.removeAllViews()
        binding.applybutton.visibility = View.GONE
        binding.imageButton2.visibility = View.GONE
        binding.apply {
            rcView.layoutManager = GridLayoutManager(this@StatBlock, 1)
            rcView.adapter = adapter1
            textView.text = nameblock
        }
    }
    private fun initre2(){
        binding.applybutton.visibility = View.VISIBLE
        binding.imageButton2.visibility = View.VISIBLE
        binding.apply {
            rcView.layoutManager = GridLayoutManager(this@StatBlock, 1)
            rcView.adapter = adapter2
            textView.text = nameblock
        }
    }
    private fun readData(){
        var text: String?=null
        var imgurl: String?=null
        database =  Firebase.database.getReference("Article")
        Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("CountBlock").get().addOnSuccessListener {
            var t=it.value.toString().toInt()
            Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("name").get().addOnSuccessListener {
                nameblock = it.value.toString()
                binding.apply {
                    textView.text = nameblock
                }
            }
            var h = 1
            while(h <= t){
                Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("Textes").child("tx"+h.toString()).get().addOnSuccessListener {
                    text = it.value.toString()
                }
                Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("Images").child(idstat.toString()+"img"+h.toString()).get().addOnSuccessListener {
                    adapter1.addblock(SatBlockData(text.toString(), it.value.toString()))
                }
                h+=1
            }
        }
    }
    private fun removestat(){
        Toast.makeText(this@StatBlock, "data deletion...", Toast.LENGTH_SHORT).show()
        Firebase.database.getReference("Article").child("CountS").setValue(Numb)
        Firebase.database.getReference("Article").child("Stati").child("count").setValue(Numb)
        Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).removeValue()
        Toast.makeText(this@StatBlock, "Успешно удалено!", Toast.LENGTH_SHORT).show()
        val myIntent = Intent(this@StatBlock, MainActivity::class.java)
        this@StatBlock.startActivity(myIntent)

    }
    private fun readData2(){
        database =  Firebase.database.getReference("Article")
        Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("CountBlock").get().addOnSuccessListener {
            var t=it.value.toString().toInt()
            Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("name").get().addOnSuccessListener {
                nameblock = it.value.toString()
                binding.apply {
                    textView.text = nameblock
                }
            }
            var h = 1
            while(h <= t){
                var p = h
                Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("Textes").child("tx"+h.toString()).get().addOnSuccessListener {
                    adapter2.addblock(StatyaSet(it.value.toString(), "img"+p.toString()))
                }
                h+=1
            }
        }
    }

    override fun onClick(statBlockData: SatBlockData) {
        Toast.makeText(this, "низя", Toast.LENGTH_LONG).show()
    }

    override fun onClick(statyaSet: StatyaSet, i: Int) {
        Toast.makeText(this, "pam", Toast.LENGTH_LONG).show()
        if(i == 1){
            val myIntent = Intent(this@StatBlock, EditTextActivity::class.java)
            myIntent.putExtra("name", statyaSet.title)
            myIntent.putExtra("idstat", idstat)
            myIntent.putExtra("idblock", statyaSet.id)
            println("go")
            this@StatBlock.startActivity(myIntent)
        }
        else if(i == 2){
            val myIntent = Intent(this@StatBlock, addpictureactivity::class.java)
            myIntent.putExtra("name", statyaSet.title)
            myIntent.putExtra("idstat", idstat)
            myIntent.putExtra("idblock", statyaSet.id)
            this@StatBlock.startActivity(myIntent)
        }


    }



}