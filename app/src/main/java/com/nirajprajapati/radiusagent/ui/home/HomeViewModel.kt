package com.nirajprajapati.radiusagent.ui.home

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nirajprajapati.radiusagent.base.BaseViewModel
import com.nirajprajapati.radiusagent.data.ApiResponse
import com.nirajprajapati.radiusagent.data.Exclusion
import com.nirajprajapati.radiusagent.data.FacilityRealm
import com.nirajprajapati.radiusagent.data.OptionRealm
import com.nirajprajapati.radiusagent.network.Result
import com.nirajprajapati.radiusagent.repository.DataRepository
import com.nirajprajapati.radiusagent.util.Const
import com.nirajprajapati.radiusagent.util.Extensions.Companion.isToday
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.Realm
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val realm: Realm,
    private val dataRepository: DataRepository,
    sharedPreferences: SharedPreferences,
) : BaseViewModel() {

    val facilitiesLiveData: MutableLiveData<List<FacilityRealm>> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        val lastSynced = sharedPreferences.getLong(Const.PREF_LAST_SYNCED, 0L)
        if (lastSynced.isToday().not()) {
            fetchData()
            sharedPreferences.edit().apply {
                putLong(Const.PREF_LAST_SYNCED, System.currentTimeMillis())
                apply()
            }
        }
        fetchFacilitiesFromRealm()
    }

    private fun fetchData() {
        viewModelScope.launch {
            dataRepository.fetchData().collect { result ->
                when (result.status) {
                    Result.Status.SUCCESS -> {
                        loadingLiveData.value = false
                        val apiResponse = result.data
                        apiResponse?.let { mapApiResponseToRealm(it) }
                        fetchFacilitiesFromRealm()
                    }

                    Result.Status.ERROR -> {
                        loadingLiveData.value = false
                        errorLiveData.value =
                            "Error fetching facilities from Realm: ${result.error}"
                    }

                    Result.Status.LOADING -> {
                        loadingLiveData.value = true
                    }
                }
            }
        }
    }

    private fun mapApiResponseToRealm(apiResponse: ApiResponse) {
        realm.executeTransaction { transactionRealm ->
            transactionRealm.deleteAll()
        }

        apiResponse.facilities.forEach { facility ->
            val facilityRealm = FacilityRealm()
            facilityRealm.facilityId = facility.facilityId
            facilityRealm.name = facility.name

            facilityRealm.options.addAll(facility.options.map { option ->
                val exclusionOptionId =
                    findExclusionOptionId(apiResponse.exclusions, facility.facilityId, option.id)
                OptionRealm(option.icon, option.id, option.name, exclusionOptionId)
            })

            realm.executeTransaction { transactionRealm ->
                transactionRealm.insertOrUpdate(facilityRealm)
            }
        }
    }

    private fun findExclusionOptionId(
        exclusions: List<List<Exclusion>>,
        facilityId: String,
        optionId: String
    ): String {
        val exclusionList = exclusions.find { list ->
            list.any { exclusion ->
                exclusion.facilityId == facilityId && exclusion.optionsId == optionId
            }
        }

        return exclusionList?.find { it.facilityId > facilityId }?.optionsId ?: "0"
    }

    private fun fetchFacilitiesFromRealm() {
        loadingLiveData.value = true

        try {
            val facilities = realm.where(FacilityRealm::class.java).findAll()
            facilitiesLiveData.value = realm.copyFromRealm(facilities)

            loadingLiveData.value = false
        } catch (e: Exception) {
            val errorMessage = "Error fetching facilities from Realm: ${e.message}"
            errorLiveData.value = errorMessage

            loadingLiveData.value = false
            Log.e("Fetch Facilities", errorMessage, e)
        }
    }
}