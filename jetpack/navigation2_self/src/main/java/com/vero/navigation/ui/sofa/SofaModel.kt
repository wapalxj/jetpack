package com.vero.navigation.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SofaModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is sssssssss Fragment"
    }
    val text: LiveData<String> = _text
}