package gnu.idealab.persona_ai_ict_contest.custom_view.circular_scrolling_card_list_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import gnu.idealab.persona_ai_ict_contest.R
import gnu.idealab.persona_ai_ict_contest.data.models.AiInfo

class CircularAdapter(
    private val context: Context,
    private val items: List<AiInfo>,
    private val listener: OnAiItemClickListener
) : RecyclerView.Adapter<CircularAdapter.CircularViewHolder>() {

    private val assetManager = context.assets

    class CircularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profile_image_view)
    }

    fun getItem(position: Int): AiInfo {
        return items[position % items.size]
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.circular_card_item, parent, false)
        return CircularViewHolder(view)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: CircularViewHolder, position: Int) {
        if (items.isEmpty()) return

        val actualPosition = position % items.size
        val item = items[actualPosition]

        // AssetManager에서 이미지 로드
        try {
            val inputStream = assetManager.open(item.imgPath)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            holder.profileImageView.setImageBitmap(bitmap)
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            holder.profileImageView.setImageResource(R.drawable.sample_image)
        }

        // 터치 리스너 설정
        setTouchListener(holder.itemView, item)
    }

    /**
     * 터치 리스너 설정
     */
    private fun setTouchListener(view: View, item: AiInfo) {
        view.setOnTouchListener(object : View.OnTouchListener {
            private var isTouchedInsideView = false

            override fun onTouch(v: View, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // 뷰 내에서 터치 시작
                        isTouchedInsideView = true
                        v.animate()
                            .scaleX(1.1f)
                            .scaleY(1.1f)
                            .setDuration(100)
                            .start()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        // 터치가 뷰 외부로 이동했는지 확인
                        val x = motionEvent.x
                        val y = motionEvent.y

                        if (x < 0 || x > v.width || y < 0 || y > v.height) {
                            isTouchedInsideView = false
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        // 손을 뗐을 때 터치가 뷰 안에서 끝났는지 확인
                        v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .withEndAction {
                                if (isTouchedInsideView) {
                                    listener.onAiItemClick(item, true) // 클릭 이벤트 전달
                                } else {
                                    listener.onAiItemClick(item, false) // 클릭 취소 이벤트 전달
                                }
                            }
                            .start()
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        // 터치가 취소된 경우
                        isTouchedInsideView = false
                        v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                    }
                }
                return true
            }
        })
    }
}

/**
 * 인터페이스 정의
 */
interface OnAiItemClickListener {
    fun onAiItemClick(item: AiInfo, isClicked: Boolean)
}
