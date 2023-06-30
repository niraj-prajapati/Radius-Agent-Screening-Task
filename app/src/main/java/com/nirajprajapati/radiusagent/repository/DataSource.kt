package com.nirajprajapati.radiusagent.repository


import com.nirajprajapati.radiusagent.data.ApiResponse
import com.nirajprajapati.radiusagent.network.APIService
import com.nirajprajapati.radiusagent.network.Result
import com.nirajprajapati.radiusagent.util.DataSourceUtils
import retrofit2.Retrofit
import javax.inject.Inject

class DataSource @Inject constructor(
    private val retrofit: Retrofit,
    private val dataSourceUtils: DataSourceUtils,
) {

    suspend fun fetchData(): Result<ApiResponse> {
        val apiService = retrofit.create(APIService::class.java)
        return dataSourceUtils.getResponse(
            request = { apiService.fetchData() },
            defaultErrorMessage = "Error fetching data"
        )
    }
}