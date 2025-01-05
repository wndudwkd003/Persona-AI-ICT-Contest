package gnu.idealab.persona_ai_ict_contest.data.interfaces

import gnu.idealab.persona_ai_ict_contest.data.models.LoginRequest
import gnu.idealab.persona_ai_ict_contest.data.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST



interface ApiService {
    @POST("/ict/login/")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
