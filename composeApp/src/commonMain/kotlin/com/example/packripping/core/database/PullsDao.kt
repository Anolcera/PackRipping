package com.example.packripping.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.packripping.core.database.entities.PullEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PullsDao {

    @Query("SELECT * FROM pulls ORDER BY pulledAtMillis DESC")
    fun observeAll(): Flow<List<PullEntity>>

    @Query("SELECT * FROM pulls WHERE isStored = 1 ORDER BY pulledAtMillis DESC")
    fun observeStored(): Flow<List<PullEntity>>

    @Insert
    suspend fun insert(pull: PullEntity)
}
