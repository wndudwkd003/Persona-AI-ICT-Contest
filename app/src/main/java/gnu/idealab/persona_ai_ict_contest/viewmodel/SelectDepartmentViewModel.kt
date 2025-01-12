package gnu.idealab.persona_ai_ict_contest.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gnu.idealab.persona_ai_ict_contest.DefaultSetting
import gnu.idealab.persona_ai_ict_contest.data.repositories.ConnectRepository

class SelectDepartmentViewModel : ViewModel() {
    private val connectRepository: ConnectRepository = ConnectRepository()
    private val _departmentList = MutableLiveData<List<String>>()
    val departmentList: MutableLiveData<List<String>> get() = _departmentList

    private val _departmentListSuccess = MutableLiveData<Boolean>()
    val departmentListSuccess: MutableLiveData<Boolean> get() = _departmentListSuccess

    fun departmentList(uid: String) {
        connectRepository.departmentList(uid) { success, departmentList ->
            if (DefaultSetting.debugMode) {
                _departmentList.value = DefaultSetting.debugDepartmentList
                _departmentListSuccess.value = true
            } else {
                if (!success) {
                    Log.d("SelectDepartmentViewModel", "Failed to fetch department list")
                }
                _departmentList.value = departmentList // false -> 빈 리스트
                _departmentListSuccess.value = success  // true or false : false -> 학과 리스트 받기 실패
            }
        }
    }

    fun saveDepartment(context: Context, uid:String, department: String,  callback: (Boolean) -> Unit) {
        connectRepository.departmentSelect(uid, department) { success ->
            if (DefaultSetting.debugMode) {
                savePrefDepartment(context, department)
                callback(true)
            } else {
                if (!success) {
                    Log.d("SelectDepartmentViewModel", "Failed to save department")
                }
                savePrefDepartment(context, department)
                callback(success)
            }
        }

    }

    fun savePrefDepartment(context: Context, department: String) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("department", department)
            apply()
        }
    }

}