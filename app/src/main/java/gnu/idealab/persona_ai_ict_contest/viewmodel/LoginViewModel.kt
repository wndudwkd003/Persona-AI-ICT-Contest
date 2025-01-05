package gnu.idealab.persona_ai_ict_contest.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gnu.idealab.persona_ai_ict_contest.DefaultSetting
import gnu.idealab.persona_ai_ict_contest.data.repositories.LoginRepository
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class LoginViewModel(
    private val repos: LoginRepository
) : ViewModel() {

    private val _nicknameValidationResult = MutableLiveData<NicknameValidationResult>()
    val nicknameValidationResult: MutableLiveData<NicknameValidationResult> get() = _nicknameValidationResult

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun validateNickname(nickname: String) {
        val result = when {
            nickname.isBlank() -> NicknameValidationResult.EMPTY
            nickname.length > 12 -> NicknameValidationResult.TOO_LONG
            nickname.contains(" ") -> NicknameValidationResult.CONTAINS_WHITESPACE
            !nickname.matches("^[a-zA-Z0-9가-힣]*$".toRegex()) -> NicknameValidationResult.CONTAINS_SPECIAL_CHAR
            else -> NicknameValidationResult.VALID
        }
        Log.d("LoginViewModel", "$result")
        _errorMessage.value = result.errorMessage()
        _nicknameValidationResult.value = result
    }

    fun login(nickname: String, context: Context) {
        validateNickname(nickname)
        if (_nicknameValidationResult.value == NicknameValidationResult.VALID) {
            val uid = generateHash(nickname)
            // 서버 사용시 디버그 모드 확인하기
            if (DefaultSetting.debugMode) {
                // 디버그 모드
                saveUserData(context, nickname, uid)
                _loginSuccess.value = true
            } else {
                // 실제 서버로 닉네임과 uid(해시)가 사용 가능한지 체크 후 저장
                repos.login(nickname, uid) { success ->
                    if (success) {
                        saveUserData(context, nickname, uid)
                        _loginSuccess.value = true
                    } else {
                        val result = NicknameValidationResult.SERVER_ERROR
                        _nicknameValidationResult.value = result
                        _errorMessage.value = result.errorMessage()
                        _loginSuccess.value = false
                    }
                }
            }
        } else {
            _loginSuccess.value = false
        }
    }

    private fun generateHash(nickname: String): String {
        // 현재 날짜 가져오기
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        // 랜덤 난수 생성
        val randomValue = Random.nextInt(100000, 999999)

        // 닉네임 + 날짜 + 랜덤 난수 결합
        val input = "$nickname$currentDate$randomValue"

        // SHA-256 해시 생성
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun saveUserData(context: Context, nickname: String, uid: String) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("nickname", nickname)
            putString("uid", uid)
            apply()
        }
    }

    private fun NicknameValidationResult.errorMessage(): String = when (this) {
        NicknameValidationResult.EMPTY -> "닉네임을 입력해주세요."
        NicknameValidationResult.TOO_LONG -> "닉네임은 12글자 이하로 입력해주세요."
        NicknameValidationResult.CONTAINS_WHITESPACE -> "닉네임에 공백이 포함될 수 없습니다."
        NicknameValidationResult.CONTAINS_SPECIAL_CHAR -> "닉네임에 특수문자를 사용할 수 없습니다."
        NicknameValidationResult.SERVER_ERROR -> "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
        else -> ""
    }
}

enum class NicknameValidationResult {
    DEFAULT, EMPTY, TOO_LONG, CONTAINS_WHITESPACE, CONTAINS_SPECIAL_CHAR, VALID, SERVER_ERROR
}



class LoginViewModelFactory(
    private val repository: LoginRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}