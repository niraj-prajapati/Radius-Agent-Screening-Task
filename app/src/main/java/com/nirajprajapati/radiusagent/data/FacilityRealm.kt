package com.nirajprajapati.radiusagent.data

import io.realm.RealmList
import io.realm.RealmObject

open class FacilityRealm(
    var facilityId: String = "",
    var name: String = "",
    var options: RealmList<OptionRealm> = RealmList(),
) : RealmObject()
