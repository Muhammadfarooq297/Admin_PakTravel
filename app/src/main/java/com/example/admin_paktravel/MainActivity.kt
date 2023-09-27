package com.example.admin_paktravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.admin_paktravel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.addtourButton.setOnClickListener {
            val intent= Intent(this,AddTourActivity::class.java)
            startActivity(intent)
        }
        binding.logoutButton.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        binding.addvehicleButton.setOnClickListener {
            val intent= Intent(this,AddVehicleActivity::class.java)
            startActivity(intent)
        }
        binding.createuserButton.setOnClickListener {
            val intent= Intent(this,CreateUserActivity::class.java)
            startActivity(intent)
        }

        binding.addfoodButton.setOnClickListener {
            val intent= Intent(this,AddFoodItemActivity::class.java)
            startActivity(intent)
        }

        binding.addroomButton.setOnClickListener {
            val intent= Intent(this,AddRoomActivity::class.java)
            startActivity(intent)
        }

    }
}