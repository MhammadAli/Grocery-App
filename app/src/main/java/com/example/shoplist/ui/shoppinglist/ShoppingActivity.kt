package com.example.shoplist.ui.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.example.shoplist.adapter.ShoppingItemAdapter
import com.example.shoplist.data.db.entities.ShoppingItem
import com.example.shoplist.databinding.ActivityShoppingBinding
import com.google.android.material.snackbar.Snackbar
import org.kodein.di.android.kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ShoppingActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: ShoppingViewModelFactory by instance()


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
        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val item = adapter.currentList[position]
                    viewModel.delete(item)
                    Snackbar.make(
                        binding.root,
                        "Item deleted successfully",
                        Snackbar.LENGTH_LONG
                    )
                        .apply {
                            setAction("Undo") {
                                viewModel.upsert(item)
                            }
                            show()
                        }
                }
            }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvShoppingItems)
        }

        binding.apply {
            rvShoppingItems.layoutManager = LinearLayoutManager(this@ShoppingActivity)
            rvShoppingItems.adapter = adapter
            data = viewModel
            lifecycleOwner = this@ShoppingActivity


            fab.setOnClickListener {
                AddShoppingItemDialog(
                    addItemClicked = ::addItem,
                    context = this@ShoppingActivity,
                ).show()
            }
        }

        viewModel.getAllShoppingItems().observe(this) {
            binding.data!!.items.value = it
            Log.i("ShoppingActivity", "onCreate: ${binding.data!!.items.value}")
        }

    }

    private fun addItem(item: ShoppingItem) {
        viewModel.upsert(item)

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