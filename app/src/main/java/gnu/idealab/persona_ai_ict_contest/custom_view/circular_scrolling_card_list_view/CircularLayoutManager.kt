package gnu.idealab.persona_ai_ict_contest.custom_view.circular_scrolling_card_list_view

import android.graphics.PointF
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.cos
import kotlin.math.sin

class CircularLayoutManager : RecyclerView.LayoutManager(), RecyclerView.SmoothScroller.ScrollVectorProvider {

    private var radius = 500f // 원의 반지름
    private var scrollOffset = 0f // 스크롤 위치 오프셋

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)

        val itemCount = itemCount
        if (itemCount == 0) return

        val centerX = width / 2f
        val centerY = height / 2f

        for (i in 0 until itemCount) {
            val view = recycler.getViewForPosition(i)
            addView(view)

            // 아이템 각도 계산
            val angleMultiplier = 2f // 각도 배율
            val angle = (scrollOffset / radius + i * (360f / itemCount) * angleMultiplier) % 360f
            val theta = Math.toRadians(angle.toDouble())

            // 아이템 위치 계산
            val x = centerX + radius * cos(theta) - view.measuredWidth / 2
            val y = centerY + radius * sin(theta) - view.measuredHeight / 2

            measureChildWithMargins(view, 0, 0)
            layoutDecoratedWithMargins(
                view,
                x.toInt(),
                y.toInt(),
                (x + view.measuredWidth).toInt(),
                (y + view.measuredHeight).toInt()
            )
        }
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val speedMultiplier = 2 // 스크롤 속도 배율
        val adjustedDx = dx * speedMultiplier

        // 스크롤 오프셋 범위 제한
        scrollOffset = (scrollOffset - adjustedDx).coerceIn((-radius.toInt() * itemCount).toFloat(),
            (radius.toInt() * itemCount).toFloat()
        )

        onLayoutChildren(recycler, state) // 레이아웃 다시 계산
        return adjustedDx
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        return PointF(1f, 0f) // 스크롤 방향 벡터
    }
}
