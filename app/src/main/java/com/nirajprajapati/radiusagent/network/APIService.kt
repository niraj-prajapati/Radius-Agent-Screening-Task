package com.nirajprajapati.radiusagent.network

import com.nirajprajapati.radiusagent.BuildConfig
import com.nirajprajapati.radiusagent.data.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @GET(BuildConfig.DB)
    suspend fun fetchData(): Response<ApiResponse>
}