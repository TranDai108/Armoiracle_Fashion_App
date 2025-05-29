package com.example.armoiraclefashionapp.models

data class FollowItem(
    val username: String, // Tên người dùng hoặc tiêu đề gợi ý
    val imageResId: Int,  // Hình ảnh gợi ý
    val description: String // Mô tả phong cách hoặc thông tin
)