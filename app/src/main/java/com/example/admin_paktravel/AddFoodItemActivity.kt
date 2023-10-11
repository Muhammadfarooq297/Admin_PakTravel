package com.example.admin_paktravel

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.admin_paktravel.Models.FoodModel
import com.example.admin_paktravel.Models.TourModel
import com.example.admin_paktravel.databinding.ActivityAddFoodItemBinding
import com.example.admin_paktravel.databinding.ActivityAddTourBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddFoodItemActivity : AppCompatActivity() {

    //variables to get data
    private lateinit var foodName:String
    private lateinit var foodPrice:String
    private lateinit var foodDescription:String
    private lateinit var foodIngredients:String

    private var foodImage: Uri?=null
    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val binding: ActivityAddFoodItemBinding by lazy {
        ActivityAddFoodItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()

        binding.addItemButton.setOnClickListener {
            foodName=binding.foodNameText.text.toString().trim()
            foodPrice=binding.foodPriceText.text.toString().trim()
            foodDescription=binding.description.text.toString().trim()
            foodIngredients=binding.ingredients.text.toString().trim()


            if(!(foodName.isBlank()||foodPrice.isBlank()||foodDescription.isBlank()||foodIngredients.isBlank()))
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
        val foodRef=database.getReference("foods")
        //generate a unique key for the new menu item
        val newItemKey=foodRef.push().key

        if(foodImage!=null)
        {
            val storageRef= FirebaseStorage.getInstance().reference
            val imageRef=storageRef.child("food_images/${newItemKey}.jpg")
            val uplaodTask=imageRef.putFile(foodImage!!)

            uplaodTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                        downloadUrl->
                    val newFood= FoodModel(
                        foodName=foodName,
                        foodPrice=foodPrice,
                        foodDescription=foodDescription,
                        foodIngredients=foodIngredients,
                        foodImage=downloadUrl.toString()
                    )
                    newItemKey?.let {
                            key->
                        foodRef.child(key).setValue(newFood).addOnSuccessListener {
                            Toast.makeText(this, "Food item added successfully.", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener{
                                Toast.makeText(this, "Food item uploading failed.", Toast.LENGTH_SHORT).show()
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
            foodImage=uri

        }
    }
}