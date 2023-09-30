package com.example.admin_paktravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.admin_paktravel.Models.UserModel
import com.example.admin_paktravel.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName:String
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth= Firebase.auth
        // Initialize Firebase Database
        database= Firebase.database.reference



        binding.createButton.setOnClickListener {

            // get text from edittext
            userName=binding.name.text.toString().trim()
            email=binding.emailOrPhone.text.toString().trim()
            password=binding.Password.text.toString()

            if(userName.isBlank()||email.isBlank()||password.isBlank())
            {
                Toast.makeText(this, "Please fill all details.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                createAccount(email,password)
            }

        }
        binding.alreadyaccountButton.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        val locationList= listOf("Islamabad","Lahore","Karachi","Faisalabad")
        val adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView=binding.listofLocation
        autoCompleteTextView.setAdapter(adapter)
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->
            if(task.isSuccessful)
            {
                Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent= Intent(this,LoginActivity::class.java)

                startActivity(intent)
            }
            else
            {
                Toast.makeText(this, "Account creation failed.", Toast.LENGTH_SHORT).show()
            }

        }

    }

    // saving data in to firebase
    private fun saveUserData() {
        // get text from edittext
        userName=binding.name.text.toString().trim()
        email=binding.emailOrPhone.text.toString().trim()
        password=binding.Password.text.toString()
        val user=UserModel(userName,email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }

}