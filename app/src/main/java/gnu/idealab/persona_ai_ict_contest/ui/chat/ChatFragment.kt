package gnu.idealab.persona_ai_ict_contest.ui.chat

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import com.google.android.material.color.MaterialColors
import gnu.idealab.persona_ai_ict_contest.R
import gnu.idealab.persona_ai_ict_contest.data.models.AiInfo
import gnu.idealab.persona_ai_ict_contest.data.repositories.ConnectRepository
import gnu.idealab.persona_ai_ict_contest.databinding.FragmentChatBinding
import gnu.idealab.persona_ai_ict_contest.viewmodel.ChatViewModel

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ChatFragment()
    }

    private val repos = ConnectRepository()

    private val viewModel: ChatViewModel by viewModels()

    private lateinit var aiInfo: AiInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            aiInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable("aiInfo", AiInfo::class.java)!!
            } else {
                it.getParcelable("aiInfo")!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        // return inflater.inflate(R.layout.fragment_chat, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 상태 표시줄을 흰색으로 설정
        activity?.window?.let { window ->
            window.statusBarColor = requireContext().getColor(android.R.color.white)

            // 흰색 배경에 어두운 아이콘 설정
            WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = true
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