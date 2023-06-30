package com.nirajprajapati.radiusagent.util

import java.util.Calendar

class Extensions {
    companion object {
        fun Long.isToday(): Boolean {
            val targetCalendar = Calendar.getInstance().apply {
                timeInMillis = this@isToday
            }

            val currentCalendar = Calendar.getInstance()

            return (targetCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
                    && targetCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR))
        }
    }
}