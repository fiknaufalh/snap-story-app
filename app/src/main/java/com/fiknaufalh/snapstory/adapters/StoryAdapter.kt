package com.fiknaufalh.snapstory.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fiknaufalh.snapstory.R
import com.fiknaufalh.snapstory.adapters.StoryAdapter.MyViewHolder
import com.fiknaufalh.snapstory.data.remote.responses.StoryItem
import com.fiknaufalh.snapstory.databinding.ItemStoryCardBinding

class StoryAdapter(private val onClickCard: (StoryItem) -> Unit):
    ListAdapter<StoryItem, MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryCardBinding.
        inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    inner class MyViewHolder(private val binding: ItemStoryCardBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryItem) {
            binding.storyUser.text = story.name
            binding.storyDesc.text = story.description
            Glide.with(binding.root)
                .load(story.photoUrl)
                .placeholder(R.color.navy)
                .into(binding.storyImage)
            Log.d("StoryAdapter", "Binding story: ${story.name}")
            itemView.setOnClickListener {
                Log.d("StoryAdapter", "Clicked on story: ${story.name}")
                onClickCard(story)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame (oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame (oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}