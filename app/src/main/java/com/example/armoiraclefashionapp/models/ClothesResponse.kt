package com.example.armoiraclefashionapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClothesResponse(
    val idcloth: String,
    val name: String,
    val picture: String,
    val size: String,
    val color: String,
    val material: String,
    val brand: String,
    val weather: String,
    val type: String,
    val author: String
): Parcelable
