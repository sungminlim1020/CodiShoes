package com.example.codishoes.model

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * 코디 정보(모자/가방/상의/바지 색 + 스타일)를
 * TFLite 모델에 넣어서 "신발 타입 인덱스"를 예측하는 클래스
 */
class AiShoeModel(context: Context) {

    companion object {
        private const val MODEL_FILE = "codishoes_model.tflite"

        // 입력 벡터 길이
        //  모자 12 + 가방 12 + 상의 12 + 바지 12 + 스타일 4 = 52
        private const val INPUT_SIZE = 52

        // Colab에서 학습시킨 "신발 타입" 클래스 개수에 맞게 수정
        private const val NUM_CLASSES = 6   // 예: 6종류 신발이면 6
    }

    private val interpreter: Interpreter

    // -------------------- 인코딩용 맵 --------------------

    // 색상 → 인덱스 (Colab에서 학습할 때 사용한 순서와 반드시 동일해야 함)
    private val colorIndex: Map<OutfitColor, Int> = mapOf(
        OutfitColor.BLACK  to 0,
        OutfitColor.WHITE  to 1,
        OutfitColor.GRAY   to 2,
        OutfitColor.NAVY   to 3,
        OutfitColor.BLUE   to 4,
        OutfitColor.RED    to 5,
        OutfitColor.PINK   to 6,
        OutfitColor.GREEN  to 7,
        OutfitColor.BROWN  to 8,
        OutfitColor.KHAKI  to 9,
        OutfitColor.PURPLE to 10,
        OutfitColor.YELLOW to 11
    )

    // 스타일 → 인덱스
    private val styleIndex: Map<StylePreference, Int> = mapOf(
        StylePreference.CASUAL to 0,
        StylePreference.STREET to 1,
        StylePreference.MINIMAL to 2,
        StylePreference.FORMAL to 3
    )

    // -------------------- 초기화 (모델 로딩) --------------------

    init {
        // assets/ml/codishoes_model.tflite 에 있는 모델 불러오기
        val afd = context.assets.openFd(MODEL_FILE)
        val inputStream = afd.createInputStream()
        val modelBytes = ByteArray(afd.length.toInt())
        inputStream.read(modelBytes)
        inputStream.close()

        val buffer = ByteBuffer.allocateDirect(modelBytes.size)
        buffer.order(ByteOrder.nativeOrder())
        buffer.put(modelBytes)
        buffer.rewind()

        interpreter = Interpreter(buffer)
    }

    // -------------------- 예측 함수 --------------------

    /**
     * @return 예측된 신발 타입 인덱스 (0 ~ NUM_CLASSES-1)
     */
    fun predictType(
        hatColor: OutfitColor?,      // 모자 (없으면 null)
        bagColor: OutfitColor?,      // 가방 (없으면 null)
        topColor: OutfitColor,       // 상의
        bottomColor: OutfitColor,    // 바지
        style: StylePreference       // 스타일
    ): Int {

        // 입력 벡터 (1 x 52)
        val input = FloatArray(INPUT_SIZE) { 0f }

        // 한 파트(모자/가방/상의/바지)에 대한 원-핫 인코딩
        fun encodeColor(color: OutfitColor?, offset: Int) {
            val idx = color?.let { colorIndex[it] } ?: return   // 색상이 없으면 그냥 패스
            input[offset + idx] = 1f
        }

        // 스타일 원-핫 인코딩
        fun encodeStyle(style: StylePreference, offset: Int) {
            val idx = styleIndex[style] ?: return
            input[offset + idx] = 1f
        }

        // 순서: 모자 / 가방 / 상의 / 바지 / 스타일
        encodeColor(hatColor,   0)   // 0 ~ 11
        encodeColor(bagColor,   12)  // 12 ~ 23
        encodeColor(topColor,   24)  // 24 ~ 35
        encodeColor(bottomColor,36)  // 36 ~ 47
        encodeStyle(style,      48)  // 48 ~ 51

        // TFLite 입력/출력 배열 준비
        val inputBatch = arrayOf(input)                   // [1, 52]
        val output = Array(1) { FloatArray(NUM_CLASSES) } // [1, num_classes]

        // 추론 실행
        interpreter.run(inputBatch, output)

        val probs = output[0]

        // 가장 점수가 높은 인덱스 찾기
        var bestIdx = 0
        var bestScore = probs[0]
        for (i in 1 until NUM_CLASSES) {
            if (probs[i] > bestScore) {
                bestScore = probs[i]
                bestIdx = i
            }
        }

        return bestIdx
    }

    fun close() {
        interpreter.close()
    }
}
