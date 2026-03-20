package com.robertcoding.data.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [NewzyEntity::class], version = 1, exportSchema = false)
abstract class NewzyRoomDatabase : RoomDatabase() {

    abstract fun newzyDao(): NewzyDao

    companion object {
        const val DATABASE_NAME = "bookmarks"
    }

}