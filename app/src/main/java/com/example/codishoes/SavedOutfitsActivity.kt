package com.example.codishoes

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codishoes.model.SavedOutfitStorage

class SavedOutfitsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var txtEmpty: TextView
    private lateinit var adapter: SavedOutfitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_outfits)

        // ✅ XML id와 정확히 일치
        recyclerView = findViewById(R.id.recyclerViewSavedOutfits)
        txtEmpty = findViewById(R.id.txtEmpty)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // 어댑터는 한 번만 생성
        adapter = SavedOutfitAdapter(
            context = this,
            outfits = mutableListOf()
        ) { updatedOutfit ->
            SavedOutfitStorage.update(this, updatedOutfit)
        }

        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadOutfits()
    }

    private fun loadOutfits() {
        val outfits = SavedOutfitStorage.getAll(this)

        if (outfits.isEmpty()) {
            txtEmpty.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            txtEmpty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            adapter.updateItems(outfits)
        }
    }
}
