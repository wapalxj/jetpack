package com.vero.navigation.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MineModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is mine Fragment"
    }
    val text: LiveData<String> = _text
}