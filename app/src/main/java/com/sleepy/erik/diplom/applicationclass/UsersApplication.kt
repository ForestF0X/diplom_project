package com.sleepy.erik.diplom.applicationclass

import android.app.Application
import com.sleepy.erik.diplom.database.UserRoomDatabase
import com.sleepy.erik.diplom.dbrepo.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class UsersApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { UserRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { UserRepository(database.userDao()) }
}