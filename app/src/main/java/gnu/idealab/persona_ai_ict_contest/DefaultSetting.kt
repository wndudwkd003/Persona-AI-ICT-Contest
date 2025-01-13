package gnu.idealab.persona_ai_ict_contest

import android.content.Context
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessage

object DefaultSetting {
    var debugMode = true        // true: 디버그 모드, false: 실제 서버 사용
    
    var debugDepartmentList = mutableListOf("컴퓨터공학과", "미래자동차공학과", "사회복지학과", "아동가족학과", "소프트웨어공학과", "물리학과", "영어학과", "전자공학과", "기계공학과", "바이오학과")

    fun getUID(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("UID", Context.MODE_PRIVATE)
        return sharedPreferences.getString("uid", "") ?: ""
    }

    val newMessage = ChatMessage("i123123123", "iu", "It's new message for debuging mode!", ByteArray(0), "2025-01-12 10:00:00", true)

    val chatHistory = mutableListOf<ChatMessage>(
        // 유저의 질문
        ChatMessage(
            uid = "user1",
            aiType = "iu",
            message = "Hi! What can you do?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:00:00",
            isAI = false
        ),
        // AI의 응답
        ChatMessage(
            uid = "iu",
            aiType = "iu",
            message = "Hello! I can provide information, assist with tasks, and much more. How can I assist you today?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:01:00",
            isAI = true
        ),
        // 유저의 추가 질문
        ChatMessage(
            uid = "user1",
            aiType = "iu",
            message = "Can you remind me about upcoming events?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:02:00",
            isAI = false
        ),
        // AI의 응답
        ChatMessage(
            uid = "iu",
            aiType = "iu",
            message = "Of course! Let me know the details of the events you'd like reminders for, and I'll handle it for you.",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:03:00",
            isAI = true
        ),
        // 유저의 추가 요청
        ChatMessage(
            uid = "user1",
            aiType = "iu",
            message = "Can you also provide weather updates?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:04:00",
            isAI = false
        ),
        // AI의 응답
        ChatMessage(
            uid = "iu",
            aiType = "iu",
            message = "Yes, I can provide real-time weather updates for your location. Would you like me to get the current forecast?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:05:00",
            isAI = true
        ),
        // 유저의 추가 요청
        ChatMessage(
            uid = "user1",
            aiType = "iu",
            message = "Can you also provide weather updates?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:04:00",
            isAI = false
        ),
        // AI의 응답
        ChatMessage(
            uid = "iu",
            aiType = "iu",
            message = "Yes, I can provide real-time weather updates for your location. Would you like me to get the current forecast?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:05:00",
            isAI = true
        ),
        // 유저의 추가 요청
        ChatMessage(
            uid = "user1",
            aiType = "iu",
            message = "Can you also provide weather updates?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:04:00",
            isAI = false
        ),
        // AI의 응답
        ChatMessage(
            uid = "iu",
            aiType = "iu",
            message = "Yes, I can provide real-time weather updates for your location. Would you like me to get the current forecast?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:05:00",
            isAI = true
        ),
        // 유저의 추가 요청
        ChatMessage(
            uid = "user1",
            aiType = "iu",
            message = "Can you also provide weather updates?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:04:00",
            isAI = false
        ),
        // AI의 응답
        ChatMessage(
            uid = "iu",
            aiType = "iu",
            message = "Yes, I can provide real-time weather updates for your location. Would you like me to get the current forecast?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:05:00",
            isAI = true
        ),
        // 유저의 추가 요청
        ChatMessage(
            uid = "user1",
            aiType = "iu",
            message = "Can you also provide weather updates?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:04:00",
            isAI = false
        ),
        // AI의 응답
        ChatMessage(
            uid = "iu",
            aiType = "iu",
            message = "Yes, I can provide real-time weather updates for your location. Would you like me to get the current forecast?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:05:00",
            isAI = true
        ),
        // 유저의 추가 요청
        ChatMessage(
            uid = "user1",
            aiType = "iu",
            message = "Can you also provide weather updates?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:04:00",
            isAI = false
        ),
        // AI의 응답
        ChatMessage(
            uid = "iu",
            aiType = "iu",
            message = "Yes, I can provide real-time weather updates for your location. Would you like me to get the current forecast?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:05:00",
            isAI = true
        ),
        // 유저의 추가 요청
        ChatMessage(
            uid = "user1",
            aiType = "iu",
            message = "Can you also provide weather updates?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:04:00",
            isAI = false
        ),
        // AI의 응답
        ChatMessage(
            uid = "iu",
            aiType = "iu",
            message = "Yes, I can provide real-time weather updates for your location. Would you like me to get the current forecast?",
            wavData = ByteArray(0),
            timestamp = "2025-01-12 10:05:00",
            isAI = true
        ),

    )


}