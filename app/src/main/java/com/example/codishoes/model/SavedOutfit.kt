package com.example.codishoes.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 한 번 추천 받은 코디를 저장해 두는 데이터
 *
 * - hatColor, bagColor, topColor, bottomColor, style 은
 *   화면에 보여주는 한글 그대로 (예: "블랙", "착용 안 함", "스트릿") 를 넣는 걸 추천.
 * - shoeNames 에는 추천된 신발 이름 리스트를 그대로 저장.
 */
data class SavedOutfit(

    // 고유 ID (리스트 구분용)
    val id: Long = System.currentTimeMillis(),

    // 액세서리 색 (null = 착용 안 함)
    val hatColor: String? = null,
    val bagColor: String? = null,

    // 의상 정보
    val topColor: String,             // 예: "블랙"
    val bottomColor: String,          // 예: "화이트"
    val style: String,                // 예: "스트릿"

    // 추천된 신발 이름들
    val shoeNames: List<String> = emptyList(),

    // 코디 즐겨찾기 여부
    var isFavorite: Boolean = false,

    // 저장 시각 (millis)
    val savedAtMillis: Long = System.currentTimeMillis()
) {

    /**
     * 📌 리스트에서 보여줄 코디 요약 문장
     * 예) "블랙 상의 · 화이트 바지 · 스트릿 무드 / 모자: 착용 안 함 · 가방: 블루"
     */
    fun buildSummary(): String {
        val hat = hatColor ?: "착용 안 함"
        val bag = bagColor ?: "착용 안 함"

        return "${topColor} 상의 · ${bottomColor} 바지 · ${style} 무드 / 모자: $hat · 가방: $bag"
    }

    /**
     * 📅 저장 날짜 포맷 (SavedOutfitAdapter에서 사용)
     * 예) 2025-12-16 21:35
     */
    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(savedAtMillis))
    }
}
