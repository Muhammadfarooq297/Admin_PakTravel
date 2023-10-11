package com.example.admin_paktravel

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.admin_paktravel.Models.FoodModel
import com.example.admin_paktravel.Models.RoomModel
import com.example.admin_paktravel.databinding.ActivityAddFoodItemBinding
import com.example.admin_paktravel.databinding.ActivityAddRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddRoomActivity : AppCompatActivity() {
    //variables to get data
    private lateinit var roomType:String
    private lateinit var roomCharges:String
    private lateinit var roomDescription:String
    private lateinit var roomLocations:String

    private var roomImage: Uri?=null
    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val binding: ActivityAddRoomBinding by lazy {
        ActivityAddRoomBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()

        binding.addRoomButton.setOnClickListener {
            roomType=binding.roomTypeText.text.toString().trim()
            roomCharges=binding.roomChargesText.text.toString().trim()
            roomDescription=binding.roomDescriptionText.text.toString().trim()
            roomLocations=binding.roomLocationsText.text.toString().trim()


            if(!(roomType.isBlank()||roomCharges.isBlank()||roomDescription.isBlank()||roomLocations.isBlank()))
            {
                uploadData()
                finish()

            }
            else
            {
                Toast.makeText(this, "Fill all the details.", Toast.LENGTH_SHORT).show()

            }
        }
        binding.selectImage.setOnClickListener {
            pickimage.launch("image/*")

        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }
    private fun uploadData() {
        //get a reference to the "menu" node in the database
        val roomRef=database.getReference("Rooms")
        //generate a unique key for the new menu item
        val newItemKey=roomRef.push().key

        if(roomImage!=null)
        {
            val storageRef= FirebaseStorage.getInstance().reference
            val imageRef=storageRef.child("room_images/${newItemKey}.jpg")
            val uplaodTask=imageRef.putFile(roomImage!!)

            uplaodTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                        downloadUrl->
                    val newRoom= RoomModel(
                        roomType=roomType,
                        roomCharges_perDay=roomCharges,
                        roomDescription=roomDescription,
                        roomLocations =roomLocations,
                        roomImages = downloadUrl.toString()
                    )
                    newItemKey?.let {
                            key->
                        roomRef.child(key).setValue(newRoom).addOnSuccessListener {
                            Toast.makeText(this, "Room details added successfully.", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener{
                                Toast.makeText(this, "Room details uploading failed.", Toast.LENGTH_SHORT).show()
                            }

                    }

                }
                    .addOnFailureListener{
                        Toast.makeText(this, "Image uploading failed.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        else
        {
            Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show()

        }



    }

    val pickimage= registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
        if(uri!=null)
        {
            binding.selectedImage.setImageURI(uri)
            roomImage=uri

        }
    }
}