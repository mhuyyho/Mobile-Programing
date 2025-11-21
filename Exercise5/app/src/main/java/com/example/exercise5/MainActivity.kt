package com.example.exercise5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv = findViewById(R.id.rvCategories)

        // horizontal layout manager
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv.layoutManager = layoutManager

        // snapping (snap to center/page)
        val snapHelper = PagerSnapHelper() // or LinearSnapHelper() for free snapping
        snapHelper.attachToRecyclerView(rv)

        // spacing between items
        val spacing = resources.getDimensionPixelSize(R.dimen.category_item_spacing).takeIf { it > 0 } ?: 24
        rv.addItemDecoration(HorizontalSpaceDecoration(spacing))

        // placeholder adapter until data loaded
        adapter = CategoryAdapter(mutableListOf(), { item, pos ->
            // onClick -> open detail
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("id", item.id)
                putExtra("name", item.name)
                putExtra("image", item.images)
                putExtra("description", item.description)
            }
            startActivity(intent)
        }, { item, pos ->
            // onLongClick -> show toast (you can change to selection)
            Toast.makeText(this, "Long pressed: ${item.name}", Toast.LENGTH_SHORT).show()
        })

        rv.adapter = adapter

        // swipe to delete
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val removed = adapter.getItem(pos)
                adapter.removeAt(pos)
                Toast.makeText(this@MainActivity, "Removed: ${removed?.name}", Toast.LENGTH_SHORT).show()
            }

            // Optional: customize background while swiping (left as default)
        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(rv)

        // load data
        loadCategories()
    }

    private fun loadCategories() {
        // Use raw JSON handling to be resilient if API returns array/object
        RetrofitClient.apiService.getCategoriesRaw().enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (!response.isSuccessful || response.body() == null) {
                    Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    return
                }
                try {
                    val body = response.body()!!
                    val listType = object : TypeToken<List<Category>>() {}.type
                    val gson = Gson()
                    val categories: List<Category> = when {
                        body.isJsonArray -> gson.fromJson(body.asJsonArray, listType)
                        body.isJsonObject -> {
                            val obj = body.asJsonObject
                            if (obj.has("categories") && obj.get("categories").isJsonArray) {
                                gson.fromJson(obj.getAsJsonArray("categories"), listType)
                            } else emptyList()
                        }
                        else -> emptyList()
                    }

                    // update adapter data
                    adapter = CategoryAdapter(categories.toMutableList(), { item, pos ->
                        val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                            putExtra("id", item.id)
                            putExtra("name", item.name)
                            putExtra("image", item.images)
                            putExtra("description", item.description)
                        }
                        startActivity(intent)
                    }, { item, pos ->
                        Toast.makeText(this@MainActivity, "Long pressed: ${item.name}", Toast.LENGTH_SHORT).show()
                    })
                    rv.adapter = adapter

                } catch (e: Exception) {
                    Log.e("API", "Parse error", e)
                    Toast.makeText(this@MainActivity, "Parse error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e("API", "onFailure", t)
                Toast.makeText(this@MainActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // simple item decoration for horizontal spacing
    class HorizontalSpaceDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: android.graphics.Rect, view: android.view.View, parent: RecyclerView, state: RecyclerView.State) {
            val pos = parent.getChildAdapterPosition(view)
            if (pos == 0) {
                outRect.left = space
            }
            outRect.right = space
        }
    }
}