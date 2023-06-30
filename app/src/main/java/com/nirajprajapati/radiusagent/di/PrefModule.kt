package com.nirajprajapati.radiusagent.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.nirajprajapati.radiusagent.util.Const
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object PrefModule {

    @Provides
    fun provideSharedPreferences(context: Application): SharedPreferences =
        context.getSharedPreferences(Const.TAG, MODE_PRIVATE)
}