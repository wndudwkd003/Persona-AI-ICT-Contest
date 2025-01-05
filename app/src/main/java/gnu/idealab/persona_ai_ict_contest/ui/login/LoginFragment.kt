package gnu.idealab.persona_ai_ict_contest.ui.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.content.Context
import androidx.core.widget.addTextChangedListener
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.util.Log
import androidx.navigation.fragment.findNavController
import gnu.idealab.persona_ai_ict_contest.R
import gnu.idealab.persona_ai_ict_contest.databinding.FragmentLoginBinding
import gnu.idealab.persona_ai_ict_contest.ui.home.HomeFragment
import gnu.idealab.persona_ai_ict_contest.viewmodel.LoginViewModel
import gnu.idealab.persona_ai_ict_contest.viewmodel.NicknameValidationResult
import java.security.MessageDigest

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SharedPreferences에서 닉네임과 UID 확인
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val savedNickname = sharedPref.getString("nickname", null)
        val savedUid = sharedPref.getString("uid", null)

        // Log.d("LoginFragment", "Saved Nickname: $savedNickname, Saved UID: $savedUid")


        // 닉네임과 UID가 저장되어 있으면 HomeFragment로 이동
        if (!savedNickname.isNullOrEmpty() && !savedUid.isNullOrEmpty()) {
            gotoHomeFragment()
            return
        }

        // 닉네임 입력 필드 검증
        binding.textInputLayout.editText?.addTextChangedListener {
            val nickname = it.toString()
            updateErrorMessage(viewModel.validateNickname(nickname))
        }

        binding.textInputLayout.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                checkAbleLogine()
                true
            } else {
                false
            }
        }

        // 로그인 버튼 클릭
        binding.loginButton.setOnClickListener {
            checkAbleLogine()
        }
    }

    private fun checkAbleLogine() {
        val nickname = binding.textInputLayout.editText?.text.toString()
        val validationResult = viewModel.validateNickname(nickname)

        // 키패드 닫기
        hideKeyboard()

        if (validationResult == NicknameValidationResult.VALID) {
            hashGeneratorAndGotoHome(nickname)
        } else {
            faileLoginAndErrorMessage(validationResult)
        }
    }

    private fun faileLoginAndErrorMessage(validationResult: NicknameValidationResult) {
        updateErrorMessage(validationResult)
        triggerShakeAnimation(binding.errorText) // 진동 효과
    }

    private fun hashGeneratorAndGotoHome(nickname: String) {
        // 해시 난수 생성 및 SharedPreferences 저장
        val uid = generateHash(nickname)
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("nickname", nickname)
            putString("uid", uid)
            apply()
        }

        // HomeFragment로 이동
        gotoHomeFragment()
    }

    private fun gotoHomeFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

    private fun generateHash(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }


    private fun updateErrorMessage(validationResult: NicknameValidationResult) {
        binding.errorText.visibility = when (validationResult) {
            NicknameValidationResult.EMPTY -> {
                binding.errorText.text = "닉네임을 입력해주세요."
                View.VISIBLE
            }
            NicknameValidationResult.TOO_LONG -> {
                binding.errorText.text = "닉네임은 12글자 이하로 입력해주세요."
                View.VISIBLE
            }
            NicknameValidationResult.CONTAINS_WHITESPACE -> {
                binding.errorText.text = "닉네임에 공백이 포함될 수 없습니다."
                View.VISIBLE
            }
            NicknameValidationResult.CONTAINS_SPECIAL_CHAR -> {
                binding.errorText.text = "닉네임에 특수문자를 사용할 수 없습니다."
                View.VISIBLE
            }
            NicknameValidationResult.VALID -> {
                binding.errorText.text = ""
                View.INVISIBLE
            }
        }
    }

    private fun triggerShakeAnimation(view: View) {
        // X축으로 진동 효과를 만들기 위한 애니메이션
        val animator1 = ObjectAnimator.ofFloat(view, "translationX", 0f, 25f)
        val animator2 = ObjectAnimator.ofFloat(view, "translationX", 25f, -25f)
        val animator3 = ObjectAnimator.ofFloat(view, "translationX", -25f, 0f)

        // 애니메이터 세트로 연결
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animator1, animator2, animator3)
        animatorSet.duration = 100 // 각 애니메이션 지속 시간 (ms)
        animatorSet.start()
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
