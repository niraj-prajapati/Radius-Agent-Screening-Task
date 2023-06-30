package com.nirajprajapati.radiusagent.repository

import com.nirajprajapati.radiusagent.data.ApiResponse
import com.nirajprajapati.radiusagent.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val dataSource: DataSource) {

    fun fetchData(): Flow<Result<ApiResponse>> = flow {
        emit(Result.loading())
        emit(dataSource.fetchData())
    }.flowOn(Dispatchers.IO)
}