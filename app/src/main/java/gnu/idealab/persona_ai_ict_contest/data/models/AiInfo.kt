package gnu.idealab.persona_ai_ict_contest.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AiInfo(
    val id: String,
    val imgPath: String,
    val name: String,
    val description: String
): Parcelable

@Parcelize
data class ChatMessage(
    @SerializedName("uid") val uid: String,
    @SerializedName("ai_type") val aiType: String,
    @SerializedName("message") val message: String,
    @SerializedName("wav_id") val wavId: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("is_ai") val isAI: Boolean
):  Parcelable

