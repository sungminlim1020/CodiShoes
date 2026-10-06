package com.example.codishoes.model

object RecommendationEngine {

    fun recommend(
        style: StylePreference,
        shoeType: ShoeType?,
        priceRange: PriceRange?
    ): List<Shoe> {

        var shoes = ShoeRepository.getAllShoes()

        // ===== 타입 필터 =====
        if (shoeType != null) {
            shoes = shoes.filter { it.type == shoeType }
        }

        // ===== 가격대 필터 =====
        if (priceRange != null) {
            shoes = shoes.filter { it.priceRange == priceRange }
        }

        // ===== 점수 계산 후 정렬 =====
        val scored = shoes.map { shoe ->
            shoe to calcScore(shoe, style)
        }.sortedByDescending { it.second }

        return scored.take(3).map { it.first }
    }

    private fun calcScore(
        shoe: Shoe,
        style: StylePreference
    ): Int {
        var score = 0

        if (shoe.styles.contains(style)) {
            score += 10
        }

        when (shoe.priceRange) {
            PriceRange.LOW -> score += 1
            PriceRange.MID -> score += 2
            PriceRange.HIGH -> score += 3
        }

        return score
    }

    // ================= 추천 이유 (AI 스타일) =================
    fun buildReason(
        shoe: Shoe,
        style: StylePreference
    ): String {

        val reasons = mutableListOf<String>()

        // 스타일
        if (shoe.styles.contains(style)) {
            reasons += "선택한 ${style.displayName} 스타일과 자연스럽게 어울립니다."
        }

        // 가격대
        when (shoe.priceRange) {
            PriceRange.LOW ->
                reasons += "가성비 좋은 선택으로 데일리 코디에 부담이 없습니다."
            PriceRange.MID ->
                reasons += "가격과 디자인의 균형이 잘 맞는 아이템입니다."
            PriceRange.HIGH ->
                reasons += "프리미엄 라인으로 코디의 완성도를 높여줍니다."
        }

        // 타입 (🔥 enum과 정확히 일치)
        when (shoe.type) {
            ShoeType.SNEAKERS ->
                reasons += "캐주얼부터 스트릿까지 폭넓게 활용할 수 있습니다."
            ShoeType.SLIPPER ->
                reasons += "편안하면서도 트렌디한 무드를 연출할 수 있습니다."
            ShoeType.RUNNING ->
                reasons += "활동적인 스타일에 잘 어울리는 기능성 슈즈입니다."
            ShoeType.LOAFER ->
                reasons += "단정하고 깔끔한 인상을 주는 클래식한 선택입니다."
        }

        return reasons.joinToString(" ")
    }
}
