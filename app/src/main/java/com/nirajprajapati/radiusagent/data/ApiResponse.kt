package com.nirajprajapati.radiusagent.data


import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("exclusions")
    val exclusions: List<List<Exclusion>>,
    @SerializedName("facilities")
    val facilities: List<Facility>
)