package com.example.codishoes

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.codishoes.model.OutfitColor
import com.example.codishoes.model.StylePreference

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerHat: Spinner
    private lateinit var spinnerBag: Spinner
    private lateinit var spinnerTop: Spinner
    private lateinit var spinnerBottom: Spinner
    private lateinit var spinnerStyle: Spinner
    private lateinit var btnRecommend: Button

    // 색상 enum 순서와 res/values/strings.xml 의 color_options 순서를 맞춰둠
    private val colorOptions = listOf(
        OutfitColor.BLACK,
        OutfitColor.GRAY,
        OutfitColor.NAVY,
        OutfitColor.WHITE,
        OutfitColor.BLUE,
        OutfitColor.RED,
        OutfitColor.PINK,
        OutfitColor.GREEN,
        OutfitColor.BROWN,
        OutfitColor.KHAKI,
        OutfitColor.PURPLE,
        OutfitColor.YELLOW
    )

    // 스타일 옵션
    private val styleOptions = listOf(
        StylePreference.CASUAL,
        StylePreference.STREET,
        StylePreference.MINIMAL,
        StylePreference.FORMAL
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 뷰 연결
        spinnerHat = findViewById(R.id.spinnerHat)
        spinnerBag = findViewById(R.id.spinnerBag)
        spinnerTop = findViewById(R.id.spinnerTop)
        spinnerBottom = findViewById(R.id.spinnerBottom)
        spinnerStyle = findViewById(R.id.spinnerStyle)
        btnRecommend = findViewById(R.id.btnRecommend)

        // 스피너 세팅
        setupSpinners()

        // 버튼 클릭 시 결과 화면으로 이동
        btnRecommend.setOnClickListener {
            val hatColor = getOptionalColor(spinnerHat.selectedItemPosition)     // null 또는 색
            val bagColor = getOptionalColor(spinnerBag.selectedItemPosition)     // null 또는 색
            val topColor = colorOptions[spinnerTop.selectedItemPosition]
            val bottomColor = colorOptions[spinnerBottom.selectedItemPosition]
            val style = styleOptions[spinnerStyle.selectedItemPosition]

            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("hatColor", hatColor?.name ?: "NONE")
                putExtra("bagColor", bagColor?.name ?: "NONE")
                putExtra("topColor", topColor.name)
                putExtra("bottomColor", bottomColor.name)
                putExtra("style", style.name)
            }
            startActivity(intent)
        }
    }

    /**
     * 모자/가방용: 스피너 0번은 "착용 안 함" → null 반환
     */
    private fun getOptionalColor(position: Int): OutfitColor? {
        return if (position == 0) {
            null
        } else {
            colorOptions[position - 1]
        }
    }

    /**
     * 각 스피너에 어댑터 세팅
     */
    private fun setupSpinners() {
        // 기본 색 이름 리스트 (12개)
        val baseColorItems = resources.getStringArray(R.array.color_options).toList()

        // 모자/가방용: "착용 안 함" + 색 12개
        val hatBagItems = listOf("착용 안 함") + baseColorItems
        val hatBagAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            hatBagItems
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerHat.adapter = hatBagAdapter
        spinnerBag.adapter = hatBagAdapter

        // 상의/바지용: 색 12개만
        val clothAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.color_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerTop.adapter = clothAdapter
        spinnerBottom.adapter = clothAdapter

        // 스타일 스피너
        ArrayAdapter.createFromResource(
            this,
            R.array.style_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerStyle.adapter = adapter
        }
    }
}
