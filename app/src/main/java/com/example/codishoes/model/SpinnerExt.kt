package com.example.codishoes

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner

fun Spinner.setOnItemSelectedListenerSimple(onSelect: () -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>, view: View?, position: Int, id: Long
        ) {
            onSelect()
        }
        override fun onNothingSelected(parent: AdapterView<*>) {}
    }
}
