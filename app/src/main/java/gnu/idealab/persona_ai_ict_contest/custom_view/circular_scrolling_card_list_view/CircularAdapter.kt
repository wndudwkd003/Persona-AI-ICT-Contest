package gnu.idealab.persona_ai_ict_contest.custom_view.circular_scrolling_card_list_view

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import gnu.idealab.persona_ai_ict_contest.R
import gnu.idealab.persona_ai_ict_contest.data.models.AiInfo
import java.io.InputStream

class CircularAdapter(
    private val context: Context,
    private val items: List<AiInfo>
) : RecyclerView.Adapter<CircularAdapter.CircularViewHolder>() {

    private val assetManager = context.assets

    class CircularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val profileImageView: ImageView = itemView.findViewById(R.id.profile_image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.circular_card_item, parent, false)
        return CircularViewHolder(view)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE


    override fun onBindViewHolder(holder: CircularViewHolder, position: Int) {
        if (items.isEmpty()) return // 데이터가 없으면 바인딩 중단

        val actualPosition = position % items.size // 현재 위치를 리스트 크기로 나눈 나머지
        val item = items[actualPosition]

        // AssetManager에서 이미지 스트림 로드
        try {
            val inputStream = assetManager.open(item.imgPath)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            holder.profileImageView.setImageBitmap(bitmap)
            inputStream.close()

        } catch (e: Exception) {
            e.printStackTrace()
            // 기본 이미지 설정
            holder.profileImageView.setImageResource(R.drawable.sample_image)
        }
    }
}
