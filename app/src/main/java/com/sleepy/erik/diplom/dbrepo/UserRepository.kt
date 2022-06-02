package com.sleepy.erik.diplom.dbrepo

import androidx.annotation.WorkerThread
import com.sleepy.erik.diplom.dao.UserDao
import com.sleepy.erik.diplom.data.User
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class UserRepository(private val userDao: UserDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allUsers: Flow<List<User>> = userDao.getAlphabetizedWords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(user: User) {
        userDao.update(user)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(mail: String) {
        userDao.delete(mail)
    }
}