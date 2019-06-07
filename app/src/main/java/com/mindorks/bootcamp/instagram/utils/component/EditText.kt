package com.mindorks.bootcamp.instagram.utils.component

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.EditText
import com.mindorks.bootcamp.instagram.R

var tempWatcher: TextWatcher? = null

@SuppressLint("ClickableViewAccessibility")
private fun EditText.setRightDrawableClickListener(onClicked: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}

fun EditText.addClearDrawable() {
    val updateRightDrawable = {
        this.setCompoundDrawablesWithIntrinsicBounds(
            0, 0,
            if (this.text.isNotEmpty()) R.drawable.ic_cancel else 0, 0
        )
    }
    updateRightDrawable()
    val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            updateRightDrawable()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
    tempWatcher = watcher
    this.addTextChangedListener(tempWatcher)

    this.setRightDrawableClickListener {
        this.text.clear()
        updateRightDrawable()
        this.requestFocus()
    }
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.removeClearDrawable() {
    this.removeTextChangedListener(tempWatcher)
    this.setOnTouchListener(null)
}