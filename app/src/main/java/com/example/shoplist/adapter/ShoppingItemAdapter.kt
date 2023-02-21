package com.example.shoplist.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.data.db.entities.ShoppingItem
import com.example.shoplist.databinding.ShoppingItemBinding

class ShoppingItemAdapter(
    private val increaseCallback: ((ShoppingItem) -> Unit),
    private val decreaseCallback: ((ShoppingItem) -> Unit),
    private val deleteCallback: (ShoppingItem) -> Unit
) :
    ListAdapter<ShoppingItem, ShoppingItemAdapter.ShoppingViewHolder>(DiffCallBack) {

    inner class ShoppingViewHolder(private val binding: ShoppingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShoppingItem) {

            binding.apply {
                ivDelete.setOnClickListener {
                    deleteCallback(item)
                }


                ivMinus.setOnClickListener {
                    decreaseCallback(item)
                }


                ivPlus.setOnClickListener {
                    increaseCallback(item)
                }

                data = item
                Log.i("ShoppingItemAdapter", "bind: $item")
                // This is important, because it forces the data binding to execute immediately,
                // which allows the RecyclerView to make the correct view size measurements
                executePendingBindings()
            }

        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        return ShoppingViewHolder(
            ShoppingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val shoppingItem = getItem(position)
        holder.bind(shoppingItem)
    }


}