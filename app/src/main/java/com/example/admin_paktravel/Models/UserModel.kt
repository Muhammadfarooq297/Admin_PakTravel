package com.example.admin_paktravel.Models

import android.provider.ContactsContract.CommonDataKinds.Email

data class UserModel(
    val email:String?=null,
    val password:String?=null,
    val name:String?=null,
)
