package br.edu.fatecpg.approomsqlite.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.approomsqlite.R
import br.edu.fatecpg.approomsqlite.model.User

class UserAdapter(
    private val users: MutableList<User>,
    private val onDeleteClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txvId = itemView.findViewById<TextView>(R.id.txv_id)
        val txvFirstName = itemView.findViewById<TextView>(R.id.txv_first_name)
        val txvLastName = itemView.findViewById<TextView>(R.id.txv_last_name)
        val btnDeletar = itemView.findViewById<Button>(R.id.btn_deletar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usuario_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        holder.txvId.text = user.id.toString()
        holder.txvFirstName.text = user.firstName
        holder.txvLastName.text = user.lastName

        holder.btnDeletar.setOnClickListener {
            onDeleteClick(user)
        }
    }

    override fun getItemCount(): Int = users.size

    fun updateUsers(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    fun removeUser(user: User) {
        val pos = users.indexOf(user)
        if (pos != -1) {
            users.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }
}
