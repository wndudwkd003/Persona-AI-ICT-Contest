package gnu.idealab.persona_ai_ict_contest.custom_view.chat_view

import android.content.Context
import android.graphics.BitmapFactory
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gnu.idealab.persona_ai_ict_contest.R
import gnu.idealab.persona_ai_ict_contest.data.models.AiInfo
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessage
import gnu.idealab.persona_ai_ict_contest.databinding.AiChatItemBinding
import gnu.idealab.persona_ai_ict_contest.databinding.UserChatItemBinding

// ChatAdapter 코드
class ChatAdapter(private val chatHistory: MutableList<ChatMessage>, private val aiInfo: AiInfo, context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val assetManager = context.assets
    companion object {
        private const val VIEW_TYPE_AI = 1
        private const val VIEW_TYPE_USER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatHistory[position].isAI) VIEW_TYPE_AI else VIEW_TYPE_USER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_AI) {
            val binding = AiChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AIViewHolder(binding)
        } else {
            val binding = UserChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            UserViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatHistory[position]
        if (holder is AIViewHolder) {
            holder.bind(message)
        } else if (holder is UserViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = chatHistory.size

    fun submitMessage(updatedMessage: ChatMessage) {
        Log.d("ChatAdapter", "updatedMessage: ${updatedMessage.toString()}")
        notifyItemInserted(chatHistory.size - 1)
        if (updatedMessage.isAI) {
            Log.d("ChatAdapter", "AI message detected. Applying typing effect.")
            applyTypingEffect(chatHistory.size - 1, updatedMessage.message)
        }
    }

    private fun applyTypingEffect(position: Int, fullText: String) {
        if (position < 0 || position >= chatHistory.size) return
        val handler = android.os.Handler(android.os.Looper.getMainLooper())

        var currentText = ""
        for (i in fullText.indices) {
            handler.postDelayed({
                currentText += fullText[i]
                chatHistory[position] = chatHistory[position].copy(message = currentText)
                notifyItemChanged(position)
                Log.d("TypingEffect", "Updated text: $currentText at position: $position")

                // 타자 효과가 완료되면 날짜를 visible로 설정
                if (i == fullText.length - 1) { // 마지막 글자까지 출력 완료
                    handler.post {
                        notifyItemChanged(position) // 최종 업데이트로 날짜 표시
                    }
                }
            }, i * 100L)
        }
    }



    inner class AIViewHolder(private val binding: AiChatItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.chatTextView.text = message.message
            binding.chatTimeText.text = message.timestamp

            // AssetManager에서 이미지 로드
            try {
                val inputStream = assetManager.open(aiInfo.imgPath)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.profileImageview.setImageBitmap(bitmap)
                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
                binding.profileImageview.setImageResource(R.drawable.sample_image)
            }

            // audio_image_button 클릭 리스너 설정
            binding.audioImageButton.setOnClickListener {
                // 버튼 애니메이션: 작아졌다가 커지기
                it.animate()
                    .scaleX(0.8f) // 가로로 80% 크기로 줄임
                    .scaleY(0.8f) // 세로로 80% 크기로 줄임
                    .setDuration(100) // 줄어드는 데 걸리는 시간 (100ms)
                    .withEndAction {
                        // 원래 크기로 복구
                        it.animate()
                            .scaleX(1f) // 원래 크기
                            .scaleY(1f) // 원래 크기
                            .setDuration(100) // 복구하는 데 걸리는 시간 (100ms)
                            .start()
                    }
                    .start()

                // wavData 재생
                message.wavData?.let {
                    if (isValidWavData(message.wavData)) {
                        playWav(message.wavData)
                    } else {
                        Log.e("AudioPlayback", "Invalid WAV data")
                    }
                }
            }
        }
    }

    private fun playWav(data: ByteArray) {
        val audioTrack: AudioTrack?

        try {
            // WAV 헤더 제거 (헤더 크기: 44 bytes)
            val audioData = data.copyOfRange(44, data.size)

            // 샘플 속성 설정 (샘플 속성은 헤더 정보에 따라 설정해야 함)
            val sampleRate = 44100 // 일반적으로 44.1kHz
            val channelConfig = AudioFormat.CHANNEL_OUT_MONO // 모노
            val audioFormat = AudioFormat.ENCODING_PCM_16BIT // 16비트 PCM

            // AudioTrack 객체 생성
            audioTrack = AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                channelConfig,
                audioFormat,
                audioData.size,
                AudioTrack.MODE_STATIC
            )

            // 데이터 쓰기
            audioTrack.write(audioData, 0, audioData.size)
            audioTrack.play()

        } catch (e: Exception) {
            Log.e("AudioPlayback", "Error playing audio", e)
        }
    }


    private fun isValidWavData(data: ByteArray): Boolean {
        return data.size > 12 && String(data.copyOfRange(0, 4)) == "RIFF" && String(data.copyOfRange(8, 12)) == "WAVE"
    }


    inner class UserViewHolder(private val binding: UserChatItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.chatTextView.text = message.message
            binding.chatTimeText.text = message.timestamp
        }
    }
}


