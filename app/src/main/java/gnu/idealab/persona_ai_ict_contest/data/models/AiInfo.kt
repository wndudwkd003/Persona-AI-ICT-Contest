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
    @SerializedName("user_id") val id: String,
    @SerializedName("message") val message: String,
    @SerializedName("wav_data") val wavData: ByteArray,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("is_ai") val isAI: Boolean
):  Parcelable
