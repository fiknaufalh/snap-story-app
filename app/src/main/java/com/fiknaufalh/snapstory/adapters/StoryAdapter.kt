package com.fiknaufalh.snapstory.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fiknaufalh.snapstory.R
import com.fiknaufalh.snapstory.adapters.StoryAdapter.MyViewHolder
import com.fiknaufalh.snapstory.data.remote.responses.StoryItem
import com.fiknaufalh.snapstory.databinding.ItemStoryCardBinding
import com.fiknaufalh.snapstory.view.detail.DetailActivity

class StoryAdapter():
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

                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(itemView.context.resources.getString(R.string.passing_name), story.name)
                intent.putExtra(itemView.context.resources.getString(R.string.passing_desc), story.description)
                intent.putExtra(itemView.context.resources.getString(R.string.passing_image), story.photoUrl)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.storyUser, "storyUser"),
                        Pair(binding.storyDesc, "storyDesc"),
                        Pair(binding.storyImage, "storyImage")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
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