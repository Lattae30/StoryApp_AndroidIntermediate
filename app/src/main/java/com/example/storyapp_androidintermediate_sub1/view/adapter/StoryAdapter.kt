package com.example.storyapp_androidintermediate_sub1.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp_androidintermediate_sub1.databinding.ItemStoriesBinding
import com.example.storyapp_androidintermediate_sub1.retrofit.response.ListStoryItem

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK){
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback (onItemClickedCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickedCallback
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: ListStoryItem)
    }

    inner class StoryViewHolder(private val binding: ItemStoriesBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(stories: ListStoryItem){
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(stories)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(stories.photoUrl.toString())
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(ivPost)
                tvRvName.text = stories.name
                tvRvDescription.text = stories.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): StoryViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val stories = getItem(position)
        if (stories != null) {
            holder.bind(stories)
        }
    }

    companion object{
        val DIFF_CALLBACK = object  : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldStory: ListStoryItem, newStory: ListStoryItem): Boolean {
                return oldStory == newStory
            }
            override fun areContentsTheSame(oldStory: ListStoryItem, newStory: ListStoryItem): Boolean {
                return oldStory == newStory
            }
        }
    }
}