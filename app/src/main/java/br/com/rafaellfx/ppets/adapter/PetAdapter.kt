package br.com.rafaellfx.ppets.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.rafaellfx.ppets.AboutPet
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.model.Pet
import kotlinx.android.synthetic.main.item_pet.view.*

class PetAdapter(private val myDataset: List<Pet>, private val context: Context) :

    RecyclerView.Adapter<PetAdapter.MyViewHolder>() {

    class MyViewHolder(val viewRoot: View) : RecyclerView.ViewHolder(viewRoot)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): PetAdapter.MyViewHolder {

        val textView = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)

        return MyViewHolder(textView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.viewRoot.txtName.text = "${myDataset[position].name}"
        holder.viewRoot.setOnClickListener {
            val intent = Intent(context, AboutPet::class.java)
            intent.putExtra("pet", myDataset[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = myDataset.size

}