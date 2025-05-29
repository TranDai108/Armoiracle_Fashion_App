package com.example.armoiraclefashionapp.models

data class UserCreate(
    val name: String?,
    val username: String?,
    val password: String?,
    val email: String?,
    val phone: String?,
    val gender: Int?,
    val avatar: String?,
    val birthday: String?,
    val weight: Int?,
    val height: Int?,
)
