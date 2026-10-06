package com.example.codishoes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.codishoes.model.SavedOutfit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SavedOutfitAdapter(
    private val context: Context,
    private val outfits: MutableList<SavedOutfit>,
    private val onUpdate: (SavedOutfit) -> Unit
) : RecyclerView.Adapter<SavedOutfitAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtSummary: TextView = view.findViewById(R.id.txtSummary)
        val txtShoes: TextView = view.findViewById(R.id.txtShoes)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val btnFavorite: ImageButton = view.findViewById(R.id.btnFavorite)
    }

    private val dateFormat =
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_outfit, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = outfits.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val outfit = outfits[position]

        holder.txtSummary.text = outfit.buildSummary()
        holder.txtShoes.text =
            "추천 신발: ${outfit.shoeNames.joinToString(", ")}"
        holder.txtDate.text =
            dateFormat.format(Date(outfit.savedAtMillis))

        // ⭐ 찜 아이콘 상태
        holder.btnFavorite.setImageResource(
            if (outfit.isFavorite)
                android.R.drawable.btn_star_big_on
            else
                android.R.drawable.btn_star_big_off
        )

        holder.btnFavorite.setOnClickListener {
            outfit.isFavorite = !outfit.isFavorite
            onUpdate(outfit)
            notifyItemChanged(position)
        }
    }

    /** 리스트 갱신용 */
    fun updateItems(newItems: List<SavedOutfit>) {
        outfits.clear()
        outfits.addAll(newItems)
        notifyDataSetChanged()
    }
}
