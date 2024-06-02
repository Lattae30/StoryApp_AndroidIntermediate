package com.example.storyapp_androidintermediate_sub1.view.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp_androidintermediate_sub1.R
import com.example.storyapp_androidintermediate_sub1.view.adapter.StoryAdapter
import com.example.storyapp_androidintermediate_sub1.databinding.ActivityMainBinding
import com.example.storyapp_androidintermediate_sub1.retrofit.response.ListStoryItem
import com.example.storyapp_androidintermediate_sub1.view.ViewModelFactory
import com.example.storyapp_androidintermediate_sub1.view.adapter.LoadingStateAdapter
import com.example.storyapp_androidintermediate_sub1.view.add.AddStoryActivity
import com.example.storyapp_androidintermediate_sub1.view.detail.DetailActivity
import com.example.storyapp_androidintermediate_sub1.view.login.LoginActivity
import com.example.storyapp_androidintermediate_sub1.view.maps.MapsActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        val layoutManager = LinearLayoutManager(this)
        binding?.rvStoryList?.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvStoryList?.addItemDecoration(itemDecoration)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else{
                setStoryList()
            }
        }

        setupView()
        setupAction()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setStoryList(){
        val adapter = StoryAdapter()
        binding?.rvStoryList?.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        
        viewModel.story.observe(this, {
            adapter.submitData(lifecycle, it)
        })

        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListStoryItem) {
                Intent(this@MainActivity, DetailActivity::class.java).also{
                    it.putExtra(DetailActivity.EXTRA_PHOTO, data.photoUrl)
                    it.putExtra(DetailActivity.EXTRA_NAME, data.name)
                    it.putExtra(DetailActivity.EXTRA_DESC, data.description)
                    startActivity(it)
                }
            }
        })
    }

    private fun setupAction() {
        binding?.toAppBar?.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.menu1 -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }
                R.id.menu2 -> {
                    AlertDialog.Builder(this).apply {
                        setTitle("Are you sure want to logging out?")
                        setPositiveButton("Yes") { _, _ ->
                            viewModel.logout()
                            showLoading(true)
                        }
                        setNegativeButton("No"){dialog, _ ->
                            dialog.dismiss()
                        }
                        create()
                        show()
                    }
                    true
                }
                else -> false
            }
        }
        binding?.fabAddNewStory?.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}