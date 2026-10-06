package com.example.codishoes

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codishoes.model.FavoriteManager
import com.example.codishoes.model.Shoe

class FavoritesActivity : AppCompatActivity() {

    private lateinit var txtEmpty: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        txtEmpty = findViewById(R.id.txtEmpty)
        recyclerView = findViewById(R.id.recyclerViewFavorites)

        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun loadFavorites() {
        val favoriteShoes: List<Shoe> =
            FavoriteManager.getFavoriteShoes(this)

        if (favoriteShoes.isEmpty()) {
            txtEmpty.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            txtEmpty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            recyclerView.adapter = ShoeAdapter(
                context = this,
                shoes = favoriteShoes.toMutableList(), // ⭐ 핵심 수정
            ) {
                // 즐겨찾기 화면에서는 고정 설명
                "즐겨찾기에 추가한 신발입니다. 다양한 코디에 활용해보세요."
            }
        }
    }
}
