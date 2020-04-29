package br.com.rafaellfx.ppets.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.rafaellfx.ppets.AboutPet
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.model.Pet
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.item_pet.view.*
import kotlin.math.acos

class PetAdapter(private val myDataset: List<Pet>, private val context: Context) :

    RecyclerView.Adapter<PetAdapter.MyViewHolder>() {

    class MyViewHolder(val viewRoot: View) : RecyclerView.ViewHolder(viewRoot)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PetAdapter.MyViewHolder {

        val textView = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)

        return MyViewHolder(textView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.viewRoot.txtName.text = "${myDataset[position].name}"

        loadImage(holder.viewRoot.imgPet, "${myDataset[position].photoUrl}")

        holder.viewRoot.setOnClickListener {
            val intent = Intent(context, AboutPet::class.java)
            intent.putExtra("pet", myDataset[position])
            context.startActivity(intent)
        }
    }

    companion object {
        @BindingAdapter("imageUrl")
        @JvmStatic
        fun loadImage(view: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(view.context)
                    .load(url)
                    //.centerCrop()
                    .fitCenter()
                    .override(680,680)
                    .into(view);
            }
        }
    }

    override fun getItemCount() = myDataset.size

}