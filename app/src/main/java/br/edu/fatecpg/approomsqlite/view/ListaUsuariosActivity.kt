package br.edu.fatecpg.approomsqlite.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import br.edu.fatecpg.approomsqlite.R
import br.edu.fatecpg.approomsqlite.dao.UserDao
import br.edu.fatecpg.approomsqlite.db.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var dao: UserDao
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_lista_usuarios)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val fabVoltar = findViewById<FloatingActionButton>(R.id.fab_voltar)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db-users"
        ).fallbackToDestructiveMigrationFrom()
            .build()

        dao = db.userDao()

        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_usuarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = UserAdapter(mutableListOf()) { userToDelete ->
            // Excluir usuário no banco
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    dao.delete(userToDelete)
                }
                // Atualizar lista na UI
                adapter.removeUser(userToDelete)
                Toast.makeText(this@ListaUsuariosActivity, "Usuário excluído", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.adapter = adapter

        // Carregar os usuários do banco
        lifecycleScope.launch {
            val users = withContext(Dispatchers.IO) {
                dao.getAll()
            }
            adapter.updateUsers(users)
        }

        fabVoltar.setOnClickListener {
            finish()
        }
    }
}