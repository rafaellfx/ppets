package br.com.rafaellfx.ppets.adapter

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.model.Location
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.LocationService
import br.com.rafaellfx.ppets.ui.listpets.ListPetsFragmentDirections
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_pet.view.*

class PetAdapter() :

    RecyclerView.Adapter<PetAdapter.MyViewHolder>() {

    private var listPet: MutableList<Pet> = mutableListOf()
    private var context:Context? = null

    class MyViewHolder(val viewRoot: View) : RecyclerView.ViewHolder(viewRoot)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetAdapter.MyViewHolder {

        context = parent.context
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet, parent, false)

        return MyViewHolder(textView)

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        LocationService.findId(listPet[position].locationId.last()).get().addOnSuccessListener { documentSnapshot ->

            holder.viewRoot.txtName.text = "${listPet[position].name}"

            loadImage(holder.viewRoot.imgPet, "${listPet[position].photoUrl}")
            val geocoder = Geocoder(context)
            val address =  geocoder.getFromLocation(
                documentSnapshot.data?.get("latitude") as Double,
                documentSnapshot.data?.get("longitude") as Double,1)

            holder.viewRoot.txtLocation.text = "${address[0].thoroughfare}, ${address[0].featureName}"

            holder.viewRoot.imgAbout.setOnClickListener {
                Navigation.findNavController(it).navigate(ListPetsFragmentDirections.actionNavigationHomeToAboutPetFragment(listPet[position]))
            }

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
                    .override(680, 680)
                    .into(view)
            }
        }
    }

    override fun getItemCount() = listPet.size

    fun update(listIntemPet: List<Pet>) {
        listPet.clear()
        listPet.addAll(listIntemPet)
        notifyDataSetChanged()
    }

}