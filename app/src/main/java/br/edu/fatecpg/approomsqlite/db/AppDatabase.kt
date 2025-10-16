package br.edu.fatecpg.approomsqlite.db

import androidx.room.Database
import androidx.room.RoomDatabase
import br.edu.fatecpg.approomsqlite.dao.UserDao
import br.edu.fatecpg.approomsqlite.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
}