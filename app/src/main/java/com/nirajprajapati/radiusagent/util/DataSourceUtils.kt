package com.nirajprajapati.radiusagent.util

import android.util.Log
import com.nirajprajapati.radiusagent.network.Error
import com.nirajprajapati.radiusagent.network.Result
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

class DataSourceUtils @Inject constructor(private val retrofit: Retrofit) {

    suspend fun <T> getResponse(
        request: suspend () -> Response<T>, defaultErrorMessage: String
    ): Result<T> {
        return try {
            val result = request.invoke()
            when {
                result.isSuccessful -> {
                    return Result.success(result.body())
                }

                else -> {
                    val errorResponse = parseError(result, retrofit)
                    Result.error(
                        errorResponse?.statusMessage ?: defaultErrorMessage, errorResponse
                    )
                }
            }
        } catch (e: Throwable) {
            Log.e(Const.TAG, "getResponse: $e")
            Result.error("Unknown Error", null)
        }
    }


    private fun parseError(response: Response<*>, retrofit: Retrofit): Error? {
        val converter = retrofit.responseBodyConverter<Error>(Error::class.java, arrayOfNulls(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            Error()
        }
    }
}