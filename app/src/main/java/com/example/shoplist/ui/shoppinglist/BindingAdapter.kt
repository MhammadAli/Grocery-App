package com.example.shoplist.ui.shoppinglist

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.adapter.ShoppingItemAdapter
import com.example.shoplist.data.db.entities.ShoppingItem

@BindingAdapter("listData")
fun RecyclerView.bind(data: List<ShoppingItem>?) {
    if (data != null){
        val adapter = this.adapter as ShoppingItemAdapter
        adapter.submitList(data)
    }

}
