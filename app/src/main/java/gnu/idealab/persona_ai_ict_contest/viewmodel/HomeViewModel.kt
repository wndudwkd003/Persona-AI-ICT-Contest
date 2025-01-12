package gnu.idealab.persona_ai_ict_contest.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gnu.idealab.persona_ai_ict_contest.DefaultSetting
import gnu.idealab.persona_ai_ict_contest.data.models.AIChatAccessRequest
import gnu.idealab.persona_ai_ict_contest.data.models.AiInfo
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessage
import gnu.idealab.persona_ai_ict_contest.data.repositories.ConnectRepository

class HomeViewModel : ViewModel() {
    private val _connectSuccess = MutableLiveData<Boolean>()
    val connectSuccess: MutableLiveData<Boolean> get() = _connectSuccess

    private val _chatHistoryList = MutableLiveData<List<ChatMessage>>()
    val chatHistoryList: MutableLiveData<List<ChatMessage>> get() = _chatHistoryList

    private val _aiInfo = MutableLiveData<AiInfo>()
    val aiInfo: MutableLiveData<AiInfo> get() = _aiInfo

    private val repos = ConnectRepository()

    fun accessChatMessage(uid: String, item: AiInfo) {
        _aiInfo.value = item
        repos.accessChatMessage(uid, item.id) { success, chatHistoryList ->
            if (DefaultSetting.debugMode) {
                _connectSuccess.value = true
                _chatHistoryList.value = DefaultSetting.chatHistory
            } else {
                if (!success) {
                    Log.d("HomeViewModel", "Failed to fetch chat list")
                }
                _connectSuccess.value = success
                _chatHistoryList.value = chatHistoryList
            }
        }
    }
}