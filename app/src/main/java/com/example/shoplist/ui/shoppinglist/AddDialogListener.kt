package com.example.shoplist.ui.shoppinglist

import com.example.shoplist.data.db.entities.ShoppingItem

interface AddDialogListener {
    fun onAddButtonClicked(item: ShoppingItem)
}