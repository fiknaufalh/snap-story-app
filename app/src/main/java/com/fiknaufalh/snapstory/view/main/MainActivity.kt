package com.fiknaufalh.snapstory.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiknaufalh.snapstory.R
import com.fiknaufalh.snapstory.adapters.StoryAdapter
import com.fiknaufalh.snapstory.data.remote.responses.StoryResponse
import com.fiknaufalh.snapstory.databinding.ActivityMainBinding
import com.fiknaufalh.snapstory.utils.ViewModelFactory
import com.fiknaufalh.snapstory.view.detail.DetailActivity
import com.fiknaufalh.snapstory.view.upload.UploadActivity
import com.fiknaufalh.snapstory.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setErrorView(false)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)

        binding.rvStory.layoutManager = layoutManager
        binding.rvStory.addItemDecoration(itemDecoration)

        binding.fabUpload.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        viewModel.fetchStories()
        viewModel.stories.observe(this) {
            stories -> setStoryList(stories)
            if (stories.listStory.isEmpty()) {
                setErrorView(true)
            } else {
                setErrorView(false)
            }
        }

        viewModel.isLoading.observe(this) {
            isLoading -> showLoading(isLoading)
        }

        viewModel.errorToast.observe(this) {
            errorToast -> errorToast?.let {
                if (errorToast) {
                    Toast.makeText(this, "Success to retrieve the data", Toast.LENGTH_SHORT).show()
                    viewModel.resetToast()
                } else {
                    Toast.makeText(this, "Failed to retrieve the data", Toast.LENGTH_SHORT).show()
                    viewModel.resetToast()
                    setErrorView(true)
                }
            }
        }

        binding.btnRetry.setOnClickListener {
            setErrorView(false)
            viewModel.fetchStories()
        }

        setupView()
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchStories()
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

    private fun setupAction() {
        binding.ivLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun setStoryList(stories: StoryResponse) {
        val adapter = StoryAdapter()
        adapter.submitList(stories.listStory)
        binding.rvStory.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setErrorView(isError: Boolean) {
        val isShow = if (isError) View.VISIBLE else View.GONE
        binding.ivError.visibility = isShow
        binding.tvError.visibility = isShow
        binding.btnRetry.visibility = isShow
        binding.rvStory.visibility = if (isError) View.GONE else View.VISIBLE
    }
}