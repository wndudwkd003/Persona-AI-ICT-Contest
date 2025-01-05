package gnu.idealab.persona_ai_ict_contest.custom_view.dash_line_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DashedLineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = 0xFF000000.toInt() // 검정색 (원하는 색상으로 변경 가능)
        strokeWidth = 5f // 선의 두께
        style = Paint.Style.STROKE // 선 스타일
        pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f) // 점선 패턴 (20픽셀 그리기, 10픽셀 비우기)
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // View의 가로 방향으로 점선 그리기
        canvas.drawLine(0f, height / 2f, width.toFloat(), height / 2f, paint)
    }
}
