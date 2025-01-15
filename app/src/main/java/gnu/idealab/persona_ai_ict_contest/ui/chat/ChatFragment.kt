package gnu.idealab.persona_ai_ict_contest.ui.chat

import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import gnu.idealab.persona_ai_ict_contest.DefaultSetting
import gnu.idealab.persona_ai_ict_contest.custom_view.chat_view.ChatAdapter
import gnu.idealab.persona_ai_ict_contest.custom_view.chat_view.TTSListener
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessage
import gnu.idealab.persona_ai_ict_contest.data.repositories.ConnectRepository
import gnu.idealab.persona_ai_ict_contest.databinding.FragmentChatBinding
import gnu.idealab.persona_ai_ict_contest.viewmodel.ChatViewModel
import gnu.idealab.persona_ai_ict_contest.viewmodel.MessageInterface

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var speechRecognizer: SpeechRecognizer

    companion object {
        fun newInstance() = ChatFragment()
    }

    private val repos = ConnectRepository()

    private val viewModel: ChatViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = ChatFragmentArgs.fromBundle(requireArguments())
        val aiInfo = args.aiInfo
        val chatMessages = args.chatMessages.toMutableList() // Convert array to list

        Log.d("ChatFragment", "Received AiInfo: $aiInfo")
        Log.d("ChatFragment", "Received ChatMessages: $chatMessages")

        viewModel.setAiInfo(aiInfo)
        viewModel.setMessageList(chatMessages)
        viewModel.initMessageListSize = chatMessages.size
    }

    private val REQUEST_CODE_MIC = 1001 // 고유 코드 설정

    // 권한 확인 및 요청 함수
    private fun checkMicrophonePermission() {
        // 권한이 허용되지 않았는지 확인
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한 요청
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE_MIC
            )
        } else {
            // 이미 권한이 허용된 경우
            startSpeechRecognition()
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_MIC) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "마이크 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
                startSpeechRecognition()
            } else {
                Toast.makeText(requireContext(), "마이크 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR") // 한국어 설정
            putExtra(RecognizerIntent.EXTRA_PROMPT, "음성을 입력하세요") // 안내 문구
        }

        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Toast.makeText(requireContext(), "듣고 있습니다...", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                val text = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0) ?: ""
                binding.chatTextview.setText(text) // 음성 입력 결과를 텍스트뷰에 설정
            }

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_NETWORK -> "네트워크 오류가 발생했습니다."
                    SpeechRecognizer.ERROR_AUDIO -> "오디오 오류가 발생했습니다."
                    SpeechRecognizer.ERROR_NO_MATCH -> "일치하는 결과가 없습니다."
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "현재 음성 인식이 사용 중입니다."
                    else -> "알 수 없는 오류가 발생했습니다."
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }


            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBeginningOfSpeech() {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer.startListening(intent)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        // return inflater.inflate(R.layout.fragment_chat, container, false)

        // 채팅방 타이틀 설정
        binding.toolbar.title = "${viewModel.aiInfo.value?.name}의 대화방"

        return binding.root
    }




    private fun playWav(data: ByteArray, sampleRate: Int) {
        val audioTrack: AudioTrack?

        try {
            // WAV 헤더 제거
            val audioData = data.copyOfRange(44, data.size)

            // 샘플 속성 설정 (샘플 속성은 헤더 정보에 따라 설정해야 함)
            val sampleRate = sampleRate // 22050
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())

        // 상태 표시줄을 흰색으로 설정
        activity?.window?.let { window ->
            window.statusBarColor = requireContext().getColor(android.R.color.white)
            WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = true
        }

        binding.toolbar.apply {
            (activity as AppCompatActivity).setSupportActionBar(this)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
            setNavigationOnClickListener {
                findNavController().navigateUp() // 뒤로가기 버튼 클릭 시 동작
            }
        }



        // ChatAdapter 연결
        val chatHistory = viewModel.chatHistoryList.value!!

        val adapter = ChatAdapter(chatHistory, viewModel.aiInfo.value!!, requireContext())

        adapter.setTTSOnClickListener(object: TTSListener {
            override fun click(wavId: String) {
                viewModel.chatTTSAccess(DefaultSetting.getUID(requireContext()), wavId)
            }
        })

        //
        viewModel.wavSuccess.observe(viewLifecycleOwner) { success ->
            if (success && viewModel.wavAudioData.value != null && viewModel.wavAudioData.value?.isNotEmpty()!!) {
                viewModel.wavAudioData.value?.let {
                    playWav(it, viewModel.sampleRate)
                }
            } else {
                Toast.makeText(requireContext(), "아직 TTS를 만들고 있어요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.chatRecyclerview.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            this.adapter = adapter

            // 초기 프래그먼트 도착 시 가장 아래로 스크롤
            post {
                if (chatHistory.isNotEmpty()) {
                    scrollToPosition(chatHistory.size - 1)
                }
            }

            // RecyclerView 스크롤 감지
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        // 스크롤이 하단에 도달하면 버튼 숨김
                        binding.newMessageButton.visibility = View.GONE
                    }
                }
            })
        }

        binding.micButton.setOnClickListener {
            binding.micButton.animate()
                .scaleX(0.8f) // 가로로 80% 크기로 줄임
                .scaleY(0.8f) // 세로로 80% 크기로 줄임
                .setDuration(100) // 줄어드는 데 걸리는 시간 (100ms)
                .withEndAction {
                    // 원래 크기로 복구
                    binding.micButton.animate()
                        .scaleX(1f) // 원래 크기
                        .scaleY(1f) // 원래 크기
                        .setDuration(100) // 복구하는 데 걸리는 시간 (100ms)
                        .start()
                }
                .start()

            checkMicrophonePermission()
        }


        // 채팅 보내기 버튼
        // 채팅 보내기 버튼
        binding.chatButton.setOnClickListener {
            val textMessage = binding.chatTextview.text.toString()

            // 버튼 애니메이션: 작아졌다가 커지기
            binding.chatButton.animate()
                .scaleX(0.8f) // 가로로 80% 크기로 줄임
                .scaleY(0.8f) // 세로로 80% 크기로 줄임
                .setDuration(100) // 줄어드는 데 걸리는 시간 (100ms)
                .withEndAction {
                    // 원래 크기로 복구
                    binding.chatButton.animate()
                        .scaleX(1f) // 원래 크기
                        .scaleY(1f) // 원래 크기
                        .setDuration(100) // 복구하는 데 걸리는 시간 (100ms)
                        .start()
                }
                .start()


            binding.chatTextview.setText("")
            binding.chatTextview.clearFocus()
            binding.chatTextview.setHint("메시지")


            if (textMessage.isNotEmpty()) {
                // 메시지 전송 로직
                viewModel.sendMessage(
                    uid = DefaultSetting.getUID(requireContext()),
                    aiType = viewModel.aiInfo.value!!.id,
                    message = textMessage,
                    listener = object : MessageInterface {
                        override fun successMessage(message: ChatMessage) {
                            binding.newMessageButton.visibility = View.VISIBLE // 응답이 오면 새로운 메시지 버튼 나오게

                        }
                    }
                )
            }
        }


        // 새로운 메시지 버튼 클릭 시 가장 아래로 스크롤
        binding.newMessageButton.setOnClickListener {
            binding.newMessageButton.visibility = View.GONE
            binding.chatRecyclerview.post {
                if (chatHistory.isNotEmpty()) {
                    binding.chatRecyclerview.scrollToPosition(chatHistory.size - 1)
                }
            }
        }


        viewModel.chatHistoryList.observe(viewLifecycleOwner) { chatHistory ->
            if (chatHistory.size != viewModel.initMessageListSize) {
                adapter.submitMessage(chatHistory[chatHistory.size - 1])
            }

            if (chatHistory.isNotEmpty()) {
                binding.emptyTextview.visibility = View.GONE
            } else {
                binding.emptyTextview.visibility = View.VISIBLE
            }

           // adapter.submitList(chatHistory.toList())
        }

    }



    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.stopListening() // 먼저 중지
        speechRecognizer.destroy()       // 리소스 해제

        // 상태 표시줄을 Material Theme 기본 색상으로 복원
        activity?.window?.let { window ->
            // Material Theme에서 colorSurface 색상 가져오기
            val statusBarColor = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorSurface, "Default Color")
            window.statusBarColor = statusBarColor

            // 기본 색상에 맞게 아이콘 색상 설정 (어두운 색인지 밝은 색인지 확인)
            WindowCompat.getInsetsController(window, requireActivity().window.decorView)?.isAppearanceLightStatusBars =
                MaterialColors.isColorLight(statusBarColor)
        }

        _binding = null
    }
}