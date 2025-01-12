package gnu.idealab.persona_ai_ict_contest.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import gnu.idealab.persona_ai_ict_contest.R
import gnu.idealab.persona_ai_ict_contest.databinding.FragmentSelectDepartmentBinding
import gnu.idealab.persona_ai_ict_contest.viewmodel.SelectDepartmentViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectDepartmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectDepartmentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentSelectDepartmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SelectDepartmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_select_department, container, false)
        _binding = FragmentSelectDepartmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isDepartmentSelected()) {
            gotoHomeFragment()
            return
        }

        val uid = getUserUID()
        viewModel.departmentList(uid)

        // 학고 정보 불러오기 및 스피너 설정
        viewModel.departmentListSuccess.observe(viewLifecycleOwner) { success ->
            if (!success) {
                Toast.makeText(requireContext(), "학과 리스트를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val departmentList = viewModel.departmentList.value
                val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, departmentList!!)
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.departmentSpinner.adapter = spinnerAdapter
            }
        }


        // 완료 버튼 리스너 설정
        binding.completeButton.setOnClickListener {
            val selectedDepartment = binding.departmentSpinner.selectedItem.toString()
            // Toast.makeText(requireContext(), "선택한 학과: $selectedDepartment", Toast.LENGTH_SHORT).show()

            // 학과 정보 저장
            viewModel.saveDepartment(requireContext(), uid, selectedDepartment) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "환영합니다!", Toast.LENGTH_SHORT).show()
                    gotoHomeFragment()
                } else {
                    Toast.makeText(requireContext(), "학과 정보 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun gotoHomeFragment() {
        findNavController().navigate(
            R.id.action_selectDepartmentFragment_to_homeFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(R.id.selectDepartmentFragment, true) // 현재 프래그먼트를 스택에서 제거
                .build()
        )
        Toast.makeText(requireContext(), "환영합니다!", Toast.LENGTH_SHORT).show()
    }


    private fun getUserUID(): String {
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("uid", "") ?: ""
    }

    private fun isDepartmentSelected(): Boolean {
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.contains("department")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SelectDepartmentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SelectDepartmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}