package com.example.admin_paktravel

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.admin_paktravel.Models.RoomModel
import com.example.admin_paktravel.Models.VehicleModel
import com.example.admin_paktravel.databinding.ActivityAddRoomBinding
import com.example.admin_paktravel.databinding.ActivityAddVehicleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddVehicleActivity : AppCompatActivity() {
    //variables to get data
    private lateinit var vehicleType:String
    private lateinit var vehicleCharges:String
    private lateinit var vehicleDescription:String
    private lateinit var vehicleLocations:String

    private var vehicleImage: Uri?=null
    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val binding: ActivityAddVehicleBinding by lazy {
        ActivityAddVehicleBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()

        binding.addVehicleButton.setOnClickListener {
            vehicleType=binding.vehicleTypeText.text.toString().trim()
            vehicleCharges=binding.vehicleChargesText.text.toString().trim()
            vehicleDescription=binding.vehicleDescription.text.toString().trim()
            vehicleLocations=binding.vehiclePickupLocations.text.toString().trim()


            if(!(vehicleType.isBlank()||vehicleCharges.isBlank()||vehicleDescription.isBlank()||vehicleLocations.isBlank()))
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
        val vehicleRef=database.getReference("Vehicles")
        //generate a unique key for the new menu item
        val newItemKey=vehicleRef.push().key

        if(vehicleImage!=null)
        {
            val storageRef= FirebaseStorage.getInstance().reference
            val imageRef=storageRef.child("vehicle_images/${newItemKey}.jpg")
            val uplaodTask=imageRef.putFile(vehicleImage!!)

            uplaodTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                        downloadUrl->
                    val newVehicle= VehicleModel(
                        vehicleType=vehicleType,
                        vehicleCharges_perDay=vehicleCharges,
                        vehicleDescription=vehicleDescription,
                        vehicleLocations =vehicleLocations,
                        vehicleImages = downloadUrl.toString()
                    )
                    newItemKey?.let {
                            key->
                        vehicleRef.child(key).setValue(newVehicle).addOnSuccessListener {
                            Toast.makeText(this, "Vehicle details added successfully.", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener{
                                Toast.makeText(this, "Vehicle details uploading failed.", Toast.LENGTH_SHORT).show()
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
            vehicleImage=uri

        }
    }
}