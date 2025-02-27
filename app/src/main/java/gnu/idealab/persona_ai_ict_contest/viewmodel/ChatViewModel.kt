package gnu.idealab.persona_ai_ict_contest.viewmodel

import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gnu.idealab.persona_ai_ict_contest.DefaultSetting
import gnu.idealab.persona_ai_ict_contest.data.models.AiInfo
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessage
import gnu.idealab.persona_ai_ict_contest.data.repositories.ConnectRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface MessageInterface {
    fun successMessage(message: ChatMessage)
}

class ChatViewModel : ViewModel() {

    var sampleRate: Int = 22050

    var initMessageListSize: Int = 0

    private val repos = ConnectRepository()

    private val _wavSuccess = MutableLiveData<Boolean>()
    val wavSuccess: MutableLiveData<Boolean> get() = _wavSuccess


    private val _wavAudioData = MutableLiveData<ByteArray>()
    val wavAudioData: MutableLiveData<ByteArray> get() = _wavAudioData


    fun setAiInfo(aiInfo: AiInfo) {
        _aiInfo.value = aiInfo
    }

    fun setMessageList(chatHistoryList: MutableList<ChatMessage>) {
        _chatHistoryList.value = chatHistoryList
    }

    fun sendMessage(uid: String, aiType: String, message: String, listener: MessageInterface) {
        val newMessage = ChatMessage(uid = uid, aiType = aiType, message = message, wavId = "", isAI = false, timestamp = getCurrentTimestamp())
        _chatHistoryList.value?.apply {
            add(newMessage) // 유저의 메시지 등록
            _chatHistoryList.value = this // 강제 참조
        }

        // 유저의 메시지 보내기
        repos.sendChatMessage(newMessage) { success, answerMessage ->
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

    private fun analyzeWavHeader(wavData: ByteArray) {
        if (wavData.size < 44) {
            Log.e("WAV Analysis", "Invalid WAV file: insufficient size")
            return
        }

        val riffHeader = String(wavData.copyOfRange(0, 4)) // "RIFF"
        val waveHeader = String(wavData.copyOfRange(8, 12)) // "WAVE"
        val sampleRate = wavData.copyOfRange(24, 28).toIntLE() // 샘플링 레이트
        val numChannels = wavData.copyOfRange(22, 24).toShortLE() // 채널 수
        val bitsPerSample = wavData.copyOfRange(34, 36).toShortLE() // 비트 심도

        this.sampleRate = sampleRate

        Log.d("WAV Analysis", """
        RIFF Header: $riffHeader
        WAVE Header: $waveHeader
        Sample Rate: $sampleRate
        Number of Channels: $numChannels
        Bits per Sample: $bitsPerSample
    """.trimIndent())
    }


    private fun ByteArray.toShortLE(): Short {
        return ((this[1].toInt() and 0xFF) shl 8 or (this[0].toInt() and 0xFF)).toShort()
    }

    private fun ByteArray.toIntLE(): Int {
        return (this[3].toInt() and 0xFF shl 24) or
                (this[2].toInt() and 0xFF shl 16) or
                (this[1].toInt() and 0xFF shl 8) or
                (this[0].toInt() and 0xFF)
    }




    private fun playBase64EncodedWav(base64Wav: String): ByteArray? {
        try {
            // Base64 문자열 디코딩 (헤더 제거 없이 전체 WAV 데이터 처리)
            val wavData = Base64.decode(base64Wav, Base64.DEFAULT)

            // WAV 데이터 유효성 검사 (필요하다면 추가적으로 할 수 있음)
            if (isValidWavData(wavData)) {
                // 헤더 제거 없이 전체 데이터 반환
                analyzeWavHeader(wavData)
                return wavData
            }

        } catch (e: Exception) {
            Log.e("AudioPlayback", "Error decoding or playing audio", e)
        }

        Log.e("AudioPlayback", "Invalid WAV data")
        return null
    }

    private fun isValidWavData(data: ByteArray): Boolean {
        return data.size > 12 && String(data.copyOfRange(0, 4)) == "RIFF" && String(data.copyOfRange(8, 12)) == "WAVE"
    }

    fun chatTTSAccess(uid: String, wavId: String) {
        repos.accessWavData(uid, wavId) { success, wavData ->
            if (!DefaultSetting.debugMode) {
                if (wavData.isNotEmpty()) {
                    _wavAudioData.value = playBase64EncodedWav(wavData)
                    _wavSuccess.value = success
                } else {
                    Log.e("test", "빈문자열 도착 wav")
                    _wavSuccess.value = false
                }

            }
        }
    }

    private val _chatHistoryList = MutableLiveData<MutableList<ChatMessage>>()
    val chatHistoryList: MutableLiveData<MutableList<ChatMessage>> get() = _chatHistoryList

    private val _aiInfo = MutableLiveData<AiInfo>()
    val aiInfo: MutableLiveData<AiInfo> get() = _aiInfo
}