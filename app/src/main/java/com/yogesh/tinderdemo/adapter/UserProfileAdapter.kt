package com.yogesh.tinderdemo.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yogesh.tinderdemo.R
import com.yogesh.tinderdemo.model.Result
import kotlinx.android.synthetic.main.user_profile_item.view.*

class UserProfileAdapter(_context: Context) :
    ListAdapter<Result, UserProfileAdapter.UsersViewHolder>(TaskDiffCallback()) {

    private val mContext = _context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_profile_item, parent, false)
        return UsersViewHolder(view)
    }

    class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, user: Result) {
            itemView.tv_name.text = user.name.getFullName()
            itemView.tv_detail.text = user.phone
            Glide.with(itemView.context)
                .load(user.picture.large)
                .into(itemView.profile_image)

            val likedValueString =
                "${context.getString(R.string.profile)} ${user.extraInfo}"
            if (!TextUtils.isEmpty(user.extraInfo)) {
            }
            itemView.callOnClick()
        }
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(mContext, item)
    }

    fun getItemFromAdapter(position: Int): Result {
        return getItem(position)
    }
}

// Used to add new items.
class TaskDiffCallback : DiffUtil.ItemCallback<Result>() {
    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.email == newItem.email
    }

    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem == newItem
    }
}