package br.edu.fatecpg.approomsqlite.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.edu.fatecpg.approomsqlite.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll():List<User>
    @Insert
    // vai criar uma subrotina
    suspend fun insertAll(vararg user:User)
}