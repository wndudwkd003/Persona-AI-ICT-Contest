package gnu.idealab.persona_ai_ict_contest.ui.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.color.MaterialColors
import gnu.idealab.persona_ai_ict_contest.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoAppFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoAppFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 상태 표시줄 색상 변경


        // 상태 표시줄을 흰색으로 설정
        activity?.window?.let { window ->
            window.statusBarColor = requireContext().getColor(android.R.color.white)

            // 흰색 배경에 어두운 아이콘 설정
            WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // 상태 표시줄을 Material Theme 기본 색상으로 복원
        activity?.window?.let { window ->
            // Material Theme에서 colorSurface 색상 가져오기
            val statusBarColor = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorSurface, "Default Color")
            window.statusBarColor = statusBarColor

            // 기본 색상에 맞게 아이콘 색상 설정 (어두운 색인지 밝은 색인지 확인)
            WindowCompat.getInsetsController(window, requireActivity().window.decorView)?.isAppearanceLightStatusBars =
                MaterialColors.isColorLight(statusBarColor)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoAppFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoAppFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}