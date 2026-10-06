package com.example.codishoes.model

object ShoeRepository {

    fun getAllShoes(): List<Shoe> {
        return listOf(

            Shoe(
                name = "나이키 에어포스 1",
                brand = "Nike",
                type = ShoeType.SNEAKERS,
                priceRange = PriceRange.MID,
                color = OutfitColor.WHITE,   // ⭐ 추가됨
                styles = setOf(
                    StylePreference.CASUAL,
                    StylePreference.STREET
                )
            ),

            Shoe(
                name = "아디다스 삼바",
                brand = "Adidas",
                type = ShoeType.SNEAKERS,
                priceRange = PriceRange.LOW,
                color = OutfitColor.BLACK,   // ⭐ 추가됨
                styles = setOf(
                    StylePreference.CASUAL,
                    StylePreference.MINIMAL
                )
            ),

            Shoe(
                name = "닥터마틴 1461",
                brand = "Dr.Martens",
                type = ShoeType.LOAFER,
                priceRange = PriceRange.HIGH,
                color = OutfitColor.BLACK,   // ⭐ 추가됨
                styles = setOf(
                    StylePreference.FORMAL,
                    StylePreference.MINIMAL
                )
            ),

            Shoe(
                name = "컨버스 척테일러",
                brand = "Converse",
                type = ShoeType.SNEAKERS,
                priceRange = PriceRange.LOW,
                color = OutfitColor.WHITE,   // ⭐ 추가됨
                styles = setOf(
                    StylePreference.CASUAL
                )
            )
        )
    }
}
