package com.example.admin_paktravel

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.admin_paktravel.Models.TourModel
import com.example.admin_paktravel.databinding.ActivityAddTourBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class AddTourActivity : AppCompatActivity() {
    //variables to get data
    private lateinit var tourName:String
    private lateinit var tourItinerary:String
    private lateinit var tourInclusions:String
    private lateinit var tourCost:String
    private lateinit var tourOperator:String
    private var tourImage:Uri?=null
    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding:ActivityAddTourBinding by lazy {
        ActivityAddTourBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()

        binding.addtourDataButton.setOnClickListener {
            tourName=binding.tourName.text.toString().trim()
            tourItinerary=binding.tourItinerary.text.toString().trim()
            tourInclusions=binding.tourInclusions.text.toString().trim()
            tourCost=binding.tourCost.text.toString().trim()
            tourOperator=binding.tourOperator.text.toString().trim()

            if(!(tourName.isBlank()||tourItinerary.isBlank()||tourInclusions.isBlank()||tourCost.isBlank()||tourOperator.isBlank()))
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
        val tourRef=database.getReference("menu")
        //generate a unique key for the new menu item
        val newItemKey=tourRef.push().key

        if(tourImage!=null)
        {
            val storageRef=FirebaseStorage.getInstance().reference
            val imageRef=storageRef.child("tour_images/${newItemKey}.jpg")
            val uplaodTask=imageRef.putFile(tourImage!!)

            uplaodTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    downloadUrl->
                    val newTour= TourModel(
                        tourName=tourName,
                        tourItinerary=tourItinerary,
                        tourInclusions=tourInclusions,
                        tourCost=tourCost,
                        tourOperator=tourOperator,
                        tourImage=downloadUrl.toString()
                    )
                    newItemKey?.let {
                        key->
                        tourRef.child(key).setValue(newTour).addOnSuccessListener {
                            Toast.makeText(this, "Data added successfully.", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener{
                                Toast.makeText(this, "Data uploading failed.", Toast.LENGTH_SHORT).show()
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
            tourImage=uri

        }
    }
}