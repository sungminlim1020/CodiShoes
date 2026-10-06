package com.example.codishoes.model

import android.content.Context

/**
 * ⚠️ Deprecated wrapper
 *
 * 실제 저장은 SavedOutfitStorage 에서 처리함
 * (기존 코드 호환용으로만 유지)
 */
object SavedOutfitManager {

    /** 새 코디 저장 */
    fun save(context: Context, outfit: SavedOutfit) {
        SavedOutfitStorage.add(context, outfit)
    }

    /** 모든 코디 가져오기 */
    fun getAll(context: Context): MutableList<SavedOutfit> {
        return SavedOutfitStorage.getAll(context).toMutableList()
    }

    /** 코디 찜 토글 */
    fun toggleFavorite(context: Context, outfit: SavedOutfit) {
        val updated = outfit.copy(isFavorite = !outfit.isFavorite)
        SavedOutfitStorage.update(context, updated)
    }

    /** 코디 삭제 */
    fun delete(context: Context, outfit: SavedOutfit) {
        SavedOutfitStorage.remove(context, outfit.id)
    }
}
