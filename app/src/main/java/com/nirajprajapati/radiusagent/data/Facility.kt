package com.nirajprajapati.radiusagent.data


import com.google.gson.annotations.SerializedName

data class Facility(
    @SerializedName("facility_id")
    val facilityId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("options")
    val options: List<Option>
)