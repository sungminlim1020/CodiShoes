package com.example.codishoes.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * SavedOutfit 를 SharedPreferences 에 저장/조회하는 단일 저장소
 * 👉 ResultActivity / SavedOutfitsActivity 모두 이것만 사용해야 함
 */
object SavedOutfitStorage {

    private const val PREF_NAME = "saved_outfits_pref"
    private const val KEY_OUTFITS = "saved_outfits"

    private val gson = Gson()

    // ================= 내부 로드 =================
    private fun load(context: Context): MutableList<SavedOutfit> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_OUTFITS, null) ?: return mutableListOf()

        return try {
            val type = object : TypeToken<List<SavedOutfit>>() {}.type
            val list: List<SavedOutfit> = gson.fromJson(json, type) ?: emptyList()
            list.toMutableList()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    // ================= 내부 저장 =================
    private fun save(context: Context, list: List<SavedOutfit>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = gson.toJson(list)
        prefs.edit()
            .putString(KEY_OUTFITS, json)
            .apply()
    }

    // ================= 외부 API =================

    /** 모든 저장된 코디 가져오기 (최신순) */
    fun getAll(context: Context): List<SavedOutfit> {
        return load(context)
            .sortedByDescending { it.savedAtMillis }
    }

    /** 새 코디 추가 */
    fun add(context: Context, outfit: SavedOutfit) {
        val list = load(context)
        list.add(0, outfit) // ⭐ 최신 코디를 맨 위에
        save(context, list)
    }

    /** 코디 업데이트 (찜 토글 등) */
    fun update(context: Context, updated: SavedOutfit) {
        val list = load(context)
        val index = list.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            list[index] = updated
            save(context, list)
        }
    }

    /** 코디 삭제 (선택 기능) */
    fun remove(context: Context, id: Long) {
        val list = load(context).filterNot { it.id == id }
        save(context, list)
    }
}
