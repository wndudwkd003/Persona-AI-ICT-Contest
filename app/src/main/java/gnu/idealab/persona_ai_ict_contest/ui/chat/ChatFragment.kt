package gnu.idealab.persona_ai_ict_contest.ui.chat

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import gnu.idealab.persona_ai_ict_contest.DefaultSetting
import gnu.idealab.persona_ai_ict_contest.custom_view.chat_view.ChatAdapter
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessage
import gnu.idealab.persona_ai_ict_contest.data.repositories.ConnectRepository
import gnu.idealab.persona_ai_ict_contest.databinding.FragmentChatBinding
import gnu.idealab.persona_ai_ict_contest.viewmodel.ChatViewModel
import gnu.idealab.persona_ai_ict_contest.viewmodel.MessageInterface

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            if (textMessage.isNotEmpty()) {
                // 메시지 전송 로직
                viewModel.sendMessage(
                    uid = DefaultSetting.getUID(requireContext()),
                    message = textMessage,
                    listener = object : MessageInterface {
                        override fun successMessage(message: ChatMessage) {
                            binding.newMessageButton.visibility = View.VISIBLE // 응답이 오면 새로운 메시지 버튼 나오게
                            binding.chatTextview.setText("")
                            binding.chatTextview.clearFocus()
                            binding.chatTextview.setHint("메시지")

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

           // adapter.submitList(chatHistory.toList())
        }

    }



    override fun onDestroy() {
        super.onDestroy()

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