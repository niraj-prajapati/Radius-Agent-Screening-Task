package com.nirajprajapati.radiusagent.base

import android.app.Application
import com.nirajprajapati.radiusagent.util.Const
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class RadiusAgentApplication : Application() {

    private var realmConfig: RealmConfiguration? = null

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        realmConfig = RealmConfiguration.Builder()
            .name(Const.DB_NAME)
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .allowWritesOnUiThread(true)
            .build()
        realmConfig?.let { Realm.setDefaultConfiguration(it) }
    }
}
