package com.example.myapplication.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.DogBreed
import com.example.myapplication.model.DogDataBase
import com.example.myapplication.model.DogsApiService
import com.example.myapplication.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(application)
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    // Instance from API
    private val dogsServices = DogsApiService()

    // Responsavel por nos devolver o o observable da API.
    private val disposable = CompositeDisposable()


    //Notifica mudanca na lista
    val dogs = MutableLiveData<List<DogBreed>>()

    //Notifica erro e o tipo
    val dogsError = MutableLiveData<Boolean>()

    // Notifica quando temos que mostrar o spinner
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        val updateTime = prefHelper.getUpdateTime()

        Log.d("sm.borges", "time: $updateTime")
        if (updateTime != null && updateTime != 0L &&
            System.nanoTime() - updateTime < refreshTime
        ) {
            fetchFromDataBase()
        } else {
            fetchFromRemote()
        }

    }

    private fun fetchFromDataBase() {
        loading.value = true

        launch {
            val dogs = DogDataBase(getApplication()).dogDao().getAllDogs()
            dogRetrieved(dogs)

            Toast.makeText(getApplication(), "Retrieved from database", Toast.LENGTH_LONG).show()
        }
    }

    // Return the data from remote end point
    private fun fetchFromRemote() {
        disposable.add(
            dogsServices.getDogs()
                //Roda o processo na thread de background
                .subscribeOn(Schedulers.newThread())
                //Onde estamos observando
                .observeOn(AndroidSchedulers.mainThread())
                //Com qual observer estamos observando
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(dogList: List<DogBreed>) {
                        storeDogsLocally(dogList)
                    }

                    override fun onError(e: Throwable) {
                        dogsError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
        loading.value = true
    }

    private fun dogRetrieved(dogsList: List<DogBreed>) {
        dogs.value = dogsList
        dogsError.value = false
        loading.value = false
    }

    private fun storeDogsLocally(list: List<DogBreed>) {
        launch {
            val dao = DogDataBase(getApplication()).dogDao()
            dao.deleteAllDogs()
            //expand a list in individual elements
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0

            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                i++
            }

            dogRetrieved(list)
        }

        prefHelper.saveUpdateItems(System.nanoTime())

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}