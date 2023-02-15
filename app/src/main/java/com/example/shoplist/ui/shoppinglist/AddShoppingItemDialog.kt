package com.example.shoplist.ui.shoppinglist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.shoplist.data.db.entities.ShoppingItem
import com.example.shoplist.databinding.DialogAddShoppingItemBinding

class AddShoppingItemDialog(context: Context,var addDialogListener: AddDialogListener) : AppCompatDialog(context) {
    lateinit var binding: DialogAddShoppingItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddShoppingItemBinding.inflate(LayoutInflater.from(context), null, false)
        setContentView(binding.root)
        binding.tvAdd.setOnClickListener {
            val name = binding.etName.text.toString()
            val amount = binding.etAmount.text.toString()

            if (name.isEmpty() || amount.isEmpty()) {
                Toast.makeText(context, "Please enter all the information", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            val item = ShoppingItem(name = name, amount = amount)
            addDialogListener.onAddButtonClicked(item)
            dismiss() // There is no onDestroy in Dialog, so we simulate it from dismiss()
        }

        binding.tvCancel.setOnClickListener {
            cancel()
        }



    }
}