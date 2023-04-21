package com.example.adminapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.adminapp.databinding.ActivityEditTextBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class EditTextActivity : AppCompatActivity() {
    lateinit var binding:ActivityEditTextBinding
    private lateinit var database: DatabaseReference
    var nameblock: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        println("Start Oncreate")
        val idstat = intent.getStringExtra("idstat") as? String
        val idblock = intent.getStringExtra("idblock") as? String
        super.onCreate(savedInstanceState)
        binding = ActivityEditTextBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database =  Firebase.database.getReference("Article")
        Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("Textes").child("tx"+(idblock?.substring(3))).get().addOnSuccessListener {
            nameblock = it.value.toString()
            binding.apply {
                edittext.setText(nameblock)
            }
        }
        binding.imageButton8.setOnClickListener{
            returnblock()

        }
        binding.imageView3.setOnClickListener{
            UpdateText()
        }
    }
    private fun UpdateText() {
        val idstat = intent.getStringExtra("idstat") as? String
        val idblock = intent.getStringExtra("idblock") as? String
        Toast.makeText(this@EditTextActivity, "Uploading file...", Toast.LENGTH_SHORT).show()
        println("Uploading file "+"tx"+(idblock?.substring(3)+" "+idstat.toString()))
        Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("Textes").child("tx"+(idblock?.substring(3))).setValue(binding.edittext.text.toString())
        Toast.makeText(this@EditTextActivity, "Успешно загружен!", Toast.LENGTH_SHORT).show()
        println("Успешно загружен!")
        returnblock()
    }
    private fun returnblock() {
        val idstat = intent.getStringExtra("idstat") as? String
        val myIntent = Intent(this@EditTextActivity, StatBlock::class.java)
        myIntent.putExtra("name", idstat)
        myIntent.putExtra("mode", "2")
        this@EditTextActivity.startActivity(myIntent)
    }

}