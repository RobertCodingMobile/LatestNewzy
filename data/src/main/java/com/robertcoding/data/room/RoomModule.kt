package com.robertcoding.data.room

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomModule = module {

    single<NewzyDao> { get<NewzyRoomDatabase>().newzyDao() }

    single<NewzyRoomDatabase> {
        Room.databaseBuilder<NewzyRoomDatabase>(
            androidContext(),
            NewzyRoomDatabase.DATABASE_NAME
        ).build()
    }
}