package gnu.idealab.persona_ai_ict_contest.data.models

// 데이터 모델
data class LoginRequest(val nickname: String, val uid: String)
data class LoginResponse(val success: Boolean)