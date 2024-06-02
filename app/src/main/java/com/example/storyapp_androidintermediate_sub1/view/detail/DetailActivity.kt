package com.example.storyapp_androidintermediate_sub1.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.storyapp_androidintermediate_sub1.databinding.ActivityDetailBinding
import com.example.storyapp_androidintermediate_sub1.view.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val storyPhoto = intent.getStringExtra(EXTRA_PHOTO)
        val userName = intent.getStringExtra(EXTRA_NAME)
        val storyDesc = intent.getStringExtra(EXTRA_DESC)

        viewModel.story.observe(this){
            binding?.apply {
                tvNameDetail.text = userName
                tvDescDetail.text = storyDesc
                Glide.with(this@DetailActivity)
                    .load(storyPhoto)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(ivPostDetail)
            }
        }
    }

    companion object{
        const val EXTRA_PHOTO = "extra_photo"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_description"
    }
}