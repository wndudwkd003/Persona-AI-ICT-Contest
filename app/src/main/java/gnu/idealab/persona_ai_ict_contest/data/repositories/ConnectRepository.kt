package gnu.idealab.persona_ai_ict_contest.data.repositories

import android.util.Log
import gnu.idealab.persona_ai_ict_contest.data.interfaces.ApiService
import gnu.idealab.persona_ai_ict_contest.data.models.AIChatAccessRequest
import gnu.idealab.persona_ai_ict_contest.data.models.AIChatAccessResponse
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessage
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessageRequest
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessageResponse
import gnu.idealab.persona_ai_ict_contest.data.models.DepartmentResponse
import gnu.idealab.persona_ai_ict_contest.data.models.LoginRequest
import gnu.idealab.persona_ai_ict_contest.data.models.LoginResponse
import gnu.idealab.persona_ai_ict_contest.data.models.UserDepartmentRequest
import gnu.idealab.persona_ai_ict_contest.data.models.UserDepartmentResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ConnectRepository {
    private val apiService: ApiService

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // 연결 타임아웃 설정
            .readTimeout(30, TimeUnit.SECONDS)    // 읽기 타임아웃 설정
            .writeTimeout(30, TimeUnit.SECONDS)   // 쓰기 타임아웃 설정
            .build()

        // Retrofit 초기화
        val retrofit = Retrofit.Builder()
            .baseUrl("http://117.16.153.30:8000") // 서버 주소
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    // 로그인 API 호출
    fun login(nickname: String, uid: String, callback: (Boolean) -> Unit) {
        val request = LoginRequest(nickname, uid)
        apiService.login(request).enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    // 성공적으로 응답을 받았을 때
                    callback(response.body()!!.success)
                } else {
                    // 응답은 받았지만 실패한 경우
                    Log.e("LoginError", "Response failed: ${response.code()} - ${response.message()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // 네트워크 요청 자체가 실패했을 때
                Log.e("LoginError", "Request failed: ${t.localizedMessage}", t)
                callback(false)
            }
        })
    }


    // 학과 리스트 호출
    fun departmentList(callback: (Boolean, List<String>) -> Unit) {
        // val request = DepartmentRequest()
        apiService.departmentList().enqueue(object: Callback<DepartmentResponse> {
            override fun onResponse(call: Call<DepartmentResponse>, response: Response<DepartmentResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback(response.body()!!.success, response.body()!!.departmentList)
                } else {
                    Log.e("DepartmentListError", "Response failed: ${response.code()} - ${response.message()}")

                    callback(false, emptyList())
                }
            }

            override fun onFailure(call: Call<DepartmentResponse>, t: Throwable) {
                Log.e("DepartmentListError", "Request failed: ${t.localizedMessage}", t)

                callback(false, emptyList())
            }
        })
    }


    // 유저 학과 등록
    fun departmentSelect(uid: String, department: String, callback: (Boolean) -> Unit) {
        val request = UserDepartmentRequest(uid, department)
        apiService.departmentSelect(request).enqueue(object: Callback<UserDepartmentResponse> {
            override fun onResponse(call: Call<UserDepartmentResponse>, response: Response<UserDepartmentResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback(response.body()!!.success)
                } else {
                    Log.e("DepartmentSelectError", "Response failed: ${response.code()} - ${response.message()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<UserDepartmentResponse>, t: Throwable) {
                Log.e("DepartmentSelectError", "Request failed: ${t.localizedMessage}", t)
                callback(false)
            }
        })
    }

    // AI 채팅방 접근
    fun accessChatMessage(uid: String, aiId: String, callback: (Boolean, List<ChatMessage>) -> Unit) {
        val request = AIChatAccessRequest(uid, aiId)
        apiService.accessChatMessage(request).enqueue(object: Callback<AIChatAccessResponse> {
            override fun onResponse(call: Call<AIChatAccessResponse>, response: Response<AIChatAccessResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback(response.body()!!.success, response.body()!!.chatHistory)
                } else {
                    callback(false, emptyList())
                    Log.e("AccessChatError", "Response failed: ${response.code()} - ${response.message()}")

                }
            }
            override fun onFailure(call: Call<AIChatAccessResponse>, t: Throwable) {
                Log.e("AccessChatError", "Request failed: ${t.localizedMessage}", t)

                callback(false, emptyList())
            }
        })
    }

    // 채팅 전송
    fun sendChatMessage(message: ChatMessage, callback: (Boolean, ChatMessage) -> Unit) {
        val request = ChatMessageRequest( message)
        apiService.sendChatMessage(request).enqueue(object: Callback<ChatMessageResponse> {
            override fun onResponse(
                call: Call<ChatMessageResponse>,
                response: Response<ChatMessageResponse>,
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback(response.body()!!.success, response.body()!!.message)
                } else {
                    Log.e("SendChatMessageError", "Response failed: ${response.code()} - ${response.message()}")

                    callback(false, ChatMessage("", "", "", "", "", true))
                }
            }

            override fun onFailure(call: Call<ChatMessageResponse>, t: Throwable) {
                Log.e("SendChatMessageError", "Request failed: ${t.localizedMessage}", t)

                callback(false, ChatMessage("", "", "", "", "", true))
            }
        })
    }


}
