package com.example.armoiraclefashionapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class QuestionResponse(
    val idques: String,
    val question: String
): Parcelable
