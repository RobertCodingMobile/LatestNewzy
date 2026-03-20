package com.robertcoding.data.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NewzyDao {

    @Query("SELECT * FROM newzy")
    fun getAll(): Flow<List<NewzyEntity>>


    @Query("SELECT * FROM newzy WHERE id = :id")
    suspend fun getById(id: Int): NewzyEntity

    @Upsert
    suspend fun upsertAll(newzy: List<NewzyEntity>)

}