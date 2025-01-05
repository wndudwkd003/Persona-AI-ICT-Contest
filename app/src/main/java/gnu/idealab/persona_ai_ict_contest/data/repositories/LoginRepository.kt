package gnu.idealab.persona_ai_ict_contest.data.repositories

import gnu.idealab.persona_ai_ict_contest.data.interfaces.ApiService
import gnu.idealab.persona_ai_ict_contest.data.models.LoginRequest
import gnu.idealab.persona_ai_ict_contest.data.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginRepository {
    private val apiService: ApiService

    init {
        // Retrofit 초기화
        val retrofit = Retrofit.Builder()
            .baseUrl("http://117.16.153.30") // 서버 주소
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    // 로그인 API 호출
    fun login(nickname: String, uid: String, callback: (Boolean) -> Unit) {
        val request = LoginRequest(nickname, uid)
        apiService.login(request).enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback(response.body()!!.success)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(false)
            }
        })
    }


}
