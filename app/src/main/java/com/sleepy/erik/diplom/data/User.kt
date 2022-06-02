package com.sleepy.erik.diplom.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val email: String,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "login") val login: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "documents_send") val documentsSend: Boolean,
    @ColumnInfo(name = "signed_in") val signedIn: Boolean
)