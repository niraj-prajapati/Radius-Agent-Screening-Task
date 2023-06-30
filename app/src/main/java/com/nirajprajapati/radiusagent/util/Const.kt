package com.nirajprajapati.radiusagent.util

import com.nirajprajapati.radiusagent.R

class Const {
    companion object {
        const val TAG = "RADIUS_AGENT"
        const val DB_NAME = "radiusagent.db"
        const val PREF_LAST_SYNCED = "pref_last_synced"

        val iconMap = mapOf(
            "apartment" to R.drawable.ic_apartment,
            "condo" to R.drawable.ic_condo,
            "boat" to R.drawable.ic_boat,
            "land" to R.drawable.ic_land,
            "rooms" to R.drawable.ic_rooms,
            "no-room" to R.drawable.ic_no_room,
            "swimming" to R.drawable.ic_swimming,
            "garden" to R.drawable.ic_garden,
            "garage" to R.drawable.ic_garage
        )
    }
}