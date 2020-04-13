package br.com.rafaellfx.ppets.ui.listpets

import android.util.Log
import androidx.lifecycle.ViewModel

class ListPetsViewModel : ViewModel() {

    init {
        Log.e("teste", "init viewModel")
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("teste", "onCleared")
    }
}
