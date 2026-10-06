package com.example.codishoes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codishoes.model.SavedOutfitStorage

class SavedOutfitListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_outfit_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSavedOutfits)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ 저장된 코디 불러오기
        val outfits = SavedOutfitStorage.getAll(this).toMutableList()

        val adapter = SavedOutfitAdapter(
            context = this,
            outfits = outfits
        ) { updated ->
            // ⭐ 즐겨찾기 상태 변경 시 저장
            SavedOutfitStorage.update(this, updated)
        }

        recyclerView.adapter = adapter
    }
}
