package gnu.idealab.persona_ai_ict_contest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gnu.idealab.persona_ai_ict_contest.DefaultSetting
import gnu.idealab.persona_ai_ict_contest.data.models.AiInfo
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessage
import gnu.idealab.persona_ai_ict_contest.data.repositories.ConnectRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

interface MessageInterface {
    fun successMessage(message: ChatMessage)
}

class ChatViewModel : ViewModel() {

    var initMessageListSize: Int = 0

    private val repos = ConnectRepository()

    fun setAiInfo(aiInfo: AiInfo) {
        _aiInfo.value = aiInfo
    }

    fun setMessageList(chatHistoryList: MutableList<ChatMessage>) {
        _chatHistoryList.value = chatHistoryList
    }

    fun sendMessage(uid: String, message: String, listener: MessageInterface) {
        val newMessage = ChatMessage(id = uid, message = message, wavData = ByteArray(0), isAI = false, timestamp = getCurrentTimestamp())
        _chatHistoryList.value?.apply {
            add(newMessage) // 유저의 메시지 등록
            _chatHistoryList.value = this // 강제 참조
        }

        // 유저의 메시지 보내기
        repos.sendChatMessage(uid, newMessage) { success, answerMessage ->
            if (DefaultSetting.debugMode) {
                _chatHistoryList.value?.apply {
                    add(DefaultSetting.newMessage) // 유저의 메시지 등록
                    _chatHistoryList.value = this // 강제 참조
                }
                listener.successMessage(answerMessage)
            } else {
                if (!success) {
                    Log.d("ChatViewModel", "Message sending failed")
                }
                _chatHistoryList.value?.apply {
                    add(answerMessage) // 유저의 메시지 등록
                    _chatHistoryList.value = this // 강제 참조
                }
                listener.successMessage(answerMessage)
            }
        }
    }

    private fun getCurrentTimestamp(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
    }

    private val _chatHistoryList = MutableLiveData<MutableList<ChatMessage>>()
    val chatHistoryList: MutableLiveData<MutableList<ChatMessage>> get() = _chatHistoryList

    private val _aiInfo = MutableLiveData<AiInfo>()
    val aiInfo: MutableLiveData<AiInfo> get() = _aiInfo
}