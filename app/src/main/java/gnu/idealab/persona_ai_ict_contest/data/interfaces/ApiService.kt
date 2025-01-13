package gnu.idealab.persona_ai_ict_contest.data.interfaces

import gnu.idealab.persona_ai_ict_contest.data.models.AIChatAccessRequest
import gnu.idealab.persona_ai_ict_contest.data.models.AIChatAccessResponse
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessageRequest
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessageResponse
import gnu.idealab.persona_ai_ict_contest.data.models.DepartmentResponse
import gnu.idealab.persona_ai_ict_contest.data.models.LoginRequest
import gnu.idealab.persona_ai_ict_contest.data.models.LoginResponse
import gnu.idealab.persona_ai_ict_contest.data.models.UserDepartmentRequest
import gnu.idealab.persona_ai_ict_contest.data.models.UserDepartmentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


interface ApiService {
    @POST("/ICT/user/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>


    @GET("/ICT/major/list")
    fun departmentList(): Call<DepartmentResponse>


    @PUT("/ICT/setmajor")
    fun departmentSelect(@Body request: UserDepartmentRequest): Call<UserDepartmentResponse>


    @POST("/ICT/chatroom/connect")
    fun accessChatMessage(@Body request: AIChatAccessRequest): Call<AIChatAccessResponse>

    @POST("/ICT/chat/message/")
    fun sendChatMessage(@Body request: ChatMessageRequest): Call<ChatMessageResponse>
}

