package com.example.adminapp

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adminapp.databinding.ActivityAddpictureactivityBinding
import com.example.adminapp.databinding.ActivityStatBlockBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class addpictureactivity : AppCompatActivity() {
    lateinit var binding:ActivityAddpictureactivityBinding
    private lateinit var database: DatabaseReference
    lateinit var ImageURI:Uri
    var nameblock: String?=null
    var isImg = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val NumActivity = intent.getStringExtra("name") as? String
        val idstat = intent.getStringExtra("idstat") as? String
        val idblock = intent.getStringExtra("idblock") as? String
        binding = ActivityAddpictureactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView2.setOnClickListener{
            selectImg()
        }
        binding.imageView3.setOnClickListener{
            uploadImg(idstat!!+idblock!!)
        }
        binding.imageButton8.setOnClickListener{
            returnblock()
        }
    }

    private fun returnblock() {
        val idstat = intent.getStringExtra("idstat") as? String
        val myIntent = Intent(this@addpictureactivity, StatBlock::class.java)
        myIntent.putExtra("name", idstat)
        myIntent.putExtra("mode", "2")
        this@addpictureactivity.startActivity(myIntent)
    }
    private fun uploadImg(name: String) {
        Toast.makeText(this@addpictureactivity, "Uploading file...", Toast.LENGTH_SHORT).show()
        if(isImg){
            println("abc $name")
            val progressDialog = ProgressDialog(this)
            Toast.makeText(this@addpictureactivity, "Uploading file...", Toast.LENGTH_SHORT).show()
            progressDialog.setMessage("Uploading file...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            val storageReference = FirebaseStorage.getInstance().getReference("images/$name")
            storageReference.putFile(ImageURI).
            addOnSuccessListener {
                binding.imageView.setImageURI(null)
                Toast.makeText(this@addpictureactivity, "Successfuly uploaded", Toast.LENGTH_SHORT).show()
                if(progressDialog.isShowing) progressDialog.dismiss()
            }.addOnFailureListener{
                if(progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this@addpictureactivity, "Failed", Toast.LENGTH_SHORT).show()
            }
            setDataurl()
            returnblock()
        }
    }
    private fun setDataurl(){
        val idstat = intent.getStringExtra("idstat") as? String
        val idblock = intent.getStringExtra("idblock") as? String
        val name = idstat!!+idblock!!
        val storageReference = FirebaseStorage.getInstance().getReference("images/$name")
        val path = storageReference.child("images/").child(name)
        path.putFile(ImageURI).addOnCompleteListener{
            if(it.isSuccessful) {
                path.downloadUrl.addOnCompleteListener{
                    if(it.isSuccessful){
                        val photoUrl = it.result.toString()
                        database =  Firebase.database.getReference("Article")
                        Firebase.database.getReference("Article").child("Stati").child(idstat.toString()).child("Images").child(idstat+idblock).setValue(photoUrl)
                    }
                }
            }
        }

    }
    private fun selectImg() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
        isImg = true
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100  && resultCode == RESULT_OK){
            ImageURI = data?.data!!
            binding.imageView.setImageURI(ImageURI)
        }
    }
}