package com.example.adminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.adminapp.databinding.ActivityNewStatBinding
import com.example.adminapp.databinding.ActivityStatBlockBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NewStatActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    lateinit var binding: ActivityNewStatBinding
    var Numb:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewStatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.setOnClickListener{
            createnew()
        }
        Firebase.database.getReference("Article").child("CountS").get().addOnSuccessListener {
            var t=it.value.toString().toInt()
            Numb = t
            Numb = Numb + 1
            println("1 Numb == "+ Numb)
        }
    }
    private fun createnew(){
        println("создание блока")
        if(binding.editTextTextPassword2.text.toString() != "" && binding.editTextTextPassword3.text.toString() != "" && binding.editTextTextPassword.text.toString() != ""){
            println("2 Numb == "+ Numb)
            Firebase.database.getReference("Article").child("CountS").setValue(Numb)
            Firebase.database.getReference("Article").child("Stati").child("count").setValue(Numb)
            Firebase.database.getReference("Article").child("Stati").child("s"+Numb.toString()).child("CountBlock").setValue(binding.editTextTextPassword.text.toString().toInt())
            Firebase.database.getReference("Article").child("Stati").child("s"+Numb.toString()).child("Images")
            Firebase.database.getReference("Article").child("Stati").child("s"+Numb.toString()).child("Textes")
            Firebase.database.getReference("Article").child("Stati").child("s"+Numb.toString()).child("block").setValue(binding.editTextTextPassword2.text.toString())
            Firebase.database.getReference("Article").child("Stati").child("s"+Numb.toString()).child("name").setValue(binding.editTextTextPassword3.text.toString())
            println("готво")
            Toast.makeText(this@NewStatActivity, "Статья создана!", Toast.LENGTH_SHORT).show()

            val myIntent = Intent(this@NewStatActivity, StatBlock::class.java)
            myIntent.putExtra("name", "s"+Numb)
            myIntent.putExtra("mode", "2")
            this@NewStatActivity.startActivity(myIntent)


        }
        else{
            Toast.makeText(this@NewStatActivity, "Поля не заполнены", Toast.LENGTH_SHORT).show()
        }
    }
}