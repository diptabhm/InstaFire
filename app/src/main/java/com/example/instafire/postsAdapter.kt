package com.example.instafire

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instafire.model.posts
import java.math.BigInteger
import java.security.MessageDigest


class postsAdapter(val context: Context, val posts: List<posts>) :
    RecyclerView.Adapter<postsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(posts: posts) {
            itemView.findViewById<TextView>(R.id.tvUsername).text = posts.user?.username
            itemView.findViewById<TextView>(R.id.tvDescription).text = posts.description
            Glide.with(context).load(posts.image_url).into(itemView.findViewById(R.id.ivPost))
            Glide.with(context).load(getProfileImageUrl(posts.user?.username as String)).into(itemView.findViewById(R.id.ivProfileImage))
            itemView.findViewById<TextView>(R.id.tvRelativetime).text = DateUtils.getRelativeDateTimeString(context, posts.creation_time, DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0)
        }

        private fun getProfileImageUrl(username : String):String{
            val digest = MessageDigest.getInstance("MD5")
            val hash = digest.digest(username.toByteArray())
            val bigInt = BigInteger(hash)
            val hex = bigInt.abs().toString(16)
            return "https://www.gravatar.com/avatar/$hex?d=identicon"
        }
    }
}