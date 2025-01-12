package gnu.idealab.persona_ai_ict_contest.data.models

// 데이터 모델
data class LoginRequest(val nickname: String, val uid: String)
data class LoginResponse(val success: Boolean)


data class DepartmentRequest(val uid: String)
data class DepartmentResponse(val success: Boolean, val departmentList: List<String>)

data class UserDepartmentRequest(val uid: String, val department: String)
data class UserDepartmentResponse(val success: Boolean)

data class AIChatAccessRequest(val uid: String, val ai: String)
data class AIChatAccessResponse(val success: Boolean, val chatHistory: List<ChatMessage>)

data class ChatMessageRequest(val uid: String, val message: ChatMessage)
data class ChatMessageResponse(val success: Boolean, val message: ChatMessage)

