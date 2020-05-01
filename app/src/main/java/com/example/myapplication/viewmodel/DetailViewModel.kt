package com.example.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.DogBreed

class DetailViewModel : ViewModel() {
    //Notifica mudanca na lista
    val dogs = MutableLiveData<DogBreed>()

    fun refresh() {
    }
}