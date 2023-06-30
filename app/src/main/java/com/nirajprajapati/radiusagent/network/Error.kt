package com.nirajprajapati.radiusagent.network

data class Error(
    val statusCode: Int = 0,
    val statusMessage: String? = null
)