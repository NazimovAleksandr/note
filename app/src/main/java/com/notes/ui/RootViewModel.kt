package com.notes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RootViewModel : ViewModel() {
    val isUpdateList: LiveData<Boolean?> by lazy { MutableLiveData() }

    fun updateList() {
        (isUpdateList as MutableLiveData).value = true
    }

    fun listUpdated() {
        (isUpdateList as MutableLiveData).value = false
    }
}