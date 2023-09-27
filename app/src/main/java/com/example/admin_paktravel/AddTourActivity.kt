package com.example.admin_paktravel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.admin_paktravel.databinding.ActivityAddTourBinding

class AddTourActivity : AppCompatActivity() {
    private val binding:ActivityAddTourBinding by lazy {
        ActivityAddTourBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.selectImage.setOnClickListener {
            pickimage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }
    val pickimage= registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri->
        if(uri!=null)
        {
            binding.selectedImage.setImageURI(uri)
        }
    }
}