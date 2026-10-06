package com.example.codishoes.model

data class Shoe(
    val name: String,
    val brand: String,
    val type: ShoeType,
    val priceRange: PriceRange,
    val color: OutfitColor,                 // ⭐ 필수
    val styles: Set<StylePreference>
)
