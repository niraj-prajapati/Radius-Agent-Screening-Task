package com.nirajprajapati.radiusagent.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.Realm

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Provides
    fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }
}