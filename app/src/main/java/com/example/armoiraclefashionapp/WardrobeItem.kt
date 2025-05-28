package com.example.armoiraclefashionapp.model

import android.net.Uri

sealed class WardrobeItem {
    data class FromUri(
        val title: String,
        val imageUri: Uri,
        val label: String = ""
    ) : WardrobeItem()

    data class FromRes(
        val title: String,
        val imageResId: Int,
        val label: String = ""
    ) : WardrobeItem()
}
