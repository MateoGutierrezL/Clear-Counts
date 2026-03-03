package com.example.clearcounts.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.clearcounts.data.database.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Update
    suspend fun updateNotification(notification: NotificationEntity)

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()

    @Query("UPDATE notifications SET leida = 1")
    suspend fun marcarTodasLeidas()
}