package gnu.idealab.persona_ai_ict_contest.data.interfaces

import gnu.idealab.persona_ai_ict_contest.data.models.AIChatAccessRequest
import gnu.idealab.persona_ai_ict_contest.data.models.AIChatAccessResponse
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessageRequest
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessageResponse
import gnu.idealab.persona_ai_ict_contest.data.models.DepartmentRequest
import gnu.idealab.persona_ai_ict_contest.data.models.DepartmentResponse
import gnu.idealab.persona_ai_ict_contest.data.models.LoginRequest
import gnu.idealab.persona_ai_ict_contest.data.models.LoginResponse
import gnu.idealab.persona_ai_ict_contest.data.models.UserDepartmentRequest
import gnu.idealab.persona_ai_ict_contest.data.models.UserDepartmentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST



interface ApiService {
    @POST("/ict/login/")
    fun login(@Body request: LoginRequest): Call<LoginResponse>


    @POST("/ict/department_list/")
    fun departmentList(@Body request: DepartmentRequest): Call<DepartmentResponse>


    @POST("/ict/department_select/")
    fun departmentSelect(@Body request: UserDepartmentRequest): Call<UserDepartmentResponse>


    @POST("/ict/chat/access/")
    fun accessChatMessage(@Body request: AIChatAccessRequest): Call<AIChatAccessResponse>

    @POST("/ict/chat/message/")
    fun sendChatMessage(@Body request: ChatMessageRequest): Call<ChatMessageResponse>
}

