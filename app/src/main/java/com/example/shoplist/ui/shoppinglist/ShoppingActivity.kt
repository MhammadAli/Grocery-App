package com.example.shoplist.ui.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoplist.R
import com.example.shoplist.adapter.ShoppingItemAdapter
import com.example.shoplist.data.db.ShoppingDatabase
import com.example.shoplist.data.db.entities.ShoppingItem
import com.example.shoplist.data.repositories.ShoppingRepository
import com.example.shoplist.databinding.ActivityShoppingBinding
import org.kodein.di.android.kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ShoppingActivity : AppCompatActivity(),KodeinAware {

    override val kodein by kodein()
    private val factory:ShoppingViewModelFactory by instance()


    lateinit var viewModel: ShoppingViewModel
    lateinit var binding: ActivityShoppingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shopping)
        viewModel = ViewModelProvider(this, factory)[ShoppingViewModel::class.java]

        val adapter = ShoppingItemAdapter(
            increaseCallback = { increaseAmount(it) },
            decreaseCallback = ::decreaseAmount, // this equal to {decreaseAmount(it)}
            deleteCallback = { deleteItem(it) }
        )
        binding.rvShoppingItems.layoutManager = LinearLayoutManager(this)
        binding.rvShoppingItems.adapter = adapter
        binding.data = viewModel
        binding.lifecycleOwner = this
        viewModel.getAllShoppingItems().observe(this, Observer {
            binding.data!!.items.value = it
            Log.i("ShoppingActivity", "onCreate: ${binding.data!!.items.value}")
        })

        binding.fab.setOnClickListener {
            AddShoppingItemDialog(this, object : AddDialogListener {
                override fun onAddButtonClicked(item: ShoppingItem) {
                    viewModel.upsert(item)
                }
            }).show()
        }

    }

    private fun increaseAmount(item: ShoppingItem) {
        val item2 = item.copy(amount = (item.amount.toInt() + 1).toString())

        viewModel.upsert(item2)
    }

    private fun decreaseAmount(item: ShoppingItem) {
        var item2: ShoppingItem = item
        if (item.amount.toInt() > 0) {
            item2 = item.copy(amount = (item.amount.toInt() - 1).toString())

        }
        viewModel.upsert(item2)
    }

    private fun deleteItem(item: ShoppingItem) = viewModel.delete(item)

}