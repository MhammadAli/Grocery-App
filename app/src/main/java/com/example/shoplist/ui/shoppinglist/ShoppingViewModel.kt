package com.example.shoplist.ui.shoppinglist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplist.data.db.entities.ShoppingItem
import com.example.shoplist.data.repositories.ShoppingRepository
import kotlinx.coroutines.launch

class ShoppingViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {
    val items = MutableLiveData<List<ShoppingItem>>()
    fun upsert(item: ShoppingItem) = viewModelScope.launch {
        repository.upsert(item)
    }

    fun delete(item: ShoppingItem) = viewModelScope.launch {
        repository.delete(item)
    }

    fun getAllShoppingItems() = repository.getAllShoppingItems()


}
