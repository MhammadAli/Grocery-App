package com.example.shoplist.ui.shoppinglist

import android.view.View
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
    val state = MutableLiveData<Int>()
    fun upsert(item: ShoppingItem) = viewModelScope.launch {
        repository.upsert(item)
    }

    fun delete(item: ShoppingItem) = viewModelScope.launch {
        repository.delete(item)
    }

    fun getAllShoppingItems() = repository.getAllShoppingItems()

    fun stateCheck() {

        if (items.value?.isEmpty() == true) {
            state.value = View.VISIBLE
        } else {
            state.value = View.GONE
        }
    }

    fun increaseItemAmount(item: ShoppingItem): ShoppingItem =
        item.copy(amount = (item.amount.toInt() + 1).toString())


    fun decreaseItemAmount(item: ShoppingItem): ShoppingItem {
        if (item.amount.toInt() > 0) {
            return item.copy(amount = (item.amount.toInt() - 1).toString())
        }
        return item
    }


}
