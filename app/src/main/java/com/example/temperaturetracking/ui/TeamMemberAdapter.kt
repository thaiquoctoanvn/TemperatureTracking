package com.example.temperaturetracking.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.temperaturetracking.R
import com.example.temperaturetracking.data.entity.Member
import kotlinx.android.synthetic.main.item_member_team.view.*

class TeamMemberAdapter : ListAdapter<Member, TeamMemberAdapter.TeamMemberViewHolder>(TeamMemberDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TeamMemberViewHolder(inflater.inflate(R.layout.item_member_team, parent, false))
    }

    override fun onBindViewHolder(holder: TeamMemberViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class TeamMemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(member: Member) {
            Glide.with(itemView)
                .load(member.imageUrl)
                .placeholder(R.drawable.ic_app)
                .error(R.drawable.ic_app)
                .circleCrop()
                .into(itemView.ivMem)
            itemView.tvMemName.text = member.name
        }
    }

}

class TeamMemberDiffUtil : DiffUtil.ItemCallback<Member>() {
    override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
        return oldItem.imageUrl == newItem.imageUrl
    }
}