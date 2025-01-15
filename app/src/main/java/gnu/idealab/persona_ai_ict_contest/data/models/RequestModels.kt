package gnu.idealab.persona_ai_ict_contest.data.models

import com.google.gson.annotations.SerializedName

// 데이터 모델
data class LoginRequest(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("user_id") val uid: String)

data class LoginResponse(
    @SerializedName("success") val success: Boolean
)

// 요청하는 변수 없음
// data class DepartmentRequest()
data class DepartmentResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("major_list") val departmentList: List<String>
)

data class UserDepartmentRequest(
    @SerializedName("user_id") val uid: String,
    @SerializedName("major_name") val department: String
)
data class UserDepartmentResponse(
    @SerializedName("success") val success: Boolean
)

data class AIChatAccessRequest(
    @SerializedName("user_id") val uid: String,
    @SerializedName("ai_type") val ai: String
)
data class AIChatAccessResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("chat_history") val chatHistory: List<ChatMessage>
)

data class ChatMessageRequest(
    @SerializedName("message") val message: ChatMessage
)
data class ChatMessageResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: ChatMessage
)


data class WavDataRequest(
    @SerializedName("uid") val uid: String,
    @SerializedName("wav_id") val wavId: String
)
data class WavDataResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("wav_data") val wavData: String
)

