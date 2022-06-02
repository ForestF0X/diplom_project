package com.sleepy.erik.diplom.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sleepy.erik.diplom.R
import com.sleepy.erik.diplom.documentsscreens.fragments.Frag5


class MyVH(itemView: View): RecyclerView.ViewHolder(itemView){
    fun bind(image: Uri){
        val imageView = itemView.findViewById<ImageView>(R.id.imageItem)
        Glide.with(itemView.context).load(image).centerCrop().into(imageView)
    }
}

class RecyclerAdapter(var context: Context, private val images: ArrayList<Uri>): RecyclerView.Adapter<MyVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        this.context = parent.context
        return MyVH(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}