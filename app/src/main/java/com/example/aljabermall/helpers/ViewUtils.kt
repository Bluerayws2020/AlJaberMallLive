package com.example.aljabermall.helpers

import android.view.View
import android.widget.EditText

object ViewUtils {

    fun View.hide() {
        visibility = View.GONE
    }

    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.inVisible() {
        visibility = View.INVISIBLE
    }

    fun EditText.isInputEmpty(): Boolean {
        return text.toString().trim().isEmpty()
    }
}