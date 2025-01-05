package gnu.idealab.persona_ai_ict_contest.viewmodel

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    fun validateNickname(nickname: String): NicknameValidationResult {
        return when {
            nickname.isBlank() -> NicknameValidationResult.EMPTY
            nickname.length > 12 -> NicknameValidationResult.TOO_LONG
            nickname.contains(" ") -> NicknameValidationResult.CONTAINS_WHITESPACE
            !nickname.matches("^[a-zA-Z0-9가-힣]*$".toRegex()) -> NicknameValidationResult.CONTAINS_SPECIAL_CHAR
            else -> NicknameValidationResult.VALID
        }
    }
}

enum class NicknameValidationResult {
    EMPTY, TOO_LONG, CONTAINS_WHITESPACE, CONTAINS_SPECIAL_CHAR, VALID
}
