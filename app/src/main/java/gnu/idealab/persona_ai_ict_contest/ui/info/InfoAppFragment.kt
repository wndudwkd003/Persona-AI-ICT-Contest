package gnu.idealab.persona_ai_ict_contest.ui.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import gnu.idealab.persona_ai_ict_contest.R
import gnu.idealab.persona_ai_ict_contest.databinding.FragmentInfoAppBinding

class InfoAppFragment : Fragment() {

    private var _binding: FragmentInfoAppBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInfoAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 상태 표시줄을 흰색으로 설정
        activity?.window?.let { window ->
            window.statusBarColor = requireContext().getColor(android.R.color.white)
            WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = true
        }

        // Toolbar 설정
        binding.toolbar.apply {
            (activity as AppCompatActivity).setSupportActionBar(this)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
            setNavigationOnClickListener {
                findNavController().navigateUp() // 뒤로가기 버튼 클릭 시 동작
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // 상태 표시줄을 Material Theme 기본 색상으로 복원
        activity?.window?.let { window ->
            val statusBarColor = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorSurface,
                "Default Color"
            )
            window.statusBarColor = statusBarColor
            WindowCompat.getInsetsController(window, requireActivity().window.decorView)
                ?.isAppearanceLightStatusBars = MaterialColors.isColorLight(statusBarColor)
        }
    }
}
