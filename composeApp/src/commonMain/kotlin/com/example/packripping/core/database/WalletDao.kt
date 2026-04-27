package com.example.packripping.core.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    @Query("SELECT balanceCents FROM wallet WHERE id = 0")
    fun observeBalance(): Flow<Long?>

    @Query("INSERT OR IGNORE INTO wallet (id, balanceCents) VALUES (0, 0)")
    suspend fun ensureRow()

    @Query("UPDATE wallet SET balanceCents = balanceCents + :delta WHERE id = 0")
    suspend fun creditDelta(delta: Long)

    @Transaction
    suspend fun credit(delta: Long) {
        ensureRow()
        creditDelta(delta)
    }
}
