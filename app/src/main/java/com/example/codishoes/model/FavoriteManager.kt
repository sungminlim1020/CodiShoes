package com.example.codishoes.model

import android.content.Context
import com.google.gson.Gson

/**
 * 신발 찜(즐겨찾기) 관리 헬퍼
 * - SharedPreferences에 Shoe 객체를 JSON 문자열 Set으로 저장
 * - 브랜드 + 이름이 같으면 같은 신발로 취급
 */
object FavoriteManager {

    private const val PREF_NAME = "codi_prefs"
    private const val KEY_FAVORITES = "favorite_shoes_json"

    private val gson = Gson()

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /** 현재 신발이 찜 상태인지 여부 */
    fun isFavorite(context: Context, shoe: Shoe): Boolean {
        val set = prefs(context).getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
        return set.any { json ->
            try {
                val s: Shoe = gson.fromJson(json, Shoe::class.java)
                s.brand == shoe.brand && s.name == shoe.name
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * 찜 상태 토글.
     * - true  : 방금 "찜 추가"
     * - false : 방금 "찜 해제"
     */
    fun toggleFavorite(context: Context, shoe: Shoe): Boolean {
        val pref = prefs(context)
        val current = pref.getStringSet(KEY_FAVORITES, emptySet())?.toMutableSet()
            ?: mutableSetOf()

        // 같은 신발(브랜드+이름)이 이미 있으면 제거 = 찜 해제
        val iterator = current.iterator()
        var found = false
        while (iterator.hasNext()) {
            val json = iterator.next()
            try {
                val s: Shoe = gson.fromJson(json, Shoe::class.java)
                if (s.brand == shoe.brand && s.name == shoe.name) {
                    iterator.remove()
                    found = true
                    break
                }
            } catch (e: Exception) {
                // 파싱 실패는 무시
            }
        }

        val nowFavorite: Boolean
        if (found) {
            nowFavorite = false
        } else {
            current.add(gson.toJson(shoe))
            nowFavorite = true
        }

        pref.edit().putStringSet(KEY_FAVORITES, current).apply()
        return nowFavorite
    }

    /** 찜한 신발 전체 목록 가져오기 */
    fun getFavoriteShoes(context: Context): List<Shoe> {
        val set = prefs(context).getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
        val result = mutableListOf<Shoe>()
        for (json in set) {
            try {
                val shoe: Shoe = gson.fromJson(json, Shoe::class.java)
                result.add(shoe)
            } catch (_: Exception) {
                // 파싱 실패는 무시
            }
        }
        return result
    }
}
