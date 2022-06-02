package com.sleepy.erik.diplom.dao

import androidx.room.*
import com.sleepy.erik.diplom.data.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM User where email= :mail and password= :password")
    fun getUser(mail: String?, password: String?): User?

    @Query("SELECT * FROM User where email= :mail")
    fun getUserData(mail: String?): User?

    @Query("SELECT * FROM user ORDER BY first_name ASC")
    fun getAlphabetizedWords(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun preInsertData(user: List<User>)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Query("DELETE FROM user where email= :mail")
    suspend fun delete(mail: String?)
}