package com.example.codishoes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.codishoes.model.FavoriteManager
import com.example.codishoes.model.Shoe
import java.net.URLEncoder

class ShoeAdapter(
    private val context: Context,
    private val shoes: MutableList<Shoe>,          // ⭐ 필터/갱신 대비 MutableList
    private val reasonBuilder: (Shoe) -> String    // 추천 이유 생성 람다
) : RecyclerView.Adapter<ShoeAdapter.ShoeViewHolder>() {

    // ================= ViewHolder =================
    inner class ShoeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtShoeName: TextView = itemView.findViewById(R.id.txtShoeName)
        val txtShoeBrand: TextView = itemView.findViewById(R.id.txtShoeBrand)
        val txtShoeDesc: TextView = itemView.findViewById(R.id.txtShoeDesc)
        val btnShop: Button = itemView.findViewById(R.id.btnShop)
        val btnFavorite: Button = itemView.findViewById(R.id.btnFavorite)
    }

    // ================= Adapter 기본 =================
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shoe, parent, false)
        return ShoeViewHolder(view)
    }

    override fun getItemCount(): Int = shoes.size

    override fun onBindViewHolder(holder: ShoeViewHolder, position: Int) {
        val shoe = shoes[position]

        // ---------- 기본 정보 ----------
        holder.txtShoeName.text = shoe.name
        holder.txtShoeBrand.text = shoe.brand
        holder.txtShoeDesc.text = reasonBuilder(shoe)

        // ---------- 쇼핑몰 가기 (네이버 쇼핑 검색) ----------
        holder.btnShop.setOnClickListener {
            try {
                val query = URLEncoder.encode(
                    "${shoe.brand} ${shoe.name}",
                    "UTF-8"
                )
                val url =
                    "https://www.nike.com/kr/w/air-force-1-shoes-5sj3yzy7ok?utm_source=naver&utm_medium=sa&utm_campaign=nike_fw_pc&utm_term=x_prod_lifestyleshoes_airforce&cp=91214191762_search_&n_media=27758&n_query=%EB%82%98%EC%9D%B4%ED%82%A4%EC%97%90%EC%96%B4%ED%8F%AC%EC%8A%A41&n_rank=1&n_ad_group=grp-a001-01-000000044291644&n_ad=nad-a001-01-000000400529074&n_keyword_id=nkw-a001-01-000006474341730&n_keyword=%EB%82%98%EC%9D%B4%ED%82%A4%EC%97%90%EC%96%B4%ED%8F%AC%EC%8A%A41&n_campaign_type=1&n_ad_group_type=1&n_match=1&NaPm=ct%3Dmj91fg0g%7Cci%3DER28472d56%2Ddabe%2D11f0%2D9df7%2D324a6f004291%7Ctr%3Dsa%7Chk%3D3f1b8c57bd4c76c56346bf2ac74f46a61aa83c0b%7Cnacn%3Drb3wBUw0Wml9\n"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "쇼핑몰 링크를 여는 중 오류가 발생했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // ---------- 찜 버튼 ----------
        updateFavoriteButton(holder, shoe)

        holder.btnFavorite.setOnClickListener {
            val isNowFavorite =
                FavoriteManager.toggleFavorite(context, shoe)

            updateFavoriteButton(holder, shoe)

            Toast.makeText(
                context,
                if (isNowFavorite) "찜 목록에 추가했어요."
                else "찜을 해제했어요.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // ================= 찜 버튼 UI =================
    private fun updateFavoriteButton(
        holder: ShoeViewHolder,
        shoe: Shoe
    ) {
        val isFav = FavoriteManager.isFavorite(context, shoe)
        holder.btnFavorite.text =
            if (isFav) "★ 찜됨" else "♡ 찜하기"
    }

    // ================= 필터 적용용 =================
    /**
     * ⭐ 신발 타입 / 가격대 필터 적용 시 호출
     */
    fun updateItems(newItems: List<Shoe>) {
        shoes.clear()
        shoes.addAll(newItems)
        notifyDataSetChanged()
    }
}
