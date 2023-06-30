package com.nirajprajapati.radiusagent.data

import io.realm.RealmObject

open class OptionRealm(
    var icon: String = "",
    var id: String = "",
    var name: String = "",
    var exclusionOptionId: String = "0"
) : RealmObject()