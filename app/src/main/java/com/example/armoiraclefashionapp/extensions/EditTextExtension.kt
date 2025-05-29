package com.example.armoiraclefashionapp.extensions

import android.annotation.SuppressLint
import android.text.InputType
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.example.armoiraclefashionapp.R

@SuppressLint("ClickableViewAccessibility")
fun TextInputEditText.enablePasswordToggleWithIcon() {
    var isPasswordVisible = false

    setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ic_show_pass), null)

    this.setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            val drawableEnd = this.compoundDrawables[2]
            if (drawableEnd != null) {
                val drawableWidth = drawableEnd.bounds.width()
                if (event.x >= (this.width - this.paddingEnd - drawableWidth)) {
                    // Toggle password visibility
                    isPasswordVisible = !isPasswordVisible
                    this.inputType = if (isPasswordVisible) {
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    } else {
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                    this.setSelection(this.text?.length ?: 0)

                    // Cập nhật icon
                    val icon = if (isPasswordVisible)
                        R.drawable.ic_hide_pass
                    else
                        R.drawable.ic_show_pass
                    setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, icon), null)

                    v.performClick()
                    return@setOnTouchListener true
                }
            }
        }
        false
    }
}
