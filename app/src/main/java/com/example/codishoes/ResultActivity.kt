package com.example.codishoes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codishoes.model.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // ================= View =================
        val txtTitle = findViewById<TextView>(R.id.txtTitle)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewShoes)

        val btnSaveOutfit = findViewById<Button>(R.id.btnSaveOutfit)
        val btnViewSavedOutfits = findViewById<Button>(R.id.btnViewSavedOutfits)
        val btnViewFavorites = findViewById<Button>(R.id.btnViewFavorites)

        txtTitle.text = "오늘의 신발 추천"

        // ================= Intent 값 =================
        val style: StylePreference =
            intent.getStringExtra("style")
                ?.let { runCatching { StylePreference.valueOf(it) }.getOrNull() }
                ?: StylePreference.CASUAL

        val shoeType: ShoeType? =
            intent.getStringExtra("shoeType")
                ?.let { runCatching { ShoeType.valueOf(it) }.getOrNull() }

        val priceRange: PriceRange? =
            intent.getStringExtra("priceRange")
                ?.let { runCatching { PriceRange.valueOf(it) }.getOrNull() }

        // ================= 코디 정보 (저장용) =================
        val topColor = intent.getStringExtra("topColor") ?: "블랙"
        val bottomColor = intent.getStringExtra("bottomColor") ?: "화이트"

        // ================= 추천 =================
        val shoes: List<Shoe> = RecommendationEngine.recommend(
            style = style,
            shoeType = shoeType,
            priceRange = priceRange
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ShoeAdapter(
            context = this,
            shoes = shoes.toMutableList()
        ) { shoe ->
            RecommendationEngine.buildReason(
                shoe = shoe,
                style = style
            )
        }

        // ================= ⭐ 코디 저장하기 =================
        btnSaveOutfit.setOnClickListener {

            val now = System.currentTimeMillis()

            val savedOutfit = SavedOutfit(
                id = now,
                hatColor = null,
                bagColor = null,
                topColor = topColor,
                bottomColor = bottomColor,
                style = style.displayName,
                shoeNames = shoes.map { it.name },
                isFavorite = false,
                savedAtMillis = now
            )

            // ⭐ SharedPreferences 기반 저장소 (중요)
            SavedOutfitStorage.add(this, savedOutfit)

            Toast.makeText(
                this,
                "코디가 저장되었습니다 👟",
                Toast.LENGTH_SHORT
            ).show()
        }

        // ================= 저장한 코디 보기 =================
        btnViewSavedOutfits.setOnClickListener {
            startActivity(Intent(this, SavedOutfitsActivity::class.java))
        }

        // ================= 찜한 신발 보기 =================
        btnViewFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }
}
