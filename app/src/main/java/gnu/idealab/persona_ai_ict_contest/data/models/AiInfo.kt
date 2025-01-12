package gnu.idealab.persona_ai_ict_contest.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AiInfo(
    val id: String,
    val imgPath: String,
    val name: String,
    val description: String
): Parcelable


data class ChatMessage(
    val id: String,
    val message: String,
    val wavData: ByteArray,
    val timestamp: String,
    val isAI: Boolean
)
