package gnu.idealab.persona_ai_ict_contest.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import gnu.idealab.persona_ai_ict_contest.R
import gnu.idealab.persona_ai_ict_contest.data.repositories.ConnectRepository
import gnu.idealab.persona_ai_ict_contest.databinding.FragmentLoginBinding
import gnu.idealab.persona_ai_ict_contest.viewmodel.LoginViewModel
import gnu.idealab.persona_ai_ict_contest.viewmodel.LoginViewModelFactory
import gnu.idealab.persona_ai_ict_contest.viewmodel.NicknameValidationResult

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // LoginViewModel을 팩토리를 사용하여 생성
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 레포지토리 생성
        val repository = ConnectRepository()

        // 팩토리를 사용해 ViewModel 생성
        val factory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 앱 실행시 shared preference 확인하기
        if (isUserLoggedIn()) {
            gotoSelectDepartmentFragment()
            return
        }

        // 닉네임 입력 필드 실시간 검증
        binding.textInputLayout.editText?.addTextChangedListener {
            val nickname = it.toString()
            viewModel.validateNickname(nickname)
        }

        // 버튼 클릭 이벤트
        binding.loginButton.setOnClickListener {
            hideKeyboard()
            val nickname = binding.textInputLayout.editText?.text.toString()
            viewModel.login(nickname, requireContext())
        }

        binding.textInputLayout.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                val nickname = binding.textInputLayout.editText?.text.toString()
                viewModel.login(nickname, requireContext())
                true
            } else {
                false
            }
        }

        // ViewModel의 상태 관찰
        viewModel.nicknameValidationResult.observe(viewLifecycleOwner, Observer { result ->
            if (result != NicknameValidationResult.VALID) {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = viewModel.errorMessage.value
                triggerShakeAnimation(binding.errorText)
            } else if (result == NicknameValidationResult.VALID) {
                binding.errorText.text = viewModel.errorMessage.value
            }
        })

        viewModel.loginSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) gotoSelectDepartmentFragment()
        })
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val nickname = sharedPref.getString("nickname", null)
        val uid = sharedPref.getString("uid", null)
        return !nickname.isNullOrEmpty() && !uid.isNullOrEmpty()
    }


    // X축 진동 애니메이션 메서드
    private fun triggerShakeAnimation(view: View) {
        val animator1 = ObjectAnimator.ofFloat(view, "translationX", 0f, 25f)
        val animator2 = ObjectAnimator.ofFloat(view, "translationX", 25f, -25f)
        val animator3 = ObjectAnimator.ofFloat(view, "translationX", -25f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animator1, animator2, animator3)
        animatorSet.duration = 100 // 각 애니메이션의 지속 시간
        animatorSet.start()
    }

    private fun gotoSelectDepartmentFragment() {
        findNavController().navigate(
            R.id.action_loginFragment_to_selectDepartmentFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true) // 현재 프래그먼트를 스택에서 제거
                .build()
        )
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(requireContext())
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
