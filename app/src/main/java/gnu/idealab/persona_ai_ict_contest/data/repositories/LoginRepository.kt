package gnu.idealab.persona_ai_ict_contest.data.repositories

import gnu.idealab.persona_ai_ict_contest.data.interfaces.ApiService
import gnu.idealab.persona_ai_ict_contest.data.models.DepartmentRequest
import gnu.idealab.persona_ai_ict_contest.data.models.DepartmentResponse
import gnu.idealab.persona_ai_ict_contest.data.models.LoginRequest
import gnu.idealab.persona_ai_ict_contest.data.models.LoginResponse
import gnu.idealab.persona_ai_ict_contest.data.models.UserDepartmentRequest
import gnu.idealab.persona_ai_ict_contest.data.models.UserDepartmentResponse
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

    // 학과 리스트 호출
    fun departmentList(uid: String, callback: (Boolean, List<String>) -> Unit) {
        val request = DepartmentRequest(uid)
        apiService.departmentList(request).enqueue(object: Callback<DepartmentResponse> {
            override fun onResponse(call: Call<DepartmentResponse>, response: Response<DepartmentResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback(response.body()!!.success, response.body()!!.departmentList)
                } else {
                    callback(false, emptyList())
                }
            }

            override fun onFailure(call: Call<DepartmentResponse>, t: Throwable) {
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
                    callback(false)
                }
            }

            override fun onFailure(call: Call<UserDepartmentResponse>, t: Throwable) {
                callback(false)
            }
        })
    }


}
