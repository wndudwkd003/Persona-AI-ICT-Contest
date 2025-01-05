package gnu.idealab.persona_ai_ict_contest.custom_view.speech_bubble_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class SpeechBubbleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth =
            18f // 테두리 두께
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE // 내부 색상
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 15f // 흰색 선 두께
        color = Color.WHITE // 흰색 선 색상
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val bubblePath = Path()
        val cornerRadius =70f
        val tailWidth = 120f
        val tailHeight = 80f
        val tailOffset = 0f // 꼬리를 위로 이동시키는 값
        val strokePadding = 12f

        // 그라데이션 설정
        gradientPaint.shader = LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            Color.BLUE, Color.MAGENTA,
            Shader.TileMode.CLAMP
        )

        // 말풍선 본체 (테두리 포함)
        val rect = RectF(
            strokePadding,
            strokePadding,
            width - strokePadding,
            height - tailHeight - strokePadding + tailOffset
        )
        bubblePath.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)

        // 내부 채우기
        canvas.drawPath(bubblePath, fillPaint)

        // 삼각형 꼬리 경로 설정
        val centerX = width / 2f
        val trianglePath = Path()
        val triangleLeft = centerX - tailWidth / 2
        val triangleRight = centerX + tailWidth / 2
        val triangleBottom = height - strokePadding
        val triangleTop = height - tailHeight - strokePadding

        trianglePath.moveTo(triangleLeft+5, triangleTop-15)
        trianglePath.lineTo(centerX, triangleBottom-15)
        trianglePath.lineTo(triangleRight-5, triangleTop-15)
        trianglePath.close()



        // 본체 테두리 그리기
        canvas.drawPath(bubblePath, gradientPaint)

        // 꼬리 테두리 그리기 (윗부분 제외)
        val tailOutlinePath = Path()
        tailOutlinePath.moveTo(triangleLeft+5, triangleTop)
        tailOutlinePath.lineTo(centerX, triangleBottom-2)
        tailOutlinePath.lineTo(triangleRight-5, triangleTop)
        tailOutlinePath.close()

        // 꼬리 테두리만 그리기
        val clipPath = Path()
        clipPath.addRect(
            triangleLeft,
            triangleTop,
            triangleRight,
            triangleBottom,
            Path.Direction.CW
        )

        tailOutlinePath.op(clipPath, Path.Op.INTERSECT)
        canvas.drawPath(tailOutlinePath, gradientPaint)
        // 삼각형 내부 채우기
        canvas.drawPath(trianglePath, fillPaint)

    }

}
