package br.edu.fatecpg.approomsqlite.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import br.edu.fatecpg.approomsqlite.R
import br.edu.fatecpg.approomsqlite.dao.UserDao
import br.edu.fatecpg.approomsqlite.databinding.ActivityMainBinding
import br.edu.fatecpg.approomsqlite.db.AppDatabase
import br.edu.fatecpg.approomsqlite.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db:AppDatabase
    private lateinit var dao:UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db-users"
        ).fallbackToDestructiveMigrationFrom()
            .build()

        dao = db.userDao()

        binding.btnSave.setOnClickListener(){
            val firstName = binding.edtFirstName.text.toString()
            val lastName = binding.edtLastName.text.toString()
            val user = User(0, firstName, lastName)
            lifecycleScope.launch{
                dao.insertAll(user)
                Toast.makeText(applicationContext, "Cadastrado com Sucesso!",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnList.setOnClickListener(){
            lifecycleScope.launch {
                val users = withContext(Dispatchers.IO) {
                    dao.getAll()
                }

                users.forEach{
                    Log.d("Lista", "${it.id} - ${it.firstName} - ${it.lastName}")
                }
            }
        }

        binding.btnList.setOnClickListener {
            val intent = Intent(this, ListaUsuariosActivity::class.java)
            startActivity(intent)
        }
    }
}