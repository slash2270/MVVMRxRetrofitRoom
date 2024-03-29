package com.example.mvvmretrofit.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvmretrofit.bean.ColorBean

@Database(entities = [ColorBean::class], version = 3, exportSchema = false)
abstract class DataBase : RoomDatabase() {
    abstract fun dbDao(): ColorDao
}